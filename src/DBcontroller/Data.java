package DBcontroller;

import Rooms.Controller.Mctl;
import Rooms.Controller.R2D;
import entity.*;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {

    public static Film film_selected;
//    giờ chiếu của 1 phim chỉ định
    public static ArrayList<Showtime> showtime_film_selected;
//    giờ khác hàng chọn
    public static Showtime showtime_time_selected;
// các loại ghế của phòng được chọn: vd 2D(Normal,VIP,SWEETBOX)
    public static ArrayList<Seat_type> seat_type;
//    danh sách phim
    public static ArrayList<Film> list_film;
//
    public static ArrayList<room> room_of_film;
    public static String type_room_selected;
    public static String type_seat_selected;



    public static int current_seat_amount=0;
//    giá tiền của loại ghế trong phòng được chọn
    public static HashMap<Integer,String> plus_of_type;
    public static ArrayList<Seat_type> seat_type_room_selected;
//  tất cả các ghế đã được chọn của khách hàng tại lịch chiếu chỉ định
    public static ArrayList<String> seat_selected;

// cac ghế đã được đặt trong đơn hàng ;
    public static ArrayList<String> order_seat_selected;

    public static  boolean EditSTS = false;
    public static  Integer Id_Order;


    public static ObservableList<String> type_room= FXCollections.observableArrayList("2D","SWEETBOX","3DMAX","GOLDENCLAS","LAMOUR");
    public static ObservableList<Order> Order_item = FXCollections.observableArrayList();

//    food

    public static ArrayList<food> food;
    public static ArrayList<Combo_food_item> Combo_food_item;
//    public static ArrayList<ComboFood> Combo_food;

    public static ObservableList<ComboFood> Combo_food = FXCollections.observableArrayList();
    public static ObservableList<OrderFoodItem> order_food_item= FXCollections.observableArrayList();
//    public static ArrayList<OrderFoodItem> order_food_item=new ArrayList<OrderFoodItem>();



    public static void setValueEmpty(){
        Order_item.clear();
        seat_type=null;
        current_seat_amount=0;
//        seat_selected=null;
        type_room_selected="";
        type_seat_selected="";
        seat_selected=null;

//        order_seat_selected=null;
//        plus_of_type=null;
//        room_of_film=null;
//        Mctl.seat_selected=new ArrayList<>();
        Mctl.seat_selected.clear();
        Combo_food.clear();
//        order_food_item=new ArrayList<>();
        order_food_item.clear();
    }
}
