package DBcontroller;

import entity.*;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {

    public static String roomname;



    public static ObservableList<Order> Order_item = FXCollections.observableArrayList();;


//    giờ chiếu của tất ca cac phim
    public static ObservableList<ArrayList<Showtime>> showtime_list = FXCollections.observableArrayList();
//    giờ chiếu của 1 phim chỉ định
    public static ArrayList<Showtime> showtime_film_selected;
//    giờ khác hàng chọn
    public static Showtime showtime_time_selected;


    public static ArrayList<Seat_type> seat_type;
    public static ArrayList<Film> list_film;
    public static ArrayList<room> room_of_film;
    public static String type_room_selected;
    public static String type_seat_selected;
    public static ObservableList<String> type_room= FXCollections.observableArrayList("2D","SWEETBOX","3DMAX","GOLDENCLAS","LAMOUR");
    public static Film film_selected;

    public static int current_seat_amount=0;
    public static int plus_original_price;
    public static HashMap<Integer,String> plus_of_type;
    public static ArrayList<Seat_type> seat_type2;



    
}
