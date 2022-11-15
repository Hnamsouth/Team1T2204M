package entity;

import java.sql.Date;
import java.sql.Time;

public class Order {
    String name_seat,name_type_seat,_name_film,room_name,type_room;
    Integer id_seat_type;
    Double price;
    Date date;
    Time time;

    public String getType_room() {
        return type_room;
    }

    public void setType_room(String type_room) {
        this.type_room = type_room;
    }

    public Order(String name_seat, String name_type_seat,Integer id_seat_type, String _name_film, String room_name, String type_room, Double price, Date date, Time time) {
        this.name_seat = name_seat;
        this.name_type_seat = name_type_seat;
        this.id_seat_type=id_seat_type;
        this._name_film = _name_film;
        this.room_name = room_name;
        this.type_room = type_room;
        this.price = price;
        this.date = date;
        this.time = time;
    }

    public Integer getId_seat_type() {
        return id_seat_type;
    }

    public void setId_seat_type(Integer id_seat_type) {
        this.id_seat_type = id_seat_type;
    }

    public String getName_seat() {
        return name_seat;
    }

    public void setName_seat(String name_seat) {
        this.name_seat = name_seat;
    }

    public String getName_type_seat() {
        return name_type_seat;
    }

    public void setName_type_seat(String name_type_seat) {
        this.name_type_seat = name_type_seat;
    }

    public String get_name_film() {
        return _name_film;
    }

    public void set_name_film(String _name_film) {
        this._name_film = _name_film;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
