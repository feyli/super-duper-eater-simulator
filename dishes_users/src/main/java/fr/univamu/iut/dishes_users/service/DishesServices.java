package fr.univamu.iut.dishes_users.service;

import fr.univamu.iut.dishes_users.repository.DishesUsersRepositoryInterface;
import fr.univamu.iut.dishes_users.model.Dishes;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.List;

/**
 * Service class for dish operations
 */
public class DishesServices {

    /**
     * Repository for dish data access
     */
    protected DishesUsersRepositoryInterface dishRepo;

    /**
     * Constructor for DishesServices
     * @param dishRepo repository interface for dishes
     */
    public DishesServices(DishesUsersRepositoryInterface dishRepo) {
        this.dishRepo = dishRepo;
    }

    /**
     * Gets a dish in JSON format by its ID
     * @param id dish ID
     * @return JSON string of the dish or null if not found
     */
    public String getDishJSON(int id){
        String result = null;
        Dishes myDish = dishRepo.getDish(id);
        if(myDish != null){
            try(Jsonb jsonb = JsonbBuilder.create()) {
                result = jsonb.toJson(myDish);
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    /**
     * Gets all dishes in JSON format
     * @return JSON string of all dishes
     */
    public String getAllDishesJSON(){
        List<Dishes> allDishes = dishRepo.getAllDishes();
        String result = null;
        try(Jsonb jsonb = JsonbBuilder.create()) {
            result = jsonb.toJson(allDishes);
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        return result;
    }

    /**
     * Creates a new dish
     * @param id dish ID
     * @param dish dish object
     * @return true if created successfully, false otherwise
     */
    public boolean createDish(int id, Dishes dish){
        return dishRepo.createDish(id, dish.getName(), dish.getDescription(), dish.getPrice());
    }

    /**
     * Updates an existing dish
     * @param id dish ID
     * @param dish dish object
     * @return true if updated successfully, false otherwise
     */
    public boolean updateDish(int id, Dishes dish){
        return dishRepo.updateDish(id, dish.getName(), dish.getDescription(), dish.getPrice());
    }

    /**
     * Deletes a dish by its ID
     * @param id dish ID
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDish(int id){
        return dishRepo.deleteDish(id);
    }
}
