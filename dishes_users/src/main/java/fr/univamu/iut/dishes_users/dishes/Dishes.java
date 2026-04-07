package fr.univamu.iut.dishes_users.dishes;
/**
 * Class representing a dish
 */
public class Dishes {
    /**
     * Dish id
     */
    protected int id;
    /**
     * Dish name
     */
    protected String name;
    /**
     * Dish description
     */
    protected String description;
    /**
     * Dish price
     */
    protected float price;
    /**
     * Default constructor
     */
    public Dishes() {

    }
    /**
     * Dish constructor
     * @param id dish id
     * @param name dish name
     * @param description dish description
     * @param price dish price
     */
    public Dishes(int id, String name, String description, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    /**
     * Method to get dish id
     * @return int id
     */
    public int getId() {
        return id;
    }
    /**
     * Method to get dish name
     * @return string name
     */
    public String getName() {
        return name;
    }
    /**
     * Method to get dish description
     * @return string description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Method to get dish price
     * @return float price
     */
    public float getPrice() {
        return price;
    }
    /**
     * Method to set dish id
     * @param id dish id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Method to set dish name
     * @param name dish name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Method to set dish description
     * @param description dish description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Method to set dish price
     * @param price dish price
     */
    public void setPrice(int price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return "Dish{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
