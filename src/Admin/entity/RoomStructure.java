package Admin.entity;

import java.util.ArrayList;

public class RoomStructure {
    private  Integer row,col;
    private ArrayList<SeatStructure> roomStt;
    private Integer id_room;

    public Integer getId_room() {
        return id_room;
    }

    public void setId_room(Integer id_room) {
        this.id_room = id_room;
    }
    public RoomStructure(Integer row, Integer col,  ArrayList<SeatStructure> roomStt,Integer id_room) {
        this.row = row;
        this.col = col;
        this.roomStt = roomStt;
        this.id_room=id_room;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }
    public ArrayList<SeatStructure> getRoomStt() {
        return roomStt;
    }
    public void setRoomStt(ArrayList<SeatStructure> roomStt) {
        this.roomStt = roomStt;
    }

}
