package Admin.CTL;

import Admin.config.ControllerDB;
import Admin.entity.Cinema;
import Admin.entity.Room;
import Admin.entity.RoomStructure;
import Admin.entity.SeatStructure;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.MaskerPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static Admin.CTL.R_ST_F_CNM.typeAction;
import static Admin.config.Data.create_room;
import static Admin.config.Data.create_room_structure;

public class CreateRoomCtl implements Initializable {
    public TextField TFrow,TFcol,   TFRoomName;
    public ComboBox<String> cbTypeRoom;
    public BorderPane bdpane;
    public AnchorPane acpane;
    private GridPane grpane;
    private String actionT="",color="none";
    private boolean affectCol=true;
    public ArrayList<SeatStructure> RoomStt;
    private Integer row=0,col=0;

    public static Room roomEdit;
    public static RoomStructure room_structureEdit;
    public static boolean ROOMeditSTT=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> typeroom= FXCollections.observableArrayList("2D","3DMAX","GOLDENCLAS","LAMOUR");
        cbTypeRoom.setItems(typeroom);
        if(ROOMeditSTT){
            cbTypeRoom.setValue(roomEdit.getId_type_room());
            TFRoomName.setText(roomEdit.getName());
            TFrow.setText(String.valueOf(room_structureEdit.getRow()));
            TFcol.setText(String.valueOf(room_structureEdit.getCol()));
        }
    }
    static ArrayList<Button> btnL;
    public void Create(ActionEvent actionEvent) {
        if(TFrow.getText().matches("[0-9]{1,2}") && TFcol.getText().matches("[0-9]{1,2}") ){
            RoomStt= new ArrayList<>();
            btnL= new ArrayList<>();
            row=Integer.parseInt(TFrow.getText());
            col=Integer.parseInt(TFcol.getText());
            HBox hb = new HBox();
            if(cbTypeRoom.getValue().matches("2D")){
                AddAction(hb,"Other","#21A13F");
                AddAction(hb,"Normal","#AD9B92");
                AddAction(hb,"VIP","#934454");
                AddAction(hb,"SWEETBOX","#DC1A68");
                AddAction(hb,"Space","none");

            }else if(cbTypeRoom.getValue().matches("3DMAX")){
                AddAction(hb,"Other","#21A13F");
                AddAction(hb,"Normal","#AD9B92");
                AddAction(hb,"VIP","#934454");
                AddAction(hb,"Space","none");
            }else if(cbTypeRoom.getValue().matches("GOLDENCLAS")){
                AddAction(hb,"GOLDENCLAS","#764F1E");
                AddAction(hb,"Space","none");
            }else{
                AddAction(hb,"LAMOUR","#4B3B32");
                AddAction(hb,"Space","none");
            }
            hb.setSpacing(5);hb.setStyle("-fx-background-color: #212121");
            hb.setAlignment(Pos.CENTER);
            hb.setPadding(new Insets(5,0,5,0));
            bdpane.setBottom(hb);
            create_room_structure = new RoomStructure(row,col,null,null);

            Cinema cmn=R_ST_F_CNM.CNM_List.stream().filter(e->e.getName().matches(R_ST_F_CNM.baseD)).collect(Collectors.toList()).get(0);
            create_room = new Room(null,TFRoomName.getText(),cbTypeRoom.getValue(),cmn.getId());
            CreateRoom();

        }
    }

    public  void AddAction(HBox hB,String type,String cl){
        Button btn= new Button(type); btn.setPrefWidth(90);btn.setPrefHeight(45);

//            btn.setStyle("-fx-background-color: #EAEAEA");

        btn.setOnMouseClicked(e->{
//            btn.setStyle("-fx-background-color: "+(type.matches("Space")?"":cl));
            btnL.forEach(a->{
                if(a.equals(btn)){
                    a.setStyle("-fx-background-color:"+cl);
                }else{
                    a.setStyle("-fx-background-color:#EAEAEA");
                }
            });
            actionT=type;color=cl;
            affectCol=type.matches("Space");
            if(RoomStt.isEmpty()){
                RoomStt.add(new SeatStructure(type,cl,null,null));
            }else{
                if(RoomStt.stream().noneMatch( r->r.getTypeSeat().matches(type) )){ // user noneMatch thay the !allMatch
                    RoomStt.add(new SeatStructure(type,cl,null,null));
                }
            }
        });
        btnL.add(btn);
        hB.getChildren().add(btn);

    }
    public  static Button [][] arrBTN;
    public void CreateRoom(){
        arrBTN = new Button[row][col];
        grpane= new GridPane();
        grpane.setStyle("-fx-background-color: #232323");
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                Button btn= new Button("");btn.setPrefWidth(45);btn.setPrefHeight(45);
                grpane.add(btn,j,i,1,1);
                arrBTN[i][j]=btn;
                int finalI = i,finalJ = j;

                btn.setOnAction(e->{
                    changeBTN(finalI, finalJ);
//                    set row and col
                    for(SeatStructure r:RoomStt){
                        if(r.getColorSeat().matches(color)){
                            if(r.getColorSeat().matches("none")){
                                if(r.getRow()==null){
                                    r.setCol(finalJ+1);r.setRow(finalI+1);
                                    break;
                                }else{
                                    RoomStt.add(new SeatStructure(actionT,color,finalI+1,finalJ+1));
                                    break;
                                }
                            }else{
                                r.setCol(finalJ+1);r.setRow(finalI+1);
                                break;
                            }
                        }
                    }
                });

            }
        }
        grpane.setVgap(5);
        grpane.setHgap(5);
        grpane.setAlignment(Pos.CENTER);grpane.setStyle("-fx-background-color: #232323");
        bdpane.setCenter(grpane);
    }
    public void changeBTN(int r,int c){
        String cl="-fx-background-color: "+color;
        if(affectCol){
            for(int i=0;i<=r;i++){
                arrBTN[i][c].setStyle(cl);
            }
        }else{
            for(int i=0; i <= c ;i++){
                arrBTN[r][i].setStyle(cl);
            }
        }

    }
    public void SaveRoom(ActionEvent actionEvent) throws SQLException {
        create_room_structure.setRoomStt(RoomStt);
//         save to db
//        insert date room, room_structure, room_structure_detail
        ControllerDB db= new ControllerDB();

        if(ROOMeditSTT){
            db.deleteRoom(roomEdit.getId());
        }
        db.SaveRoom();

        db.getRoomStructure();
        R_ST_F_CNM.tableV.setItems(R_ST_F_CNM.Room_List);

        bdpane.setCenter(acpane);
    }
    public void listRoom(ActionEvent actionEvent) throws IOException {
        typeAction="Room";
        View.Show();
        ROOMeditSTT=false;
    }
