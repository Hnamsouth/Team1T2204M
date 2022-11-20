package entity;

import java.util.ArrayList;

public class ComboFood {
    private String id;
    private Double price;
    private ArrayList<Combo_food_item> Combo_food_item;

    public ComboFood(String id, Double price, ArrayList<entity.Combo_food_item> combo_food_item) {
        this.id = id;
        this.price = price;
        Combo_food_item = combo_food_item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ArrayList<entity.Combo_food_item> getCombo_food_item() {
        return Combo_food_item;
    }

    public void setCombo_food_item(ArrayList<entity.Combo_food_item> combo_food_item) {
        Combo_food_item = combo_food_item;
    }
}
