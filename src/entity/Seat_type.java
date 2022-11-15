package entity;

public class Seat_type {
    private Integer id,plus_original_price;
    private String name,id_type_room;

    public Seat_type(Integer id, Integer plus_origin_price, String id_type_room, String name) {
        this.id = id;
        this.plus_original_price = plus_origin_price;
        this.id_type_room = id_type_room;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlus_origin_price() {
        return plus_original_price;
    }

    public void setPlus_origin_price(Integer plus_origin_price) {
        this.plus_original_price = plus_origin_price;
    }

    public String getId_type_room() {
        return id_type_room;
    }

    public void setId_type_room(String id_type_room) {
        this.id_type_room = id_type_room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
