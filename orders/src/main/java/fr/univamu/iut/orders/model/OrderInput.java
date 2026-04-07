package fr.univamu.iut.orders.model;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating a new order.
 * Matches the OrderInput schema from OpenAPI spec.
 */
public class OrderInput {
    private int subscriberId;
    private String deliveryAddress;
    private LocalDate deliveryDate;
    private List<OrderItemInput> items;
    
    public OrderInput() {
    }
    
    public int getSubscriberId() {
        return subscriberId;
    }
    
    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
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
    
    public List<OrderItemInput> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemInput> items) {
        this.items = items;
    }
}
