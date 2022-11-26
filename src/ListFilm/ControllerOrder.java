package ListFilm;


import DBcontroller.*;
import Rooms.Controller.Mctl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerOrder implements Initializable {
    public static class orderEdit{
        private Button Film=new Button("Edit Film"),Date_Time=new Button("Edit Date & Time"),Seat=new Button("Edit Seat");

        public Button getFilm() {
            return Film;
        }
        public void setFilm(Button film) {
            Film = film;
        }
        public Button getSeat() {
            return Seat;
        }
        public void setSeat(Button seat) {
            Seat = seat;
        }

        public Button getDate_Time() {
            return Date_Time;
        }

        public void setDate_Time(Button date_Time) {
            Date_Time = date_Time;
        }

        public orderEdit() {
            Film.setStyle("-fx-text-fill: black");
            Date_Time.setStyle("-fx-text-fill: black");
            Seat.setStyle("-fx-text-fill: black");

            Film.setOnAction(e->{
//                set value : null
                try {
                    Data.EditSTS = true;
                    Main.editV.ListFlim();
                    db.checkOrderStatus(orderFD.get(0).getId());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            Date_Time.setOnAction(e->{
//              set value for: Data.film_selected
                try {
                    db.checkOrderStatus(orderFD.get(0).getId());
                    db.GetFilmEditDate(orderFD.get(0).getId());
//              go to showtime
                    Data.EditSTS = true;
                    Main.editV.showtime();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            Seat.setOnAction(e->{
//              set value for:  Data.film_selected, showtime_time_selected;
                try {
                    db.checkOrderStatus(orderFD.get(0).getId());
                    db.GetFilmEditDate(orderFD.get(0).getId());
                    db.GetShowtimeEditSeat(orderFD.get(0).getId());
                    db.OrderSeatSelected(orderFD.get(0).getId());
//              goto room;
                    Data.EditSTS = true;
                    Main.editV.room();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    public static class order{
        private Integer id;private HBox Action= new HBox();

        Button Edit=new Button("Edit"),Delete=new Button("Delete");

        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public HBox getAction() {
            return Action;
        }
        public void setAction(HBox action) {
            Action = action;
        }

        public  order(Integer id){
            this.id = id;
            Edit.setStyle("-fx-text-fill: black");
            Delete.setStyle("-fx-text-fill: black");
            Action.getChildren().add(Edit);
            Action.getChildren().add(Delete);
            Action.paddingProperty().setValue(new Insets(0,0,0,65));
            Action.setSpacing(15);

            Edit.setOnAction(e->{
                if(     orderinfoFD.isEmpty()){
                    orderinfoFD.add(0,new orderEdit());
                }else{
                    orderinfoFD.set(0,new orderEdit());}
            });

            Delete.setOnAction(e->{
                try {
                    if(db.deleteOrder(id)){
                        showalert("Order deleted successfully");
                        if(!orderFD.isEmpty()){
                            orderFD.remove(0);
                        }
                        if(!orderinfoFD.isEmpty()){
                            orderinfoFD.remove(0);
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
    static DBcontroller db;
    public TextField searchOrderText;
//
    public TableView<order> Order;
    public TableColumn<order, Integer> colOrderID;
    public TableColumn<order,HBox> colOrderAction;
//
    public TableView<orderEdit> InfoOrder;
    public TableColumn<orderEdit,Button> colFilm,colDatetime,colSeat;

    public static ObservableList<order> orderFD=FXCollections.observableArrayList();
    public static ObservableList<orderEdit> orderinfoFD=FXCollections.observableArrayList();

    public static  boolean deletests=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colOrderID.setCellValueFactory(new PropertyValueFactory<> ("id"));
        colOrderAction.setCellValueFactory(new PropertyValueFactory<> ("Action"));

        colFilm.setCellValueFactory(new PropertyValueFactory<> ("Film"));
        colDatetime.setCellValueFactory(new PropertyValueFactory<orderEdit,Button> ("Date_Time"));
        colSeat.setCellValueFactory(new PropertyValueFactory<> ("Seat"));

        Order.setItems(orderFD);
        InfoOrder.setItems(orderinfoFD);
        db= new DBcontroller();
        if(deletests){
            setempty();
        }
    }

    public void searchOrder(ActionEvent actionEvent) throws SQLException {
        if(!searchOrderText.getText().isEmpty()){

            if(db.getOrderid(Integer.valueOf(searchOrderText.getText()))){
                Data.Id_Order=Integer.valueOf(searchOrderText.getText());
            }else{
                showalert("Over time Edit");
            }
        }else{
            showalert("Please Enter Id");
        }

    }

    public void setempty(){
        Order.getItems().clear();
        InfoOrder.getItems().clear();
        searchOrderText.setText("");
        deletests=false;
    }

    public static void showalert(String cnt){
        Alert al= new Alert(Alert.AlertType.INFORMATION);
        al.setContentText(cnt);
        al.getButtonTypes().add(ButtonType.CLOSE);
        al.show();
    }
}
