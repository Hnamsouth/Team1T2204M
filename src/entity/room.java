package entity;

public class room {
    private String id_room,id_type_room;

    public String getId_room() {
        return id_room;
    }

    public void setId_room(String id_room) {
        this.id_room = id_room;
    }

    public String getId_type_room() {
        return id_type_room;
    }

    public void setId_type_room(String id_type_room) {
        this.id_type_room = id_type_room;
    }

    public room(String id_room, String id_type_room) {
        this.id_room = id_room;
        this.id_type_room = id_type_room;
    }
}
