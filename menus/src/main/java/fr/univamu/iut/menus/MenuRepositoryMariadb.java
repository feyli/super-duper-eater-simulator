package fr.univamu.iut.menus;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant d'accèder aux menus stockés dans une base de données Mariadb
 */
public class MenuRepositoryMariadb implements MenuRepositoryInterface, Closeable {

    /**
     * Accès à la base de données (session)
     */
    protected Connection dbConnection;

    /**
     * Constructeur de la classe
     *
     * @param infoConnection chaîne de caractères avec les informations de connexion
     *                       (p.ex. jdbc:mariadb://mysql-[compte].alwaysdata.net/[compte]_library_db
     * @param user           chaîne de caractères contenant l'identifiant de connexion à la base de données
     * @param pwd            chaîne de caractères contenant le mot de passe à utiliser
     */
    public MenuRepositoryMariadb(String infoConnection, String user, String pwd) throws java.sql.SQLException, java.lang.ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    @Override
    public void close() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Menu getMenu(Integer id) {
        String query = "SELECT id, name, creatorId, creationDate, updateDate FROM menus WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                String name = result.getString("name");
                Integer creatorId = result.getInt("creatorId");
                Date creationDate = result.getDate("creationDate");
                Date updateDate = result.getDate("updateDate");
                ArrayList<Menu.DishSummary> dishes = getMenuDishes(id);

                Menu selectedMenu = new Menu(id, name, creatorId, creationDate, updateDate, dishes, null);
                selectedMenu.recomputeTotalPrice();
                return selectedMenu;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Menu> getAllMenus() {
        ArrayList<Menu> listMenus = new ArrayList<>();

        String query = "SELECT id, name, creatorId, creationDate, updateDate FROM menus ORDER BY id";

        try (PreparedStatement ps = dbConnection.prepareStatement(query);
             ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                Integer creatorId = result.getInt("creatorId");
                Date creationDate = result.getDate("creationDate");
                Date updateDate = result.getDate("updateDate");
                ArrayList<Menu.DishSummary> dishes = getMenuDishes(id);

                Menu currentMenu = new Menu(id, name, creatorId, creationDate, updateDate, dishes, null);
                currentMenu.recomputeTotalPrice();
                listMenus.add(currentMenu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listMenus;
    }

    @Override
    public boolean updateMenu(Integer id, String name, Integer creatorId, java.sql.Date updateDate, List<Menu.DishSummary> dishes) {
        String updateMenuQuery = "UPDATE menus SET name=?, creatorId=?, updateDate=? WHERE id=?";
        String deleteMenuDishesQuery = "DELETE FROM menu_dish WHERE menuId=?";
        String insertMenuDishQuery = "INSERT INTO menu_dish (menuId, dishId) VALUES (?, ?)";

        try {
            boolean previousAutoCommit = dbConnection.getAutoCommit();
            dbConnection.setAutoCommit(false);

            try (PreparedStatement updateStatement = dbConnection.prepareStatement(updateMenuQuery);
                 PreparedStatement deleteStatement = dbConnection.prepareStatement(deleteMenuDishesQuery);
                 PreparedStatement insertStatement = dbConnection.prepareStatement(insertMenuDishQuery)) {

                updateStatement.setString(1, name);
                updateStatement.setInt(2, creatorId);
                updateStatement.setDate(3, updateDate);
                updateStatement.setInt(4, id);

                int nbRowModified = updateStatement.executeUpdate();
                if (nbRowModified == 0) {
                    dbConnection.rollback();
                    return false;
                }

                deleteStatement.setInt(1, id);
                deleteStatement.executeUpdate();

                if (dishes != null) {
                    for (Menu.DishSummary dish : dishes) {
                        Integer dishId = extractDishId(dish);
                        if (dishId == null) {
                            continue;
                        }
                        insertStatement.setInt(1, id);
                        insertStatement.setInt(2, dishId);
                        insertStatement.executeUpdate();
                    }
                }

                dbConnection.commit();
                return true;
            } catch (SQLException e) {
                dbConnection.rollback();
                throw e;
            } finally {
                dbConnection.setAutoCommit(previousAutoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Récupère les plats associés à un menu.
     */
    protected ArrayList<Menu.DishSummary> getMenuDishes(Integer menuId) throws SQLException {
        ArrayList<Menu.DishSummary> dishes = new ArrayList<>();
        String query = "SELECT dishId FROM menu_dish WHERE menuId=? ORDER BY dishId";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, menuId);
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    dishes.add(new Menu.DishSummary(result.getInt("dishId"), null, null));
                }
            }
        }

        return dishes;
    }

    /**
     * Extrait l'identifiant d'un plat quel que soit le format JSON reçu.
     */
    protected Integer extractDishId(Menu.DishSummary dish) {
        if (dish == null) {
            return null;
        }
        return dish.getId();
    }
}
