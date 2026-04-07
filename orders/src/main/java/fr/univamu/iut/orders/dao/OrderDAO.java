package fr.univamu.iut.orders.dao;

import fr.univamu.iut.orders.database.DatabaseManager;
import fr.univamu.iut.orders.model.*;
import fr.univamu.iut.orders.service.MenusAPIClient;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Order entity.
 * Handles all database operations for orders and order items.
 */
public class OrderDAO {
    
    private final DatabaseManager dbManager;
    private final MenusAPIClient menusAPIClient;
    
    public OrderDAO() {
        this.dbManager = DatabaseManager.getInstance();
        this.menusAPIClient = new MenusAPIClient();
    }
    
    /**
     * Retrieves all orders, optionally filtered by subscriber ID.
     * Enriches order items with menu data from Menus API.
     * 
     * @param subscriberId optional subscriber ID filter (null for all orders)
     * @return list of orders
     * @throws SQLException if database error occurs
     */
    public List<Order> getAllOrders(Integer subscriberId) throws SQLException {
        String sql = subscriberId == null
                ? "SELECT * FROM `order` ORDER BY order_date DESC"
                : "SELECT * FROM `order` WHERE subscriber_id = ? ORDER BY order_date DESC";
        
        Connection conn = dbManager.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (subscriberId != null) {
                stmt.setInt(1, subscriberId);
            }
            
            ResultSet rs = stmt.executeQuery();
            List<Order> orders = new ArrayList<>();
            
            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                loadOrderItems(conn, order);
                orders.add(order);
            }
            
            return orders;
        } finally {
            dbManager.closeConnection(conn);
        }
    }
    
    /**
     * Retrieves a single order by ID.
     * 
     * @param orderId the order ID
     * @return the Order object, or null if not found
     * @throws SQLException if database error occurs
     */
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        
        Connection conn = dbManager.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                loadOrderItems(conn, order);
                return order;
            }
            
            return null;
        } finally {
            dbManager.closeConnection(conn);
        }
    }
    
    /**
     * Creates a new order with its items.
     * Validates menus via Menus API before insertion.
     * Uses transactions to ensure atomicity.
     * 
     * @param input the order input data
     * @return the created Order with generated ID
     * @throws SQLException if database error occurs
     * @throws IOException if Menus API call fails
     * @throws MenusAPIClient.MenuNotFoundException if a menu doesn't exist
     */
    public Order createOrder(OrderInput input) throws SQLException, IOException, MenusAPIClient.MenuNotFoundException {
        // Validate all menus exist and fetch their data
        Map<Integer, Menu> menuData = new HashMap<>();
        for (OrderItemInput item : input.getItems()) {
            Menu menu = menusAPIClient.getMenuById(item.getMenuId());
            menuData.put(item.getMenuId(), menu);
        }
        
        Connection conn = dbManager.getConnection();
        try {
            conn.setAutoCommit(false);  // Start transaction
            
            // Insert order
            String orderSql = "INSERT INTO `order` (subscriber_id, delivery_address, delivery_date) VALUES (?, ?, ?)";
            PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, input.getSubscriberId());
            orderStmt.setString(2, input.getDeliveryAddress());
            orderStmt.setDate(3, Date.valueOf(input.getDeliveryDate()));
            
            orderStmt.executeUpdate();
            
            // Get generated order ID
            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Failed to get generated order ID");
            }
            int orderId = generatedKeys.getInt(1);
            
            // Insert order items
            String itemSql = "INSERT INTO order_menu (order_id, menu_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(itemSql);
            
            for (OrderItemInput item : input.getItems()) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getMenuId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.executeUpdate();
            }
            
            conn.commit();  // Commit transaction
            
            // Fetch and return the created order
            return getOrderById(orderId);
            
        } catch (Exception e) {
            conn.rollback();  // Rollback on error
            throw e;
        } finally {
            conn.setAutoCommit(true);
            dbManager.closeConnection(conn);
        }
    }
    
    /**
     * Updates an order's delivery information.
     * Only delivery address and date can be modified.
     * 
     * @param orderId the order ID to update
     * @param input the update data
     * @return the updated Order, or null if not found
     * @throws SQLException if database error occurs
     */
    public Order updateOrder(int orderId, OrderUpdateInput input) throws SQLException {
        String sql = "UPDATE `order` SET delivery_address = ?, delivery_date = ? WHERE id = ?";
        
        Connection conn = dbManager.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, input.getDeliveryAddress());
            stmt.setDate(2, Date.valueOf(input.getDeliveryDate()));
            stmt.setInt(3, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                return null;  // Order not found
            }
            
            return getOrderById(orderId);
        } finally {
            dbManager.closeConnection(conn);
        }
    }
    
    /**
     * Deletes an order and all its items.
     * Items are deleted automatically via foreign key cascade.
     * 
     * @param orderId the order ID to delete
     * @return true if deleted, false if not found
     * @throws SQLException if database error occurs
     */
    public boolean deleteOrder(int orderId) throws SQLException {
        String sql = "DELETE FROM `order` WHERE id = ?";
        
        Connection conn = dbManager.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            dbManager.closeConnection(conn);
        }
    }
    
    /**
     * Extracts an Order object from a ResultSet (without items).
     */
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setSubscriberId(rs.getInt("subscriber_id"));
        
        Timestamp orderDateTimestamp = rs.getTimestamp("order_date");
        order.setOrderDate(orderDateTimestamp.toLocalDateTime());
        
        order.setDeliveryAddress(rs.getString("delivery_address"));
        
        Date deliveryDateSql = rs.getDate("delivery_date");
        order.setDeliveryDate(deliveryDateSql.toLocalDate());
        
        return order;
    }
    
    /**
     * Loads order items for an order and enriches them with menu data from Menus API.
     * Updates the order's items list and total price.
     */
    private void loadOrderItems(Connection conn, Order order) throws SQLException {
        String sql = "SELECT menu_id, quantity FROM order_menu WHERE order_id = ?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, order.getId());
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int menuId = rs.getInt("menu_id");
            int quantity = rs.getInt("quantity");
            
            OrderItem item = new OrderItem(menuId, quantity);
            
            // Fetch menu data from Menus API
            try {
                Menu menu = menusAPIClient.getMenuById(menuId);
                item.setMenuName(menu.getName());
                item.setUnitPrice(menu.getTotalPrice());
                // linePrice is calculated automatically when unitPrice is set
            } catch (IOException | MenusAPIClient.MenuNotFoundException e) {
                // If menu API fails, set default values
                item.setMenuName("Unknown Menu");
                item.setUnitPrice(0.0);
                System.err.println("Failed to fetch menu " + menuId + ": " + e.getMessage());
            }
            
            order.addItem(item);
        }
    }
}
