package fr.univamu.iut.dishes_users.resource;

import fr.univamu.iut.dishes_users.repository.DishesUsersRepositoryInterface;
import fr.univamu.iut.dishes_users.model.Users;
import fr.univamu.iut.dishes_users.service.UsersServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for user operations
 */
@Path("/users")
@ApplicationScoped
public class UsersResource {

    /**
     * Services for user operations
     */
    private UsersServices usersServices;

    /**
     * Default constructor
     */
    public UsersResource(){}

    /**
     * Constructor with dependency injection
     * @param userRepo repository interface for users
     */
    @Inject
    public UsersResource(DishesUsersRepositoryInterface userRepo){
        this.usersServices = new UsersServices(userRepo);
    }

    /**
     * Alternative constructor
     * @param usersServices users services instance
     */
    public UsersResource(UsersServices usersServices){this.usersServices = usersServices;}

    /**
     * Gets a user by its ID
     * @param id user ID
     * @return JSON response with the user or 404 if not found
     */
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

    /**
     * Gets all users
     * @return JSON response with all users
     */
    @GET
    @Produces("application/json")
    public String getAllUsers(){
        return usersServices.getAllUsersJSON();
    }

    /**
     * Creates a new user
     * @param id user ID
     * @param user user object from request body
     * @return Response indicating success or failure
     */
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

    /**
     * Updates an existing user
     * @param id user ID
     * @param user user object from request body
     * @return Response indicating success or failure
     */
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

    /**
     * Deletes a user by its ID
     * @param id user ID
     * @return Response indicating success or failure
     */
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