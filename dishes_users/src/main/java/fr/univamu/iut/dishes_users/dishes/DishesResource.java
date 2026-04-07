package fr.univamu.iut.dishes_users.dishes;

import fr.univamu.iut.dishes_users.DishesUsersRepositoryInterface;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/dishes")
@ApplicationScoped
public class DishesResource {

    private DishesServices dishesServices;

    public DishesResource(){}

    public @Inject DishesResource(DishesUsersRepositoryInterface dishRepo){this.dishesServices = new DishesServices(dishRepo);}

    public DishesResource(DishesServices dishesServices){this.dishesServices = dishesServices;}

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

    @GET
    @Produces("application/json")
    public String getAllDishes(){
        return dishesServices.getAllDishesJSON();
    }

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
}