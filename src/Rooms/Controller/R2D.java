package Rooms.Controller;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import entity.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

import static DBcontroller.Data.*;


public class R2D  {

    public static ObservableList<Order> DSGhe=FXCollections.observableArrayList();
    public static VBox vbx=new VBox();
    @FXML
    public AnchorPane acPane;
    char Cname='A';
    int BTNPRE_W,BTNPRE_H,COL,ROW,FONT_SIZE,GT=3,GVIP=6,GSB;
    String NM="#AD9B92",VIP="#934454",SWB="#DC1A68",DD="#AE2A33";
    String RoomName="";
    Double spacing;
    private final String R1="Cinema 1",R2="Cinema 2",R3="Cinema 3",R4="Cinema 4",R5="Cinema 5",R6="Cinema 6",R7="Cinema 7",IMAX="3DMAX";

    public R2D(){
        String RoomName= showtime_time_selected.getId_room();
        vbx.alignmentProperty().setValue(Pos.CENTER);
        if(RoomName.matches(R1) || RoomName.matches(R2)|| RoomName.matches(R3)|| RoomName.matches(R4)|| RoomName.matches(R5)){
            COL=16;     ROW=9; BTNPRE_W=45;    BTNPRE_H=45;spacing=4.5;FONT_SIZE=14; GSB=1;
        }else if(RoomName.matches(R7) || RoomName.matches(R6)){
            COL=24;     ROW=11;BTNPRE_W=40;    BTNPRE_H=40;spacing=4.0;FONT_SIZE=12; GSB=1;
        }else if(RoomName.matches(IMAX)){
            COL=30;     ROW=15;BTNPRE_W=37;    BTNPRE_H=37;spacing=2.5;FONT_SIZE=10; GSB=0;
        }
        this.RoomName=RoomName;
    }

    public  static  ArrayList<HBox> hbsts = new ArrayList<HBox>();
    public  static  VBox vbsts = new VBox();
    public VBox createVB() {

        for (int i = 0; i < ROW;i++) {
            HBox hb= new HBox();
            hbsts.add(i,hb);
            hb.alignmentProperty().setValue(Pos.CENTER);
            int index=0;
            for (int j = 0; j < COL;j++) {
//                tỷ lệ rạp 2 - 6 - 2 theo chiều ngang
                if(RoomName.matches(R1) || RoomName.matches(R2)|| RoomName.matches(R3)|| RoomName.matches(R4)|| RoomName.matches(R5)){
                    if(j !=0 && j != COL-1 || i == ROW-1 ){
                        add(i,index,hbsts.get(i)); index++; }
                    else{
                        addSpace(hbsts.get(i));}
                }else if(RoomName.matches(R7) || RoomName.matches(R6)){
                    if(j !=4 && j != 19 || i == ROW-1 ){
                        add(i,index,hbsts.get(i)); index++; }
                    else{
                        addSpace(hbsts.get(i));}
                }else if(RoomName.matches(IMAX)){
                    if(j !=7 && j != 8 && j !=21 && j != 22 || i == ROW-1 ){
                        add(i,index,hbsts.get(i)); index++;}
                    else{
                        addSpace(hbsts.get(i));}
                }
                hb.setSpacing(spacing);
            }
            vbx.getChildren().add(hbsts.get(i));
            vbx.setSpacing(spacing);
            Cname++;
        }
        return this.vbx;
    }
    public static ArrayList<Button> seat=new ArrayList<Button>();
    public void add(int i,int j,HBox hb){
         Button btn= new Button();
        seat.add(i,btn);
        seat.get(i).setPrefWidth(BTNPRE_W);seat.get(i).setPrefHeight(BTNPRE_H);
//                    CHECK vị trí đã được đặt  thêm bắt sự kiên vào chỗ chưa đc đặt ngược lại thì disable
        if(!check(String.valueOf(Cname) + (j+1))){
            btnAC(seat.get(i),i,j,hb);
        }else{
            seat.get(i).setText("X");
            seat.get(i).setDisable(true);
        }
        hb.getChildren().add(seat.get(i));

    }
    private void btnAC(Button btn,int i,int j,HBox hb) {
        btn.setText(String.valueOf(Cname) + (j+1));
        String clP=i<GT ? NM : i == ROW-1 ?  GSB==1 ? SWB : VIP: VIP;
        btn.setStyle("-fx-background-color:"+clP);
        btn.getStyleClass().add("btn");
        btn.setFont(Font.font(FONT_SIZE));

        btn.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            String cl= btn.getStyle().equals("-fx-background-color:"+clP) ? "-fx-background-color:"+DD : "-fx-background-color:"+clP;
//            nếu hàng cuối là sweet box
            if(i==ROW-1 && GSB==1){
//          set style
                    Data.type_seat_selected="SWEETBOX";
                    Button s=new Button();
                    if(j%2==0){
                        hb.getChildren().get(j+1).setStyle(cl);
                        btn.setStyle(cl);
                    }else      {
                        hb.getChildren().get(j-1).setStyle(cl);
                        btn.setStyle(cl);
                    };
                        addOrderItem("SWEETBOX",btn);
                        addOrderItem("SWEETBOX", (Button) (j%2==0?hb.getChildren().get(j+1):hb.getChildren().get(j-1)));

            }else if(i<3){
                btn.setStyle(cl);
                Data.type_seat_selected="Normal";
                addOrderItem("Normal",btn);
            }else{
                btn.setStyle(cl);
                Data.type_seat_selected="VIP";
                addOrderItem("VIP",btn);
            }

        });

    }



    public  void addOrderItem(String Typeseat , Button btn)  {
        DBcontroller db= new DBcontroller();
        try {
            db.getSeatType();
        }catch (Exception e) {

        }
        seat_type.forEach(dt->{
            if(Typeseat.matches(dt.getName())){
                Double price= film_selected.getPrice()+dt.getPlus_origin_price()*100/ film_selected.getPrice();
                Order od= new Order(
                        btn.getText(),
                        Typeseat,
                        dt.getId(),
                        film_selected.getName(),
                        showtime_time_selected.getId_room(),
                        type_room_selected,
                        price,
                        showtime_time_selected.getDate(),
                        showtime_time_selected.getTime()
                );

                if(Data.Order_item.stream().anyMatch(e->e.getName_seat().matches(btn.getText()))){

                    Data.Order_item.removeIf(o->o.getName_seat().matches(btn.getText()));

                    if(Mctl.seat_selected!=null) {
                        Mctl.seat_selected.removeIf(t->t.matches(btn.getText()));
                    }

                }else{

                    Data.Order_item.add(od);

                    Mctl.seat_selected.add(btn.getText());
                    Data.current_seat_amount++;
                }
            }
        });
    }
    public boolean check(String ID){
        ArrayList<String> a=new ArrayList<>();
        a.add("E1");a.add("F3");a.add("D5");a.add("H16");
        return a.contains(ID);
    }
    public void addSpace(HBox hb){
        Button btn= new Button();
        btn.setStyle("-fx-background-color:none");btn.setPrefWidth(BTNPRE_W);btn.setPrefHeight(BTNPRE_H);
        hb.getChildren().add(btn);
    }
    private void showalert() {
        Alert al= new Alert(Alert.AlertType.WARNING);
        al.setContentText("Please choose the same seat type ");
        al.getButtonTypes().add(ButtonType.CLOSE);
        al.show();
    }


}
