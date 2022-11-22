package Rooms.Controller;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

import static DBcontroller.Data.seat_selected;


public class Mctl implements Initializable {
//    test
//    FXML
    public BorderPane bdpane;
    public HBox hbUD;
    public VBox vB ;
    public Label nameFilm,dateTimeRoom,comboSelected;
    public Text seat,combo,total;
    public HBox seatSelected;
    public Button btnBack;
    public Button CancelEdit;
    //    VALUE
    public static Double totalseat,totalCombo,TotalSeatCB;
    public static ObservableList<String> seat_selected= FXCollections.observableArrayList();
    public static boolean FoodSTS=false;
    private VBox prev;
    Boolean foodBroom=false;
    public Mctl(){};
    DBcontroller db;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        db= new DBcontroller();
        if(Data.EditSTS){
            CancelEdit.setVisible(true);
            TotalSeatCB=totalseat;
            seat.setText(String.valueOf(totalseat));
            total.setText(String.valueOf(TotalSeatCB));
            seat_selected.forEach( e->{
                String s=seatSelected.getChildren().isEmpty() ? e:", "+e;
                Text txt= new Text(s);
                txt.setStyle("-fx-fill: white");
                txt.setFont(Font.font("System Bold",20));
                seatSelected.getChildren().add(txt);
            });

        }else{
            totalseat=0.0;totalCombo=0.0;TotalSeatCB=0.0;
        }
        if(!FoodSTS){

            nameFilm.textProperty().set(Data.film_selected.getName());
            String dtr=Data.showtime_time_selected.getId_room()+"  |  "+Data.showtime_time_selected.getDate()+"  |  "+Data.showtime_time_selected.getTime().toString().substring(0,5);

            dateTimeRoom.textProperty().set(dtr);
            dateTimeRoom.setFont(Font.font("Bold",20));

            try {
                db.plusprice();
                db.getSeatSelected();
                Data.seat_selected.forEach(System.out::println);

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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
            if(!seat_selected.isEmpty()){
                btnHandle();
            }
            if(!Data.order_food_item.isEmpty()){
                totalfood();
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
                createBTN("VIP","#934454")
        );
        if(R2D.GSB==1){
            vB.getChildren().add(createBTN("Sweet Box","#DC1A68"));
        }
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
                txt.setStyle("-fx-fill: white");
                txt.setFont(Font.font("System Bold",20));
                seatSelected.getChildren().add(txt);
            });
            totalSeat();
        }else{
            seatSelected.getChildren().clear();

        }
    }
    public void totalSeat(){
        totalseat=0.0;
        Data.Order_item.forEach(e->{
            totalseat+=e.getPrice();
        });
        seat.setText(String.valueOf(totalseat));
        total.setText(String.valueOf(totalseat+(totalCombo!=0?totalCombo:0)));
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
            Data.Order_item.clear();
            seat_selected.clear();
            Data.order_food_item.clear();
            Mctl.FoodSTS=false;
            Data.current_seat_amount=0;
            R2D.vbx=new VBox();
            Main.editV.showtime();

        }else{
            bdpane.setCenter(prev);
            bdpane.setRight(vB);
            foodBroom=false;
        }


    }
    public void toListFilm(ActionEvent actionEvent) throws Exception{
        Mctl.FoodSTS=false;
        Data.setValueEmpty();
        Main.editV.ListFlim();
    }
    public void btnSubmit(ActionEvent actionEvent) throws Exception {
//        remove bdpane right
        if(foodBroom){
//            Main.editV.PrintInvoices();
            Main.editV.CheckOut();
//            foodBroom=false;
        }else{
            if(seat_selected==null||seat_selected.isEmpty()){
                Alert al= new Alert(Alert.AlertType.WARNING);
                al.setContentText("Seat empty....!");
                al.getButtonTypes().add(ButtonType.CLOSE);
                al.show();
            }else{
                bdpane.setRight(null);
                foodBroom=true;
                db.getAllFood();
                db.getCombo_food_item();
                db.getCombo_food();
                food();
            }
        }
    }
    private void food() throws Exception {
        Parent food= FXMLLoader.load(Mctl.class.getResource("/Rooms/Food2.fxml"));
        bdpane.setCenter(food);
        BorderPane.setAlignment(food,Pos.CENTER);

        for(int i=0;i<FoodController.MNPL.size();i++){
            FoodController.MNPL.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, a->{
                totalfood();
            });
        }

    }
    public  void totalfood(){
        totalCombo=0.0;
        Data.order_food_item.forEach(z->{
            System.out.println(z.getAmount()+"-------------\t"+z.getId_combo_food());
            totalCombo+=(z.getPrice()*z.getAmount());
        });
        combo.setText(String.valueOf(totalCombo));
        total.setText(String.valueOf(totalseat+totalCombo));
    }
    public void CancelEdit(ActionEvent actionEvent) throws IOException {
        Main.editV.CancalEdit();
    }
}
