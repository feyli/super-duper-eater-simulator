package fr.univamu.iut.dishes_users.resource;

import fr.univamu.iut.dishes_users.service.DishesServices;
import fr.univamu.iut.dishes_users.repository.DishesUsersRepositoryInterface;
import fr.univamu.iut.dishes_users.model.Dishes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for dish operations
 */
@Path("/dishes")
@ApplicationScoped
public class DishesResource {

    /**
     * Services for dish operations
     */
    private DishesServices dishesServices;

    /**
     * Default constructor
     */
    public DishesResource(){}

    /**
     * Constructor with dependency injection
     * @param dishRepo repository interface for dishes
     */
    @Inject
    public DishesResource(DishesUsersRepositoryInterface dishRepo){
        this.dishesServices = new DishesServices(dishRepo);
    }

    /**
     * Alternative constructor
     * @param dishesServices dishes services instance
     */
    public DishesResource(DishesServices dishesServices){this.dishesServices = dishesServices;}

    /**
     * Gets a dish by its ID
     * @param id dish ID
     * @return JSON response with the dish or 404 if not found
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getDish(@PathParam("id") int id){
        String result = dishesServices.getDishJSON(id);
        if(result == null){
            throw new NotFoundException();
        }
        return result;
    }

    /**
     * Gets all dishes
     * @return JSON response with all dishes
     */
    @GET
    @Produces("application/json")
    public String getAllDishes(){
        return dishesServices.getAllDishesJSON();
    }

    /**
     * Creates a new dish
     * @param id dish ID
     * @param dish dish object from request body
     * @return Response indicating success or failure
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response createDish(@PathParam("id") int id, Dishes dish){
        if(!dishesServices.createDish(id, dish)){
            throw new NotFoundException();
        } else {
            return Response.ok("created").build();
        }
    }

    /**
     * Updates an existing dish
     * @param id dish ID
     * @param dish dish object from request body
     * @return Response indicating success or failure
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response updateDish(@PathParam("id") int id, Dishes dish){
        if(!dishesServices.updateDish(id, dish)){
            throw new NotFoundException();
        } else {
            return Response.ok("updated").build();
        }
    }

    /**
     * Deletes a dish by its ID
     * @param id dish ID
     * @return Response indicating success or failure
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response deleteDish(@PathParam("id") int id){
        if(!dishesServices.deleteDish(id)){
            throw new NotFoundException();
        } else {
            return Response.ok("deleted").build();
        }
    }
}