package fr.univamu.iut.dishes_users.users;

import fr.univamu.iut.dishes_users.DishesUsersRepositoryInterface;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/users")
@ApplicationScoped
public class UsersResource {

    private UsersServices usersServices;

    public UsersResource(){}

    public @Inject UsersResource(DishesUsersRepositoryInterface userRepo){this.usersServices = new UsersServices(userRepo);}

    public UsersResource(UsersServices usersServices){this.usersServices = usersServices;}

    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getUser(@PathParam("id") int id){
        String result = usersServices.getUserJSON(id);
        if(result == null){
            throw new NotFoundException();
        }
        return result;
    }

    @GET
    @Produces("application/json")
    public String getAllUsers(){
        return usersServices.getAllUsersJSON();
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response createUser(@PathParam("id") int id, Users user){
        if(!usersServices.createUser(id, user)){
            throw new NotFoundException();
        } else {
            return Response.ok("created").build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response updateUser(@PathParam("id") int id, Users user){
        if(!usersServices.updateUser(id, user)){
            throw new NotFoundException();
        } else {
            return Response.ok("updated").build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response deleteUser(@PathParam("id") int id){
        if(!usersServices.deleteUser(id)){
            throw new NotFoundException();
        } else {
            return Response.ok("deleted").build();
        }
    }
}