package fr.univamu.iut.orders.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a menu retrieved from the Menus API.
 * Used for validating menu existence and fetching price information.
 */
public class Menu {
    private int id;
    private String name;
    private int creatorId;
    private String creatorName;
    private LocalDate creationDate;
    private LocalDate updateDate;
    private List<DishSummary> dishes;
    private double totalPrice;
    
    public Menu() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public LocalDate getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    
    public LocalDate getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
    
    public List<DishSummary> getDishes() {
        return dishes;
    }
    
    public void setDishes(List<DishSummary> dishes) {
        this.dishes = dishes;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    /**
     * Nested class for dish summary within a menu.
     */
    public static class DishSummary {
        private int id;
        private String name;
        private double price;
        
        public DishSummary() {
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public double getPrice() {
            return price;
        }
        
        public void setPrice(double price) {
            this.price = price;
        }
    }
}
