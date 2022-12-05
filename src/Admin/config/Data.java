package Admin.config;

import Admin.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.ArrayList;

public class Data {
//    hd-c
    public final static ObservableList<String> filmStatus= FXCollections.observableArrayList("Now Showing","Coming Soon","Stop Showing");

    public static Integer id_manager,id_filmEdit_ST=0,id_roomEdit_ST=0;
//      create
    public  static Room create_room;
    public  static RoomStructure create_room_structure;
    public static Film create_film;
    public static Showtime create_showtime;
    public static Cinema create_cinema;



//    get
    public static ObservableList<RoomStructure> listRoomStructure = FXCollections.observableArrayList();
    public static ObservableList<String> city= FXCollections.observableArrayList();
    public static ObservableList<String> room_showtime= FXCollections.observableArrayList();
    public static ObservableList<Cinema> Cinema= FXCollections.observableArrayList();
    public static  ObservableList<ArrayList<TotalCinema>> TotalCinema_List= FXCollections.observableArrayList();
    public static  ObservableList<PieChart.Data> TotalCinemaInWeek= FXCollections.observableArrayList();

}
