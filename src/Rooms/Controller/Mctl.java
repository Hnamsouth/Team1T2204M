package Rooms.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class Mctl implements Initializable {
//    test
    public BorderPane bdpane;
    public HBox hbUD;
    public VBox vB =new VBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        room1(null);
    }

    public void room1(ActionEvent actionEvent) {
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

    public void room2(ActionEvent actionEvent) {
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
    }

    public void room3(ActionEvent actionEvent) {
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
        rec.setStrokeWidth(0);rec.setWidth(35);rec.setHeight(35);
        rec.fillProperty().setValue(Color.valueOf(color));
//
        btn.graphicTextGapProperty().set(15);
        btn.graphicProperty().set(rec);

        return btn;
    }
}
