package fr.univamu.iut.menus.model;

import jakarta.json.bind.annotation.JsonbDateFormat;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Classe représentant un menu
 */
public class Menu {

    /**
     * Identifiant du menu
     */
    protected Integer id;

    /**
     * Nom du menu
     */
    protected String name;

    /**
     * Identifiant du créateur du menu
     */
    protected Integer creatorId;

    /**
     * Nom du créateur du menu (enrichi depuis l'API users)
     */
    protected String creatorName;

    /**
     * Date de création du menu
     */
    @JsonbDateFormat("yyyy-MM-dd")
    protected Date creationDate;

    /**
     * Date de mise à jour du menu
     */
    @JsonbDateFormat("yyyy-MM-dd")
    protected Date updateDate;

    /**
     * Plats dans le menu
     */
    protected ArrayList<DishSummary> dishes;

    /**
     * Prix total du menu
     */
    protected BigDecimal totalPrice;

    /**
     * Constructeur par défaut
     */
    public Menu(){
    }

    /**
     * Constructeur de menu
     * @param id un entier avec l'identifiant du menu
     * @param name une chaine de caractere avec le nom du menu
     * @param creatorId un entier avec l'identifiant du menu
     * @param dishes une liste de plats lié au menu
     * @param totalPrice un entier avec le prix total du menu
     */
    public Menu(Integer id, String name, Integer creatorId, ArrayList<DishSummary> dishes, BigDecimal totalPrice){
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.dishes = dishes;
        this.totalPrice = totalPrice;
    }

    /**
     * Constructeur complet du menu
     */
    public Menu(Integer id, String name, Integer creatorId, Date creationDate, Date updateDate, ArrayList<DishSummary> dishes, BigDecimal totalPrice) {
        this(id, name, creatorId, dishes, totalPrice);
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public ArrayList<DishSummary> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<DishSummary> dishes) {
        this.dishes = dishes;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Recalcule le prix total à partir des plats ayant un prix connu.
     */
    public void recomputeTotalPrice() {
        if (dishes == null || dishes.isEmpty()) {
            totalPrice = BigDecimal.ZERO;
            return;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (DishSummary dish : dishes) {
            if (dish != null && dish.getPrice() != null) {
                sum = sum.add(dish.getPrice());
            }
        }
        totalPrice = sum;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creatorId=" + creatorId +
                ", creatorName='" + creatorName + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", dishes=" + dishes +
                ", totalPrice=" + totalPrice +
                '}';
    }

    /**
     * Représentation d'un plat dans le menu.
     */
    public static class DishSummary {
        protected Integer id;
        protected String name;
        protected BigDecimal price;

        public DishSummary() {
        }

        public DishSummary(Integer id, String name, BigDecimal price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "DishSummary{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    '}';
        }
    }
}

