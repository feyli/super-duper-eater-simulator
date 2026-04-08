package fr.univamu.iut.dishes_users.dishes;

import fr.univamu.iut.dishes_users.DishesUsersRepositoryInterface;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.util.List;

public class DishesServices {

    protected DishesUsersRepositoryInterface dishRepo;

    public DishesServices(DishesUsersRepositoryInterface dishRepo) {
        this.dishRepo = dishRepo;
    }

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

    public boolean createDish(int id, Dishes dish){
        return dishRepo.createDish(id, dish.getName(), dish.getDescription(), dish.getPrice());
    }

    public boolean updateDish(int id, Dishes dish){
        return dishRepo.updateDish(id, dish.getName(), dish.getDescription(), dish.getPrice());
    }

    public boolean deleteDish(int id){
        return dishRepo.deleteDish(id);
    }
}
