package fr.univamu.iut.menus;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Classe utilisée pour récupérer les informations nécessaires à la ressource
 * (permet de dissocier ressource et mode d'accès aux données)
 */
public class MenuService {

    /**
     * Objet permettant d'accéder au dépôt où sont stockées les informations sur les menus
     */
    protected MenuRepositoryInterface menuRepo ;

    /**
     * Client HTTP utilisé pour enrichir les menus avec les APIs externes.
     */
    protected ExternalApiClient externalApiClient;

    /**
     * Constructeur permettant d'injecter l'accès aux données
     * @param menuRepo objet implémentant l'interface d'accès aux données
     */
    public  MenuService( MenuRepositoryInterface menuRepo) {
        this(menuRepo, new ExternalApiClient());
    }

    public MenuService(MenuRepositoryInterface menuRepo, ExternalApiClient externalApiClient) {
        this.menuRepo = Objects.requireNonNull(menuRepo, "Le repository de menus ne peut pas etre null");
        this.externalApiClient = Objects.requireNonNull(externalApiClient, "ExternalApiClient ne peut pas etre null");
    }

    /**
     * Méthode retournant les informations sur les menus au format JSON
     * @return une chaîne de caractère contenant les informations au format JSON
     */
    public String getAllMenusJSON(){

        List<Menu> allMenus = menuRepo.getAllMenus();
        enrichMenus(allMenus);

        // ...existing code...
        return buildMenusJson(allMenus);
    }

    /**
     * Méthode retournant au format JSON les informations sur un menu recherché
     * @param id l'identifiant du menu recherché
     * @return une chaîne de caractère contenant les informations au format JSON
     */
    public String getMenuJSON( Integer id ){
        String result = null;
        Menu myMenu = menuRepo.getMenu(id);

        // si le menu a été trouvé
        if( myMenu != null ) {
            enrichMenu(myMenu, externalApiClient.fetchUserNamesById(), externalApiClient.fetchDishesById());
            List<Menu> menuList = new ArrayList<>();
            menuList.add(myMenu);
            result = buildMenusJson(menuList);
        }
        return result;
    }

    /**
     * Méthode permettant de mettre à jours les informations d'un menu
     * @param id identifiant du menu à mettre à jours
     * @param menu les nouvelles informations a etre utilisé
     * @return true si le livre a pu être mis à jours
     */
    public boolean updateMenu(Integer id, Menu menu) {
        return menuRepo.updateMenu(id, menu.name, menu.creatorId, menu.updateDate, menu.dishes);
    }

    private void enrichMenus(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return;
        }

        Map<Integer, String> userNames = externalApiClient.fetchUserNamesById();
        Map<Integer, ExternalApiClient.DishData> dishes = externalApiClient.fetchDishesById();

        for (Menu menu : menus) {
            enrichMenu(menu, userNames, dishes);
        }
    }

    private void enrichMenu(Menu menu, Map<Integer, String> userNames, Map<Integer, ExternalApiClient.DishData> dishesById) {
        if (menu == null) {
            return;
        }

        if (userNames != null && menu.getCreatorId() != null) {
            menu.setCreatorName(userNames.get(menu.getCreatorId()));
        }

        ArrayList<Menu.DishSummary> dishList = menu.getDishes();
        if (dishList != null) {
            for (Menu.DishSummary dish : dishList) {
                if (dish == null || dish.getId() == null || dishesById == null) {
                    continue;
                }

                ExternalApiClient.DishData dishData = dishesById.get(dish.getId());
                if (dishData != null) {
                    dish.setName(dishData.getName());
                    dish.setPrice(dishData.getPrice());
                }
            }
        }

        menu.recomputeTotalPrice();
    }

    public static class MenusResponse {
        private final List<Menu> menus;

        public MenusResponse(List<Menu> menus) {
            this.menus = menus != null ? menus : new ArrayList<>();
        }

        public List<Menu> getMenus() {
            return menus;
        }
    }

    private String buildMenusJson(List<Menu> menus) {
        JsonArrayBuilder menusArray = Json.createArrayBuilder();

        for (Menu menu : menus) {
            JsonArrayBuilder dishArray = Json.createArrayBuilder();
            if (menu.getDishes() != null) {
                for (Menu.DishSummary dish : menu.getDishes()) {
                    JsonObjectBuilder dishObj = Json.createObjectBuilder()
                            .add("id", dish.getId())
                            .add("name", dish.getName() != null ? dish.getName() : "")
                            .add("price", dish.getPrice() != null ? dish.getPrice().doubleValue() : 0.0);
                    dishArray.add(dishObj.build());
                }
            }

            JsonObjectBuilder menuObj = Json.createObjectBuilder()
                    .add("id", menu.getId())
                    .add("name", menu.getName() != null ? menu.getName() : "")
                    .add("creatorId", menu.getCreatorId() != null ? menu.getCreatorId() : 0)
                    .add("creatorName", menu.getCreatorName() != null ? menu.getCreatorName() : "")
                    .add("creationDate", menu.getCreationDate() != null ? menu.getCreationDate().toString() : "")
                    .add("updateDate", menu.getUpdateDate() != null ? menu.getUpdateDate().toString() : "")
                    .add("dishes", dishArray.build())
                    .add("totalPrice", menu.getTotalPrice() != null ? menu.getTotalPrice().doubleValue() : 0.0);

            menusArray.add(menuObj.build());
        }

        return Json.createObjectBuilder()
                .add("menus", menusArray.build())
                .build()
                .toString();
    }
}
