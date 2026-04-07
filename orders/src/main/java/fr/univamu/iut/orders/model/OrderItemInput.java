package fr.univamu.iut.orders.model;

/**
 * DTO for order item input when creating an order.
 * Only includes the fields provided by the client.
 */
public class OrderItemInput {
    private int menuId;
    private int quantity;
    
    public OrderItemInput() {
    }
    
    public int getMenuId() {
        return menuId;
    }
    
    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
