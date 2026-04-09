package fr.univamu.iut.dishes_users.repository;

import fr.univamu.iut.dishes_users.model.Dishes;
import fr.univamu.iut.dishes_users.model.Users;
import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MariaDB implementation of the dishes and users repository
 */
public class DishesUsersRepositoryMariadb implements DishesUsersRepositoryInterface, Closeable {

    /**
     * Database connection
     */
    protected Connection dbConnection;

    /**
     * Constructor for MariaDB repository
     * @param connectionInfo database connection string
     * @param user database username
     * @param pwd database password
     * @throws SQLException if connection fails
     * @throws ClassNotFoundException if MariaDB driver not found
     */
    public DishesUsersRepositoryMariadb(String connectionInfo, String user, String pwd) throws java.sql.SQLException, java.lang.ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionInfo, user, pwd);
    }

    /**
     * Closes the database connection
     */
    @Override
    public void close() {
        try{
            dbConnection.close();
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Gets a dish by its ID
     * @param id dish ID
     * @return Dishes object or null if not found
     */
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

    /**
     * Gets all dishes
     * @return List of all dishes
     */
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

    /**
     * Creates a new dish
     * @param id dish ID
     * @param name dish name
     * @param description dish description
     * @param price dish price
     * @return true if created successfully, false otherwise
     */
    @Override
    public boolean createDish(int id, String name, String description, float price) {
        String query = "INSERT INTO dishes (id, name, description, price) VALUES (?, ?, ?, ?)";
        boolean created = false;
       try(PreparedStatement ps = dbConnection.prepareStatement(query)) {
           ps.setInt(1, id);
           ps.setString(2, name);
           ps.setString(3, description);
           ps.setFloat(4, price);
           created  = ps.execute();
       } catch(SQLException e) {
           throw new RuntimeException(e);
       }
       return created;
    }

    /**
     * Updates an existing dish
     * @param id dish ID
     * @param name dish name
     * @param description dish description
     * @param price dish price
     * @return true if updated successfully, false otherwise
     */
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

    /**
     * Deletes a dish by its ID
     * @param id dish ID
     * @return true if deleted successfully, false otherwise
     */
    @Override
    public boolean deleteDish(int id) {
        String query = "DELETE FROM dishes WHERE id=?";
        boolean deleted = false;
        try(PreparedStatement ps = dbConnection.prepareStatement(query)){
            ps.setInt(1, id);
            deleted = ps.execute();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return deleted;
    }

    /**
     * Gets a user by its ID
     * @param id user ID
     * @return Users object or null if not found
     */
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

    /**
     * Gets all users
     * @return List of all users
     */
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

    /**
     * Creates a new user
     * @param id user ID
     * @param lastName user last name
     * @param firstName user first name
     * @param email user email
     * @param address user address
     * @return true if created successfully, false otherwise
     */
    @Override
    public boolean createUser(int id, String lastName, String firstName, String email, String address) {
        String query = "INSERT INTO dishes (id, lastName, firstName, email, address) VALUES (?, ?, ?, ?, ?)";
        boolean created = false;
        try(PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.setString(2, lastName);
            ps.setString(3, firstName);
            ps.setString(4, email);
            ps.setString(5, address);
            created  = ps.execute();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return created;
    }

    /**
     * Updates an existing user
     * @param id user ID
     * @param lastName user last name
     * @param firstName user first name
     * @param email user email
     * @param address user address
     * @return true if updated successfully, false otherwise
     */
    @Override
    public boolean updateUser(int id, String lastName, String firstName, String email, String address) {
        String query = "UPDATE users SET lastName=?, firstName=?, email=?, address=? where id=?";
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

    /**
     * Deletes a user by its ID
     * @param id user ID
     * @return true if deleted successfully, false otherwise
     */
    @Override
    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id=?";
        boolean deleted = false;
        try(PreparedStatement ps = dbConnection.prepareStatement(query)){
            ps.setInt(1, id);
            deleted = ps.execute();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return deleted;
    }
}