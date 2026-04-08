package fr.univamu.iut.menus;

import java.sql.Date;
import java.util.List;

/**
 * Interface d'accès aux données des menus
 */
public interface MenuRepositoryInterface {

    /**
     *  Méthode fermant le dépôt où sont stockées les informations sur les menus
     */
    void close();

    /**
     * Méthode retournant le menu dont l'identifiant est passée en paramètre
     * @param id identifiant du menu recherché
     * @return un objet Menu représentant le menu recherché
     */
    Menu getMenu(Integer id);

    /**
     * Méthode retournant la liste des menus
     * @return une liste d'objets menus
     */
    List<Menu> getAllMenus();

    /**
     * Méthode permettant de mettre à jours un menu enregistré
     * @param id identifiant du menu à mettre à jours
     * @param name nouveau titre du menu
     * @param creatorId nouveau créateur du menu
     * @param updateDate nouvelle date de mise à jour du menu
     * @param  dishes nouveaux plats du menu
     * @return true si le menu existe et la mise à jours a été faite, false sinon
     */
    boolean updateMenu(Integer id, String name, Integer creatorId, Date updateDate, List<Menu.DishSummary> dishes);
}
