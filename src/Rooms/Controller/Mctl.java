package Rooms.Controller;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import config.Connector;
import entity.Order;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javax.swing.text.StyleConstants.Bold;

public class Mctl implements Initializable {
//    test
    public BorderPane bdpane;
    public HBox hbUD;
    public VBox vB =new VBox();
    public TextField roomname;

    public static ArrayList<Order> order_tk_item;
    public Label nameFilm,dateTimeRoom;
    public Text total;
    Double totaldb;
    public static ArrayList<String> seat_selected;
    public HBox seatSelected;

    public Mctl(){};
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        seat_selected=new ArrayList<>();
        totaldb=0.0;
//        String room="3DMAX";
        nameFilm.textProperty().set(Data.film_selected.getName());
        String dtr=Data.showtime_time_selected.getId_room()+"  |  "+Data.showtime_time_selected.getDate()+"  |  "+Data.showtime_time_selected.getTime().toString().substring(0,5);

        dateTimeRoom.textProperty().set(dtr);
        dateTimeRoom.setFont(Font.font("Bold",20));
//        total.setText(Data.total);
        try {
            DBcontroller db= new DBcontroller();
            db.plusprice();
        }catch (Exception e) {
            e.printStackTrace();
        }


        String room=Data.showtime_time_selected.getId_room();
        if(room.equals("Cinema 1") || room.equals("Cinema 2")|| room.equals("Cinema 3")|| room.equals("Cinema 4")
                || room.equals("Cinema 5")|| room.equals("Cinema 6")|| room.equals("Cinema 7")|| room.equals("3DMAX")){
            room2();
        }else if(room.contains("Golden Class")){
            room1();
        }else{
            room3();
        }
        vB.setPadding(new Insets(20,0,0,0));

    }

    //test room
    public void room1() {
        GOLDENCLASS room= new GOLDENCLASS();
        VBox r=room.createVB();
        if(bdpane.getCenter() != r ){   bdpane.setCenter(r);}
        vB.getChildren().clear();
        vB.getChildren().addAll(
                createBTN("Đang chọn","#AE2A33"),
                createBTN("Đã đặt","#717071"),
                createBTN("GOLDEN CLASS","#764F1E")
        );

    }
    int as=0;
    public void room2() {
        R2D room=new R2D();
        VBox r=room.createVB();
        if(bdpane.getCenter() != r ){   bdpane.setCenter(r);}
        vB.getChildren().clear();
        vB.getChildren().addAll(
                createBTN("Đang chọn","#AE2A33"),
                createBTN("Đã đặt","#717071"),
                createBTN("Thường","#AD9B92"),
                createBTN("VIP","#934454"),
                createBTN("Sweet Box","#DC1A68")
        );
        R2D.seat.forEach(e->{

            e.addEventHandler(MouseEvent.MOUSE_CLICKED,a->{
                DBcontroller db= new DBcontroller();
                if(seat_selected!=null){
//                    show seat
                    seatSelected.getChildren().clear();
                    seat_selected.forEach(q->{
                        String s=seatSelected.getChildren().isEmpty() ? q:", "+q;
                        Text txt= new Text(s);
                        txt.setFont(Font.font("System Bold",20));
                        seatSelected.getChildren().add(txt);
                    });
//                     show total
                    int as=Data.Order_item.size();

//                    if(Data.Order_item.isEmpty() || Data.Order_item.get(0).getName_type_seat()
//                            .matches(Data.Order_item.get(as-1).getName_type_seat()) ){
                        Double tt= Data.film_selected.getPrice();
                        
                        for(Integer i:Data.plus_of_type.keySet()){

                            if(Data.plus_of_type.get(i).matches(Data.type_seat_selected)){
                                if(seat_selected.size()<Data.current_seat_amount){
                                    totaldb-=Data.type_seat_selected.matches("SWEETBOX")?(tt+( i *tt/100))*2 :  tt+( i *tt/100);
                                    Data.current_seat_amount=Data.type_seat_selected.matches("SWEETBOX")?Data.current_seat_amount-2:Data.current_seat_amount-1;
                                }else{
                                    totaldb+=Data.type_seat_selected.matches("SWEETBOX")?(tt+( i *tt/100))*2 :  tt+( i *tt/100);
                                }
                                total.setText(String.valueOf(totaldb));
                            }
                        }
//                    }else{
//                        Alert al= new Alert(Alert.AlertType.WARNING);
//                        al.setContentText("---------------------");
//                        al.getButtonTypes().add(ButtonType.CLOSE);
//                        al.show();
//                    }

                }else{
                    seatSelected.getChildren().clear();

                }
            });
        });


    }

    public void room3() {
        LAMOUR room=new LAMOUR();
        VBox r=room.createVB();
        if(bdpane.getCenter() != r ){   bdpane.setCenter(r);}
        vB.getChildren().clear();
        vB.getChildren().addAll(
                createBTN("Đang chọn","#AE2A33"),
                createBTN("Đã đặt","#717071"),
                createBTN("Lammour","#4B3B32"));
    }
    public Button createBTN(String text, String color){

        Button btn= new Button();
        btn.setPrefWidth(200);btn.setPrefHeight(55);btn.getStyleClass().add("btn-loaighe");
        btn.alignmentProperty().setValue(Pos.TOP_LEFT);btn.setText(text);
//
        Rectangle rec= new Rectangle();
        rec.setStrokeWidth(0);rec.setWidth(22);rec.setHeight(22);
        rec.fillProperty().setValue(Color.valueOf(color));
//
        btn.graphicTextGapProperty().set(10);
        btn.graphicProperty().set(rec);

        return btn;
    }

    public void back(ActionEvent actionEvent) throws Exception{
        Main.editV.showtime();
        Data.Order_item.clear();
        R2D.vbx=new VBox();
    }

    public void toListFilm(ActionEvent actionEvent) throws Exception{
        Main.editV.ListFlim();
        Data.Order_item.clear();
        R2D.vbx=new VBox();
    }
}
