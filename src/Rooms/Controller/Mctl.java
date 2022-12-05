package Rooms.Controller;

import Admin.entity.SeatStructure;
import DBcontroller.DBcontroller;
import DBcontroller.Data;
import entity.Order;
import entity.RoomStructure;
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
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static DBcontroller.Data.*;


public class Mctl implements Initializable {
//    test
//    FXML
    public BorderPane bdpane;
    public VBox vB ;
    public Label nameFilm,dateTimeRoom;
    public Text seat,combo,total;
    public HBox seatSelected;
    public Button btnBack;
    public Button CancelEdit;
    //    VALUE
    public static Double totalseat,totalCombo,TotalSeatCB;
    public static ObservableList<String> seat_selected= FXCollections.observableArrayList();
    public static boolean FoodSTS=false;
    public static Boolean onlyFood=false;
    public Boolean foodBroom=false;

    private VBox prev;
    private char text='A';String DD="#AE2A33";int grpIndex=0;
    private DBcontroller db;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        db= new DBcontroller();
        if(!FoodSTS){
//            set info
            nameFilm.textProperty().set(Data.film_selected.getName());
            String dtr=Data.showtime_time_selected.getId_room()+"  |  "+Data.showtime_time_selected.getDate()+"  |  "+ Data.showtime_time_selected.getTime().toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))+ " ~ "+
                    Data.showtime_time_selected.getTime().toLocalTime().plusMinutes(Data.film_selected.getDuration()).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) ;
            dateTimeRoom.textProperty().set(dtr);
            dateTimeRoom.setFont(Font.font("Bold",20));
//            get seat selected
            try {  db.plusprice();    db.getSeatSelected();   }catch (Exception e) {   e.printStackTrace(); }
