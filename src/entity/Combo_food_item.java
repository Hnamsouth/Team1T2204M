package entity;

public class Combo_food_item {
    private Integer id;
    private String id_combo_food,id_food;
    private Integer amount,decrease;

    public Combo_food_item(Integer id, String id_combo_food, String id_food, Integer amount, Integer decrease) {
        this.id = id;
        this.id_combo_food = id_combo_food;
        this.id_food = id_food;
        this.amount = amount;
        this.decrease = decrease;
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

    public String getId_food() {
        return id_food;
    }

    public void setId_food(String id_food) {
        this.id_food = id_food;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getDecrease() {
        return decrease;
    }

    public void setDecrease(Integer decrease) {
        this.decrease = decrease;
    }
}
