package entity;

import java.sql.Date;
import java.sql.Time;

public class Showtime {
    Integer id,id_film;
    String id_room;
    Date date;
    Time time;

    public Showtime(Integer id, Integer id_film, String id_room, Date date, Time time) {
        this.id = id;
        this.id_film = id_film;
        this.id_room = id_room;
        this.date = date;
        this.time = time;
    }
    public Showtime(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_film() {
        return id_film;
    }

    public void setId_film(Integer id_film) {
        this.id_film = id_film;
    }

    public String getId_room() {
        return id_room;
    }

    public void setId_room(String id_room) {
        this.id_room = id_room;
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
