package Rooms.Controller;

import entity.ChonGhe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class R2D  {

    public static ObservableList<ChonGhe> DSGhe=FXCollections.observableArrayList();
    public VBox vbx=new VBox();
    @FXML
    public AnchorPane acPane;
    char Cname='A';
    int BTNPRE_W=45,    BTNPRE_H=45,    COL=16,     ROW=12  ,GT=3,GVIP=6,GSB=1;
    String NM="#AD9B92",VIP="#934454",SWB="#DC1A68",DD="#AE2A33";
//     tạo enum xác định tên phòng
//    bắt điều kiện xác định col - row, màu , loại ghế cho từng phòng
//     render

//      2d: col 25 row 11     ,col 23 row - 11,     col 14- row 9
//     imax : col 37-row 15
//     golden class: col 8, row 7

    public R2D(){
        vbx.alignmentProperty().setValue(Pos.CENTER);
    }
    public VBox createVB() {
        for (int i = 0; i < ROW;i++) {
            HBox hb= new HBox();
            hb.alignmentProperty().setValue(Pos.CENTER);
            for (int j = 0; j < COL;j++) {
//                tỷ lệ rạp 2 - 6 - 2 theo chiều ngang
                if(j !=3 && j != 12 || i == ROW-1 ){
                    Button btn= new Button();
                    btn.setPrefWidth(BTNPRE_W);btn.setPrefHeight(BTNPRE_H);
//                    CHECK vị trí đã được đặt  thêm bắt sự kiên vào chỗ chưa đc đặt ngược lại thì disable
                    if(!check(String.valueOf(Cname) + (j+1))){
                        btnAC(btn,i,j,hb);
//
                    }else{
                        btn.setText("X");
                        btn.setDisable(true);
                    }
                    hb.getChildren().add(btn);
                }else{
                    Button btn= new Button();
                    btn.setStyle("-fx-background-color:none");btn.setPrefWidth(BTNPRE_W);btn.setPrefHeight(BTNPRE_H);
                    hb.getChildren().add(btn);
                }
                hb.setSpacing(4.5);
            }
            vbx.getChildren().add(hb);
            vbx.setSpacing(4.5);
            Cname++;
        }
        return this.vbx;
    }
    private void btnAC(Button btn,int i,int j,HBox hb) {
        btn.setText(String.valueOf(Cname) + (j+1));
        String clP=i<3 ? NM : i == ROW-1 ? SWB : VIP;
        btn.setStyle("-fx-background-color:"+clP);
        btn.getStyleClass().add("btn");
        btn.setOnMouseClicked(e->{
            String cl= btn.getStyle().equals("-fx-background-color:"+clP) ? "-fx-background-color:"+DD : "-fx-background-color:"+clP;
//            nếu hàng cuối là sweet box
            if(i==ROW-1 && GSB!=0){
//          set style
                Button s;
                if(j%2==0){     hb.getChildren().get(j+1).setStyle(cl);btn.setStyle(cl);s=(Button) hb.getChildren().get(j+1);}
                else      {     hb.getChildren().get(j-1).setStyle(cl);btn.setStyle(cl);s=(Button) hb.getChildren().get(j-1);};
                btn.setStyle(cl);
//          add to list
                if(cl.equals("-fx-background-color:"+DD)){
                    DSGhe.addAll(new ChonGhe(btn.getText(),"SweetBox","B10","2022-10-03","01:57:07"),
                            new ChonGhe( s.getText(),"SweetBox","B10","2022-10-03","01:57:07"));
//                    DSGhe.add(new ChonGhe(btn.getText(),"SweetBox","B10","2022-10-03","01:57:07"));
//                    DSGhe.add(new ChonGhe( s.getText(),"SweetBox","B10","2022-10-03","01:57:07"));
                }else{
                    DSGhe.removeIf(ha->ha.getIdGhe().equals(btn.getText())||ha.getIdGhe().equals(s.getText()));
//                    DSGhe.removeIf(ha->ha.getIdGhe().equals(s.getText()));
                }
            }else{
                //          set style
                btn.setStyle(cl);
                //          add to list
                if(cl.equals("-fx-background-color:"+DD)){
                    DSGhe.add(new ChonGhe(btn.getText(),i<3 ? "NorMal" : "VIP","B10","2022-10-03","01:57:07"));
                }else{
                    DSGhe.removeIf(ha->ha.getIdGhe().equals(btn.getText()));
                }

            }
            System.out.println( DSGhe.size());

        });
    }
    public boolean check(String ID){
        ArrayList<String> a=new ArrayList<>();
        a.add("E1");a.add("F3");a.add("D5");a.add("H16");
        return a.contains(ID);
    }


}
