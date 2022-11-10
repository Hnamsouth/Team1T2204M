package Rooms.Controller;

import entity.ChonGhe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class LAMOUR {
    public static ObservableList<ChonGhe> DSGhe= FXCollections.observableArrayList();
    public VBox vbx=new VBox();
    char Cname='A';
    int BTNPRE_W,BTNPRE_H,    COL,     ROW ;
    String LAMOUR,DD;

    public LAMOUR() {
        vbx.alignmentProperty().setValue(Pos.CENTER);
    }
    public  VBox createVB()  {
        BTNPRE_W=45;    BTNPRE_H=45;    COL=11;     ROW=6  ;
        LAMOUR="#4B3B32";DD="#AE2A33";

        for (int i = 0; i < ROW;i++) {
            HBox hb= new HBox();
            hb.alignmentProperty().setValue(Pos.CENTER);
            int index=0;
            for (int j = 0; j < COL;j++) {
//                tỷ lệ rạp 2 - 6 - 2 theo chiều ngang
                if(j !=2 ){
                    Button btn= new Button();
                    btn.setPrefWidth(BTNPRE_W);btn.setPrefHeight(BTNPRE_H);
//                    CHECK vị trí đã được đặt  thêm bắt sự kiên vào chỗ chưa đc đặt ngược lại thì disable
                    if(!check(String.valueOf(Cname) + (j+1))){
                        btnAC(btn,i,index,hb);
                        index++;
                    }else{
                        btn.setText("X");
                        btn.setDisable(true);
                        index++;
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
        btn.setStyle("-fx-background-color:"+LAMOUR);
        btn.getStyleClass().add("btn");
        btn.setOnMouseClicked(e->{
            String cl= btn.getStyle().equals("-fx-background-color:"+LAMOUR) ? "-fx-background-color:"+DD : "-fx-background-color:"+LAMOUR;
            btn.setStyle(cl);
        });
    }

    public boolean check(String ID){
        ArrayList<String> a=new ArrayList<>();
        a.add("E1");a.add("F3");a.add("D5");a.add("H16");
        return a.contains(ID);
    }

}