//        new: nếu chỉ định đến food thì bắt điều kiện
            //            info seat
            vB=new VBox();
            vB.alignmentProperty().set(Pos.TOP_CENTER);
            vB.setPrefWidth(121);vB.setPrefHeight(550);
            setInfoSeat();
            bdpane.setRight(vB);
            bdpane.setAlignment(vB,Pos.CENTER);
            vB.setPadding(new Insets(20,0,0,0));

            PreView(Data.roomStructure);

        }else{
            if(bdpane!=null){    bdpane.setRight(null);  btnBack.setVisible(false);  }
            try {   food();  } catch (Exception e) {   throw new RuntimeException(e); }
        }

            if(!seat_selected.isEmpty()) btnHandle();
            if(!Data.order_food_item.isEmpty()) totalfood();

    }

    public void PreView(RoomStructure r){
        text='A';grpIndex=0;
        ArrayList<entity.SeatStructure> rSTT= r.getRoomStt().stream().filter(e->!e.getTypeSeat().matches("Space") && !e.getTypeSeat().matches("Other")).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<entity.SeatStructure> space = r.getRoomStt().stream().filter(e->e.getTypeSeat().matches("Space")).collect(Collectors.toCollection(ArrayList::new));
        entity.SeatStructure Other = null;
        List<entity.SeatStructure> L1 = r.getRoomStt().stream().filter(e->e.getTypeSeat().matches("Other")).collect(Collectors.toList());
        if(!L1.isEmpty()){
            Other=L1.get(0);
        }

        GridPane grpane= new GridPane();
        for(int a=0;a<rSTT.size();a++){
            entity.SeatStructure rm=rSTT.get(a);
            for(int i = a==0 ? 0: rSTT.get(a-1).getRow() ; i<rm.getRow();i++) {
                int indexcol=0;
                System.out.println(i);
                for (int j = 0; j < rm.getCol(); j++) {
                    Button btn= new Button("");btn.setPrefWidth(r.getRow()>11?33:40);btn.setPrefHeight(r.getRow()>11?33:40);
                    if(r.getRow()>11){
                        btn.setFont(Font.font(9));
                    }
                    int finalI = i,finalJ = j;
                    long check= space.stream().filter(s->finalI <= s.getRow()-1 &&  finalJ==s.getCol()-1).count(); // check space
                    boolean check2 = Other != null && (finalI <= Other.getRow() - 1 && finalJ <= Other.getCol() - 1); // check other type seat
                    if(check==0){


                        if(checkSeatSelected(String.valueOf(text) + (indexcol+1))){
                            if(!Mctl.seat_selected.isEmpty() && Mctl.seat_selected.contains(String.valueOf(text) + (indexcol+1))   ){
                                System.out.println("check 1");
                                btn.setStyle( "-fx-background-color:"+DD);
                                btn.setDisable(true);
                            }else{
                                System.out.println("check 2");
                                btn.setText("X");
//                                btn.setStyle("-fx-background-color: #F0F0F0");
                                btn.setStyle("-fx-background-color: #757475");
                                btn.setDisable(true);
                            }

                        }else{
                            btn.setText(String.valueOf(text) + (indexcol+1));
                            btn.setStyle("-fx-background-color:"+(check2?Other.getColorSeat():rm.getColorSeat()));
                        }
                        int finalI1 = i,finalJ1 = j;
//          btn action
                        btn.setOnAction(e->{
                                Pair<String,String> typeSchecked=checkTypeSeat(rSTT,finalI1+1);
                                String cl= btn.getStyle().trim().matches("-fx-background-color:"+typeSchecked.getValue())? "-fx-background-color:"+DD : "-fx-background-color:"+typeSchecked.getValue() ;

                                if(typeSchecked.getKey().matches("Normal")){
                                    btn.setStyle(cl);
                                    Data.type_seat_selected="Normal";
                                    addOrderItem("Normal",btn);
                                }else if(typeSchecked.getKey().matches("VIP")){
                                    btn.setStyle(cl);
                                    Data.type_seat_selected="VIP";
                                    addOrderItem("VIP",btn);
                                }else if(typeSchecked.getKey().matches("SWEETBOX")){
                                    Data.type_seat_selected="SWEETBOX";
                                    int index=grpane.getChildren().indexOf(btn);
                                    if(finalJ1 %2==0){
                                        btn.setStyle(cl);
                                        grpane.getChildren().get(index+1).setStyle(cl);
                                    }else{
                                        btn.setStyle(cl);
                                        grpane.getChildren().get(index-1).setStyle(cl);
                                    }
                                    addOrderItem("SWEETBOX",btn);
                                    addOrderItem("SWEETBOX", (Button) (finalJ1%2==0? grpane.getChildren().get(index+1):grpane.getChildren().get(index-1)));
                                }else if(typeSchecked.getKey().matches("GOLDENCLAS")){
                                    btn.setStyle(cl);
                                    Data.type_seat_selected="GOLDENCLAS";
                                    addOrderItem("GOLDENCLAS",btn);
                                }else if(typeSchecked.getKey().matches("LAMOUR")){
                                    btn.setStyle(cl);
                                    Data.type_seat_selected="LAMOUR";
                                    addOrderItem("LAMOUR",btn);
                                }
                                btnHandle();
                            });
                        indexcol++;
                    }else{
                        btn.setStyle("-fx-background-color:none ");
                    }
                    btn.getStyleClass().add("btn-text-fill");
                    grpane.add(btn,j,i);
                    grpIndex++;
                }
                text =  (char) (text + 1);
//                System.out.println(text);
            }
        }

        grpane.setVgap(5);
        grpane.setHgap(5);
        grpane.setAlignment(Pos.CENTER);
        grpane.setPrefSize(1280,720);

        bdpane.setCenter(grpane);
    }
    public  void addOrderItem(String Typeseat , Button btn)  {
        DBcontroller db= new DBcontroller();
        try {    db.getSeatType(); }catch (Exception e) {  }

        seat_type.forEach(dt->{
            if(Typeseat.matches(dt.getName())){
//                System.out.println("check"+dt.getName() +"\n"+Typeseat);
                Double price= (film_selected.getPrice()+dt.getPlus_origin_price()*film_selected.getPrice()/100)/1 ;
                Order od= new Order(
                        btn.getText(),
                        Typeseat, //
                        dt.getId(), //
                        film_selected.getName(),
                        room_selected.getName(),
                        type_room_selected, //
                        price,
                        showtime_time_selected.getDate(),
                        showtime_time_selected.getTime()
                );
//                nếu trong Order_item đã tồn tại ghế ny thì xóa đi ngược lại sẽ thêm vào Order_item
                if(Data.Order_item.stream().anyMatch(e->e.getName_seat().matches(btn.getText()))){
                    Data.Order_item.removeIf(o->o.getName_seat().matches(btn.getText()));
                    if(!Mctl.seat_selected.isEmpty()) {
                        Mctl.seat_selected.removeIf(t->t.matches(btn.getText()));
                    }
                    Data.current_seat_amount--;
                }else{
                    Data.Order_item.add(od);
                    Mctl.seat_selected.add(btn.getText());
                    Data.current_seat_amount++;
                }
            }
        });
    }
    public Pair<String,String> checkTypeSeat(ArrayList<entity.SeatStructure> rSTT, int row){
        for(entity.SeatStructure e:rSTT){
            if(e.getTypeSeat().matches("SWEETBOX")){
                if(e.getRow()==row){ return new Pair<>(e.getTypeSeat(),e.getColorSeat()); }
            }else if(e.getTypeSeat().matches("Normal")){
                if(e.getRow()>=row){ return new Pair<>(e.getTypeSeat(),e.getColorSeat()); }
            }else if(e.getTypeSeat().matches("VIP")){
                if(e.getRow()>=row){ return new Pair<>(e.getTypeSeat(),e.getColorSeat()); }
            }else if(e.getTypeSeat().matches("GOLDENCLAS")){
                return new Pair<>(e.getTypeSeat(),e.getColorSeat());
            }else if(e.getTypeSeat().matches("LAMOUR")){
               return new Pair<>(e.getTypeSeat(),e.getColorSeat());
            }
        };
        return null;
    }
    public boolean checkSeatSelected(String ID){
        if(Data.seat_selected == null){
            return false;
        }else{
            return Data.seat_selected.contains(ID);
        }

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
        if(Data.EditSTS){
            totalCombo=0.0;
        }
        Data.Order_item.forEach(e->{
            totalseat+=e.getPrice();
        });
//        System.out.println(totalseat +" \t"+totalCombo);
        seat.setText(String.valueOf(totalseat));
        total.setText(String.valueOf(totalseat+(totalCombo!=null?totalCombo:0)));
    }
