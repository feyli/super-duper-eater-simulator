package fr.univamu.iut.dishes_users.service;

import fr.univamu.iut.dishes_users.repository.DishesUsersRepositoryInterface;
import fr.univamu.iut.dishes_users.model.Users;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.List;

/**
 * Service class for user operations
 */
public class UsersServices {

    /**
     * Repository for user data access
     */
    protected DishesUsersRepositoryInterface userRepo;

    /**
     * Constructor for UsersServices
     * @param userRepo repository interface for users
     */
    public UsersServices(DishesUsersRepositoryInterface userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Gets a user in JSON format by its ID
     * @param id user ID
     * @return JSON string of the user or null if not found
     */
    public String getUserJSON(int id){
        String result = null;
        Users myUser = userRepo.getUser(id);
        if(myUser != null){
            try(Jsonb jsonb = JsonbBuilder.create()) {
                result = jsonb.toJson(myUser);
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    /**
     * Gets all users in JSON format
     * @return JSON string of all users
     */
    public String getAllUsersJSON(){
        List<Users> allUsers = userRepo.getAllUsers();
        String result = null;
        try(Jsonb jsonb = JsonbBuilder.create()) {
            result = jsonb.toJson(allUsers);
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        return result;
    }

    /**
     * Creates a new user
     * @param id user ID
     * @param user user object
     * @return true if created successfully, false otherwise
     */
    public boolean createUser(int id, Users user){
        return userRepo.createUser(id, user.getLastName(), user.getFirstName(), user.getEmail(), user.getAddress());
    }

    /**
     * Updates an existing user
     * @param id user ID
     * @param user user object
     * @return true if updated successfully, false otherwise
     */
    public boolean updateUser(int id, Users user){
        return userRepo.updateUser(id, user.getLastName(), user.getFirstName(), user.getEmail(), user.getAddress());
    }

    /**
     * Deletes a user by its ID
     * @param id user ID
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteUser(int id){
        return userRepo.deleteUser(id);
    }
}
