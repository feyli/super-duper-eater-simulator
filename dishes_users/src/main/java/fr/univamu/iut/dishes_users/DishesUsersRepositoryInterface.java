package fr.univamu.iut.dishes_users;

import fr.univamu.iut.dishes_users.dishes.Dishes;
import fr.univamu.iut.dishes_users.users.Users;

import java.util.*;

public interface DishesUsersRepositoryInterface {

    public void close();

    public Dishes getDish(int id);

    public List<Dishes> getAllDishes();

    public boolean updateDish(int id, String name, String description, float price);

    public Users getUser(int id);

    public List<Users> getAllUsers();

    public boolean updateUser(int id, String lastName, String firstName, String email, String address);
}