//    char text='A'; int index=0;
//    public void RenderRoom(){
//        text='A';
//        RoomStructure r= roomStructureRender;
//        ArrayList<SeatStructure> rSTT= r.getRoomStt().stream().filter(e->!e.getTypeSeat().matches("Space") && !e.getTypeSeat().matches("Other")).collect(Collectors.toCollection(ArrayList::new));
//        ArrayList<SeatStructure> space = r.getRoomStt().stream().filter(e->e.getTypeSeat().matches("Space")).collect(Collectors.toCollection(ArrayList::new));
//        SeatStructure Other = null;
//        List<SeatStructure> L1 = r.getRoomStt().stream().filter(e->e.getTypeSeat().matches("Other")).collect(Collectors.toList());
//        if(!L1.isEmpty()){
//            Other=L1.get(0);
//        }
//
//        GridPane grpane= new GridPane();
//        for(int a=0;a<rSTT.size();a++){
//            SeatStructure rm=rSTT.get(a);
//            for(int i = a==0 ? 0: rSTT.get(a-1).getRow() ; i<rm.getRow();i++) {
//                int indexcol=0;
//                for (int j = 0; j < rm.getCol(); j++) {
//                    Button btn= new Button("");btn.setPrefWidth(45);btn.setPrefHeight(45);
//                    int finalI = i,finalJ = j;
//                    long check= space.stream().filter(s->finalI <= s.getRow()-1 &&  finalJ==s.getCol()-1).count(); // check space
//                    boolean check2 = Other != null && (finalI <= Other.getRow() - 1 && finalJ <= Other.getCol() - 1);
//                    if(check==0){
//                        btn.setText(String.valueOf(text) + (indexcol+1));
//                        if(check2){
//                            btn.setStyle("-fx-background-color: "+Other.getColorSeat());
//                            indexcol++;
//                        }else{
//                            btn.setStyle("-fx-background-color: "+rm.getColorSeat());
//                            indexcol++;
//                        }
//
//                    }else{
//                        btn.setStyle("-fx-background-color:none ");
//                    }
//                    grpane.add(btn,j,i);
//                }
//                text = rm.getTypeSeat().matches("Space")?text: (char) (text + 1);
//            }
//        }
//
//        grpane.setVgap(5);
//        grpane.setHgap(5);
//        grpane.setAlignment(Pos.CENTER);
//
//
//    }
    public void back(ActionEvent actionEvent) {
        row=0;col=0;
        bdpane.setCenter(acpane);
    }


}
