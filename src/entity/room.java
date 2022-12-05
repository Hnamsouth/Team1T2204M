package entity;

public class room {
    private  Integer id,id_cinema;
    private String name,id_type_room;

    public room(Integer id, Integer id_cinema, String name, String id_type_room) {
        this.id = id;
        this.id_cinema = id_cinema;
        this.name = name;
        this.id_type_room = id_type_room;
    }
    public  room(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_cinema() {
        return id_cinema;
    }

    public void setId_cinema(Integer id_cinema) {
        this.id_cinema = id_cinema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_type_room() {
        return id_type_room;
    }

    public void setId_type_room(String id_type_room) {
        this.id_type_room = id_type_room;
    }
}
