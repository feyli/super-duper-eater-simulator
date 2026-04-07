package fr.univamu.iut.orders.model;

/**
 * Represents an item in an order.
 * Includes both database fields (menuId, quantity) and calculated/fetched fields
 * (menuName, unitPrice, linePrice) retrieved from Menus API.
 */
public class OrderItem {
    private int menuId;
    private String menuName;  // Fetched from Menus API
    private int quantity;
    private double unitPrice;  // Fetched from Menus API
    private double linePrice;  // Calculated: unitPrice * quantity
    
    public OrderItem() {
    }
    
    public OrderItem(int menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
    
    public int getMenuId() {
        return menuId;
    }
    
    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.linePrice = unitPrice * quantity;
    }
    
    public double getLinePrice() {
        return linePrice;
    }
    
    public void setLinePrice(double linePrice) {
        this.linePrice = linePrice;
    }
}
