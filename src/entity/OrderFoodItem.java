package entity;

public class OrderFoodItem {
    private Integer id;
    private String id_combo_food,description;
    private Integer id_order_food,amount;
    private Double price;

    public OrderFoodItem(Integer id, String id_combo_food, Integer id_order_food, Integer amount) {
        this.id = id;
        this.id_combo_food = id_combo_food;
        this.id_order_food = id_order_food;
        this.amount = amount;
    }

    public OrderFoodItem(Integer id, String id_combo_food, String description, Integer id_order_food, Integer amount,Double price) {
        this.id = id;
        this.id_combo_food = id_combo_food;
        this.description = description;
        this.id_order_food = id_order_food;
        this.amount = amount;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getId_combo_food() {
        return id_combo_food;
    }

    public void setId_combo_food(String id_combo_food) {
        this.id_combo_food = id_combo_food;
    }

    public Integer getId_order_food() {
        return id_order_food;
    }

    public void setId_order_food(Integer id_order_food) {
        this.id_order_food = id_order_food;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
