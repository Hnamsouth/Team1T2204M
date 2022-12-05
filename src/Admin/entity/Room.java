package Admin.entity;

import Admin.CTL.CreateRoomCtl;
import Admin.CTL.R_ST_F_CNM;
import Admin.CTL.View;
import Admin.config.ControllerDB;
import DBcontroller.Data;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Admin.config.Data.listRoomStructure;


public class Room {
    private  Integer id,id_cinema;
    private String name,id_type_room;
    private HBox action= new HBox();
    private Button PreView = new Button("PreView"),Edit= new Button("Edit"), Delete= new Button("Delete");

    public Room() {

    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId_type_room() {
        return id_type_room;
    }
    public void setId_type_room(String id_type_room) {
        this.id_type_room = id_type_room;
    }
    public Integer getId_cinema() {
        return id_cinema;
    }
    public void setId_cinema(Integer id_cinema) {
        this.id_cinema = id_cinema;
    }
    public HBox getAction() {
        return action;
    }
    public void setAction(HBox action) {
        this.action = action;
    }

    public Room(Integer id,String name, String id_type_room, Integer id_cinema) {
        this.id = id;
        this.name = name;
        this.id_type_room = id_type_room;
        this.id_cinema=id_cinema;


        PreView.setStyle("-fx-text-fill: black");
        Edit.setStyle("-fx-text-fill: black");
        this.action.getChildren().addAll(PreView,Edit);
        this.action.setSpacing(7);

        PreView.setOnAction(e->{
            listRoomStructure.forEach(a->{
                if(a.getId_room().equals(this.getId())){
                    PreView(a,a.getRow(),a.getCol());
                }
            });
        });
        Edit.setOnAction(e->{
            ControllerDB db= new ControllerDB();
            CreateRoomCtl.roomEdit=this;
            CreateRoomCtl.ROOMeditSTT=true;

            try {
                db.getRoomStructureEdit(this.id);
                View.CreateRoom();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
//        Delete.setOnAction(e->{
//            ControllerDB db= new ControllerDB();
//            try {
//                Alert al= new Alert(Alert.AlertType.CONFIRMATION);
//                al.getDialogPane().getButtonTypes().clear();
//                al.getDialogPane().getButtonTypes().addAll(ButtonType.YES,ButtonType.NO);
//                Optional<ButtonType> rs= al.showAndWait();
//                if(rs.get().equals(ButtonType.YES)){
//                    db.deleteRoom(this.id);
//                    R_ST_F_CNM.Room_List.removeIf(a->a.id.equals(this.id));
////                    db.getRoomStructure();
////                    R_ST_F_CNM.tableV.setItems(R_ST_F_CNM.Room_List);
//                }else{
//                    al.close();
//                }
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
    }
    char text='A';
    public void PreView(RoomStructure r,int row,int col){
        text='A';
        ArrayList<SeatStructure> rSTT= r.getRoomStt().stream().filter(e->!e.getTypeSeat().matches("Space") && !e.getTypeSeat().matches("Other")).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<SeatStructure> space = r.getRoomStt().stream().filter(e->e.getTypeSeat().matches("Space")).collect(Collectors.toCollection(ArrayList::new));
        SeatStructure Other = null;
        List<SeatStructure> L1 = r.getRoomStt().stream().filter(e->e.getTypeSeat().matches("Other")).collect(Collectors.toList());
        if(!L1.isEmpty()){
            Other=L1.get(0);
        }

        GridPane grpane= new GridPane();
        for(int a=0;a<rSTT.size();a++){
            SeatStructure rm=rSTT.get(a);
            for(int i = a==0 ? 0: rSTT.get(a-1).getRow() ; i<rm.getRow();i++) {
                int indexcol=0;
                for (int j = 0; j < rm.getCol(); j++) {
                    Button btn= new Button("");btn.setPrefWidth(row>11?36:45);btn.setPrefHeight(row>11?36:45);
                    if(row>11){
                        btn.setFont(Font.font(10));
                    }
                    int finalI = i,finalJ = j;
                    long check= space.stream().filter(s->finalI <= s.getRow()-1 &&  finalJ==s.getCol()-1).count(); // check space
                    boolean check2 = Other != null && (finalI <= Other.getRow() - 1 && finalJ <= Other.getCol() - 1);
                    if(check==0){
                        btn.setText(String.valueOf(text) + (indexcol+1));
                        if(check2){
                            btn.setStyle("-fx-background-color: "+Other.getColorSeat());
                            indexcol++;
                        }else{
                            btn.setStyle("-fx-background-color: "+rm.getColorSeat());
                            indexcol++;
                        }

                    }else{
                        btn.setStyle("-fx-background-color:none ");
                    }
                    grpane.add(btn,j,i);
                }
                text = rm.getTypeSeat().matches("Space")?text: (char) (text + 1);
            }
        }

        grpane.setVgap(5);
        grpane.setHgap(5);
        grpane.setAlignment(Pos.CENTER);
        grpane.setPrefSize(1280,720);

        Alert al= new Alert(Alert.AlertType.NONE);
        al.getDialogPane().setGraphic(grpane);
        al.getDialogPane().setPrefSize(1280,720);
        al.show();
        Window window = al.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            al.close();
        });
    }
}
