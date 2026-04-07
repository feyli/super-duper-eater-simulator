package fr.univamu.iut.orders.model;

import java.time.LocalDate;

/**
 * DTO for updating an existing order.
 * Only delivery address and date can be modified.
 * Matches the OrderUpdateInput schema from OpenAPI spec.
 */
public class OrderUpdateInput {
    private String deliveryAddress;
    private LocalDate deliveryDate;
    
    public OrderUpdateInput() {
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
}