//
    private void setInfoSeat() {
        if(Data.room_selected.getId_type_room().matches("2D")){
            vB.getChildren().clear();
            vB.getChildren().addAll(
                    createBTN("Đang chọn","#AE2A33"),
                    createBTN("Đã đặt","#757475"),
                    createBTN("Thường","#AD9B92"),
                    createBTN("VIP","#934454"),
                    createBTN("SWEETBOX","#DC1A68")
            );
        }else if(Data.room_selected.getId_type_room().matches("3DMAX")){
            vB.getChildren().clear();
            vB.getChildren().addAll(
                    createBTN("Đang chọn","#AE2A33"),
                    createBTN("Đã đặt","#757475"),
                    createBTN("Thường","#AD9B92"),
                    createBTN("VIP","#934454")
            );
        }else if(Data.room_selected.getId_type_room().matches("GOLDENCLAS")){
            vB.getChildren().clear();
            vB.getChildren().addAll(
                    createBTN("Đang chọn","#AE2A33"),
                    createBTN("Đã đặt","#757475"),
                    createBTN("GOLDEN","#764F1E")
            );
        }else if(Data.room_selected.getId_type_room().matches("LAMOUR")){
            vB.getChildren().clear();
            vB.getChildren().addAll(
                    createBTN("Đang chọn","#AE2A33"),
                    createBTN("Đã đặt","#717071"),
                    createBTN("Lammour","#4B3B32"));
        }
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
            Main.editV.showtime();

        }else{
//            bdpane.setCenter(prev);
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
        if(foodBroom || onlyFood){
            Main.editV.CheckOut();
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
//            System.out.println(z.getAmount()+"-------------\t"+z.getId_combo_food());
            totalCombo+=(z.getPrice()*z.getAmount());
        });
        combo.setText(String.valueOf(totalCombo));
        total.setText(String.valueOf(totalseat+totalCombo));
    }
    public void CancelEdit(ActionEvent actionEvent) throws IOException {
        Main.editV.CancalEdit();
    }
}
