package fr.univamu.iut.dishes_users;

import fr.univamu.iut.dishes_users.dishes.Dishes;
import fr.univamu.iut.dishes_users.users.Users;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishesUsersRepositoryMariadb implements DishesUsersRepositoryInterface, Closeable {

    protected Connection dbConnection;

    public DishesUsersRepositoryMariadb(String connectionInfo, String user, String pwd) throws java.sql.SQLException, java.lang.ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionInfo, user, pwd);
    }

    @Override
    public void close() {
        try{
            dbConnection.close();
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Dishes getDish(int id) {
        Dishes selectedDish = null;
        String query = "SELECT * FROM dishes WHERE id=?";
        try(PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                String name = result.getString("name");
                String description = result.getString("description");
                float price = result.getFloat("price");
                selectedDish = new Dishes(id, name, description, price);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedDish;
    }

    @Override
    public List<Dishes> getAllDishes() {
        ArrayList<Dishes> selectedDishes;
        String query = "SELECT * FROM dishes";
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ResultSet result = ps.executeQuery();
            selectedDishes = new ArrayList<>();
            while(result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                float price = result.getFloat("price");
                Dishes selectedDish = new Dishes(id, name, description, price);
                selectedDishes.add(selectedDish);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedDishes;
    }

    @Override
    public boolean updateDish(int id, String name, String description, float price) {
        String query = "UPDATE dishes SET name=?, description=?, price=? where id=?";
        int nbRowModified = 0;
        try (PreparedStatement ps = dbConnection.prepareStatement(query)){
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.setFloat(4, price);
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return (nbRowModified != 0);
    }

    @Override
    public Users getUser(int id) {
        Users selectedUser = null;
        String query = "SELECT * FROM users WHERE id=?";
        try(PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                String lastName = result.getString("lastName");
                String firstName = result.getString("firstName");
                String email = result.getString("email");
                String address = result.getString("address");
                selectedUser = new Users(id, lastName, firstName, email, address);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedUser;
    }

    @Override
    public List<Users> getAllUsers() {
        ArrayList<Users> selectedUsers;
        String query = "SELECT * FROM users";
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ResultSet result = ps.executeQuery();
            selectedUsers = new ArrayList<>();
            while(result.next()) {
                int id = result.getInt("id");
                String lastName = result.getString("lastName");
                String firstName = result.getString("firstName");
                String email = result.getString("email");
                String address = result.getString("address");
                Users selectedUser = new Users(id, lastName,  firstName, email, address);
                selectedUsers.add(selectedUser);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedUsers;
    }

    @Override
    public boolean updateUser(int id, String lastName, String firstName, String email, String address) {
        String query = "UPDATE dishes SET lastName=?, firstName=?, email=?, address=? where id=?";
        int nbRowModified = 0;
        try (PreparedStatement ps = dbConnection.prepareStatement(query)){
            ps.setInt(1, id);
            ps.setString(2, lastName);
            ps.setString(3, firstName);
            ps.setString(4, email);
            ps.setString(5, address);
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return (nbRowModified != 0);
    }
}