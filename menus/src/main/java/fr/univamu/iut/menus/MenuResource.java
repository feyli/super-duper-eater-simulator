package fr.univamu.iut.menus;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.Objects;

/**
 * Ressource associée aux menus
 * (point d'accès de l'API REST)
 */
@Path("/menus")
@ApplicationScoped
public class MenuResource {

    /**
     * Service utilisé pour accéder aux données des menus et récupérer/modifier leurs informations
     */
    private MenuService service;

    @Inject
    private MenuRepositoryInterface menuRepo;

    public MenuResource() {
    }

    @PostConstruct
    void init() {
        if (this.service == null) {
            this.service = new MenuService(Objects.requireNonNull(menuRepo, "MenuRepositoryInterface injection is null"));
        }
    }

    /**
     * Constructeur permettant d'initialiser le service d'accès aux menus
     */
    public MenuResource( MenuService service ){
        this.service = service;
    }

    /**
     * Enpoint permettant de publier de tous les menus enregistrés
     * @return la liste des menus (avec leurs informations) au format JSON
     */
    @GET
    @Produces("application/json")
    public String getAllMenus() {
        return service.getAllMenusJSON();
    }

    /**
     * Endpoint permettant de publier les informations d'un livre dont la référence est passée paramètre dans le chemin
     * @param id identifiant du menu recherché
     * @return les informations du menu recherché au format JSON
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getMenu( @PathParam("id") Integer id){

        String result = service.getMenuJSON(id);

        // si le menu n'a pas été trouvé
        if( result == null )
            throw new NotFoundException();

        return result;
    }

    /**
     * Endpoint permettant de mettre à jours le statut d'un menu uniquement
     * @param id la identifiant du menu dont il faut changer les informations
     * @param menu le menu transmis en HTTP au format JSON et convertit en objet Menu
     * @return une réponse "updated" si la mise à jour a été effectuée, une erreur NotFound sinon
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response updateMenu(@PathParam("id") Integer id, Menu menu ){

        // si le menu n'a pas été trouvé
        if( ! service.updateMenu(id, menu) )
            throw new NotFoundException();
        else
            return Response.ok("updated").build();
    }
}