package fr.univamu.iut.dishes_users.users;

import fr.univamu.iut.dishes_users.DishesUsersRepositoryInterface;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.util.List;

public class UsersServices {

    protected DishesUsersRepositoryInterface userRepo;

    public UsersServices(DishesUsersRepositoryInterface userRepo) {
        this.userRepo = userRepo;
    }

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

    public boolean updateUser(int id, Users user){
        return userRepo.updateUser(id, user.getLastName(), user.getFirstName(), user.getEmail(), user.getAddress());
    }
}
