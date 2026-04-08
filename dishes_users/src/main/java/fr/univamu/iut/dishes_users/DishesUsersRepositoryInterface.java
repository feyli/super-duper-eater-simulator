package fr.univamu.iut.dishes_users;

import fr.univamu.iut.dishes_users.dishes.Dishes;
import fr.univamu.iut.dishes_users.users.Users;

import java.util.*;

public interface DishesUsersRepositoryInterface {

    public void close();

    public Dishes getDish(int id);

    public List<Dishes> getAllDishes();

    public boolean createDish(int id, String name, String description, float price);

    public boolean updateDish(int id, String name, String description, float price);

    public boolean deleteDish(int id);

    public Users getUser(int id);

    public List<Users> getAllUsers();

    public boolean createUser(int id, String lastName, String firstName, String email, String address);

    public boolean updateUser(int id, String lastName, String firstName, String email, String address);

    public boolean deleteUser(int id);
}