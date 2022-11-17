package Rooms.Controller;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Mctl implements Initializable {
//    test
//    FXML
    public BorderPane bdpane;
    public HBox hbUD;
    public VBox vB ;
    public Label nameFilm,dateTimeRoom,comboSelected;
    public Text seat,combo,total;
    public Text totalFood;
    public HBox seatSelected;
    public Button btnBack;
    public BorderPane bdPaneFood;
    //    VALUE
    Double totaldb;
    public static ArrayList<String> seat_selected;
    public static boolean FoodSTS=false;

    public Mctl(){};

    @Override
    public void initialize(URL location, ResourceBundle resources){
        if(!FoodSTS){
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
            db.getSeatSelected();
        }catch (Exception e) {
            e.printStackTrace();
        }
//        new: nếu chỉ định đến food thì bắt điều kiện

            vB=new VBox();
            vB.alignmentProperty().set(Pos.TOP_CENTER);
            vB.setPrefWidth(121);vB.setPrefHeight(550);
            bdpane.setRight(vB);
            bdpane.setAlignment(vB,Pos.CENTER);
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
        }else{
            if(bdpane!=null){
                bdpane.setRight(null);
                btnBack.setVisible(false);
            }

            try {
                food();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



    }

    public void room1() {
        GOLDENCLASS room= new GOLDENCLASS();
        VBox r=room.createVB();
        if(bdpane.getCenter() != r ){
            bdpane.setCenter(r);
            prev=r;
        }
        vB.getChildren().clear();
        vB.getChildren().addAll(
                createBTN("Đang chọn","#AE2A33"),
                createBTN("Đã đặt","#717071"),
                createBTN("GOLDEN CLASS","#764F1E")
        );

    }
    public void room2() {
        R2D room=new R2D();
        VBox r=room.createVB();
        if(bdpane.getCenter() != r ){
            bdpane.setCenter(r);
            prev=r;
        }
        vB.getChildren().clear();
        vB.getChildren().addAll(
                createBTN("Đang chọn","#AE2A33"),
                createBTN("Đã đặt","#717071"),
                createBTN("Thường","#AD9B92"),
                createBTN("VIP","#934454"),
                createBTN("Sweet Box","#DC1A68")
        );
//        seat handle
        R2D.seat.forEach(e->{
            e.addEventHandler(MouseEvent.MOUSE_CLICKED,a->{
                btnHandle();
            });
        });
    }

    public void btnHandle(){
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

            Double tt= Data.film_selected.getPrice();
            for(Integer i:Data.plus_of_type.keySet()){

                if(Data.plus_of_type.get(i).matches(Data.type_seat_selected)){
                    if(seat_selected.size()<Data.current_seat_amount){
                        totaldb-=Data.type_seat_selected.matches("SWEETBOX")?(tt+( i *tt/100))*2 :  tt+( i *tt/100);
                        Data.current_seat_amount=Data.type_seat_selected.matches("SWEETBOX")?Data.current_seat_amount-2:Data.current_seat_amount-1;
                    }else{
                        totaldb+=Data.type_seat_selected.matches("SWEETBOX")?(tt+( i *tt/100))*2 :  tt+( i *tt/100);
                    }
                    seat.setText(String.valueOf(totaldb));
                }
            }
        }else{
            seatSelected.getChildren().clear();

        }
    }
    public void room3() {
        LAMOUR room=new LAMOUR();
        VBox r=room.createVB();
        if(bdpane.getCenter() != r ){
            bdpane.setCenter(r);
            prev=r;
        }
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
        if(!foodBroom){
            Main.editV.showtime();
            Data.Order_item.clear();
            R2D.vbx=new VBox();
            Mctl.FoodSTS=false;
            Data.current_seat_amount=0;
        }else{
            bdpane.setCenter(prev);
            bdpane.setRight(vB);
            foodBroom=false;
        }


    }
    public void toListFilm(ActionEvent actionEvent) throws Exception{
        Main.editV.ListFlim();
        Data.Order_item.clear();
        R2D.vbx=new VBox();
        Mctl.FoodSTS=false;
    }
    private VBox prev;
    Boolean foodBroom=false;
    public void btnSubmit(ActionEvent actionEvent) throws IOException {
//        remove bdpane right
        if(foodBroom){
            Main.editV.PrintInvoices();
        }else{
            bdpane.setRight(null);
            foodBroom=true;
            food();
        }
    }
    private void food() throws IOException {
        Parent food= FXMLLoader.load(Mctl.class.getResource("/Rooms/Food2.fxml"));

        Text txt= new Text("test.......");
        txt.setFont(Font.font(50));
        bdpane.setCenter(food);
        bdpane.setAlignment(food,Pos.CENTER);
    }
}
