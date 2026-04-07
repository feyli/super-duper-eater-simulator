package fr.univamu.iut.orders.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete order with all its items.
 * Combines data from the database with calculated/fetched fields.
 */
public class Order {
    private int id;
    private int subscriberId;
    private LocalDateTime orderDate;
    private String deliveryAddress;
    private LocalDate deliveryDate;
    private List<OrderItem> items;
    private double totalPrice;  // Calculated from items
    
    public Order() {
        this.items = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getSubscriberId() {
        return subscriberId;
    }
    
    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
        calculateTotalPrice();
    }
    
    public void addItem(OrderItem item) {
        this.items.add(item);
        calculateTotalPrice();
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    /**
     * Calculates total price from all order items.
     */
    private void calculateTotalPrice() {
        this.totalPrice = items.stream()
                .mapToDouble(OrderItem::getLinePrice)
                .sum();
    }
}
