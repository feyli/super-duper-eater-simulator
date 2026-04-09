package fr.univamu.iut.dishes_users.repository;

import fr.univamu.iut.dishes_users.model.Dishes;
import fr.univamu.iut.dishes_users.model.Users;
import java.util.*;

/**
 * Interface for dishes and users repository operations
 */
public interface DishesUsersRepositoryInterface {

    /**
     * Closes the repository connection
     */
    public void close();

    /**
     * Gets a dish by its ID
     * @param id dish ID
     * @return Dishes object or null if not found
     */
    public Dishes getDish(int id);

    /**
     * Gets all dishes
     * @return List of all dishes
     */
    public List<Dishes> getAllDishes();

    /**
     * Creates a new dish
     * @param id dish ID
     * @param name dish name
     * @param description dish description
     * @param price dish price
     * @return true if created successfully, false otherwise
     */
    public boolean createDish(int id, String name, String description, float price);

    /**
     * Updates an existing dish
     * @param id dish ID
     * @param name dish name
     * @param description dish description
     * @param price dish price
     * @return true if updated successfully, false otherwise
     */
    public boolean updateDish(int id, String name, String description, float price);

    /**
     * Deletes a dish by its ID
     * @param id dish ID
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDish(int id);

    /**
     * Gets a user by its ID
     * @param id user ID
     * @return Users object or null if not found
     */
    public Users getUser(int id);

    /**
     * Gets all users
     * @return List of all users
     */
    public List<Users> getAllUsers();

    /**
     * Creates a new user
     * @param id user ID
     * @param lastName user last name
     * @param firstName user first name
     * @param email user email
     * @param address user address
     * @return true if created successfully, false otherwise
     */
    public boolean createUser(int id, String lastName, String firstName, String email, String address);

    /**
     * Updates an existing user
     * @param id user ID
     * @param lastName user last name
     * @param firstName user first name
     * @param email user email
     * @param address user address
     * @return true if updated successfully, false otherwise
     */
    public boolean updateUser(int id, String lastName, String firstName, String email, String address);

    /**
     * Deletes a user by its ID
     * @param id user ID
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteUser(int id);
}