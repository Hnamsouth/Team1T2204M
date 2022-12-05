package Showtime;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import entity.Film;
import entity.Showtime;
import entity.room;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    public VBox vbox,vboxCenter;
    public Button CancelEdit;
    GridPane gp;
    public static boolean fromlistfilm=false;
    DBcontroller db;
    public void toListFlim(ActionEvent actionEvent) throws IOException {
        grpaneindex=0;
        Data.showtime_time_selected=null;
        Data.order_seat_selected=null;
        Main.editV.ListFlim();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(Data.Order_item == null?"false":Data.Order_item.size());
         db= new DBcontroller();
        LocalDate daten= LocalDate.now();
        if(Data.EditSTS){
            CancelEdit.setVisible(true);
        }
        try {
            if( Data.showtime_film_selected==null || fromlistfilm){
                db.getShowtime();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        vbox.setAlignment(Pos.CENTER);
        try {
            showdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(Data.showtime_time_selected!=null){
            handleButtonAction(null,Data.showtime_time_selected.getDate().toString(),false);
        }else{
            handleButtonAction(null,LocalDate.now().toString(),true);
        }
//        showtime();
    }

    private static int grpaneindex=0;
    private void showdate() throws Exception{
         gp = new GridPane();
        int a=0;
        for(int i=0;i<2;i++){
            for(int j=0;j<15;j++){
                LocalDate date=LocalDate.now().plusDays(a);

                Button btn= new Button();
                btn.setPrefWidth(81.0);btn.setPrefHeight(50.0);btn.setAlignment(Pos.CENTER);
                Label label = new Label();
                label.setPrefWidth(65.0);label.setPrefHeight(39.0); label.setAlignment(Pos.CENTER); label.setFont(Font.font(31));
                label.setText(String.valueOf(date.getDayOfMonth()));

                Text text= new Text(date.getMonth().getValue()+" "+date.getDayOfWeek().toString().substring(0,3));
                text.setFont(Font.font(11));text.setWrappingWidth(27);
                label.setGraphic(text); label.getStyleClass().add("text-date");text.setFill(Color.BLACK);
                btn.setGraphic(label);
                btn.getStyleClass().add("btn-date");

                int finalA = a;
                btn.setOnAction(e->{
                    DBcontroller DB= new DBcontroller();
                    int d=date.compareTo(LocalDate.now());
                    if(d==0){
                        try {
                            db.getShowtime();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }else{
                        Data.showtime_film_selected =  db.showtimeFilmSelected(date.toString()); //
                    }
                    Data.showtime_time_selected=null;
                    vboxCenter.getChildren().clear();
                    grpaneindex= finalA;
                   this.handleButtonAction(e,date.toString(),false);
                });

                gp.add(btn,j,i,1,1);
                a++;
            }
        }
        gp.setHgap(4.3);
        gp.setVgap(4.3);
        gp.setPadding(new Insets(5,0,5,3));

        vbox.getChildren().add(gp);
    }
    private void handleButtonAction(ActionEvent event, String date,boolean fromInit) {
        Text datetxt= new Text(date);
        datetxt.setFont(Font.font(25));datetxt.setFill(Color.WHITE);
        vboxCenter.getChildren().add(datetxt);
        vboxCenter.setMargin(datetxt,new Insets(10,0,10,10));

        if(fromInit){
            try {
                db.getShowtime();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } // current date
        }

        for(int i=0;i<gp.getChildren().size();i++){
            if(i==grpaneindex){
                gp.getChildren().get(i).setStyle("-fx-background-color: white");
            }else{
                gp.getChildren().get(i).setStyle("-fx-background-color: rgba(227, 225, 225, 0.66)");
            }
            if(Data.showtime_time_selected!=null && i==grpaneindex){
                gp.getChildren().get(i).setStyle("-fx-background-color:  #DE9F54");
            }
        }
        showtime2();
    }
    private void showtime2(){
        ArrayList<Showtime> st= Data.showtime_film_selected;
        ArrayList<room> typer=Data.room_of_film;
        ObservableList<String> trm= Data.type_room;

        typer.forEach(e->{

            AnchorPane anp= new AnchorPane();
            Text text= new Text(e.getName());
            text.setFont(Font.font(19));text.setFill(Color.WHITE);
            anp.getChildren().add(text);
            anp.setTopAnchor(text,10.0);
            anp.setLeftAnchor(text,10.0);
            GridPane gp= new GridPane();

            ArrayList<Showtime> narr2 = st.stream().filter(k -> k.getId_room().equals(e.getId())).collect(Collectors.toCollection(ArrayList::new));
            int a=0;
            for(int i=0;i < narr2.size() / 10 + (narr2.size() % 10 != 0 ? 1 : 0) ;i++){
                for(int j = 0; j<( Math.min(narr2.size(), 10)); j++){
                    int x=a;
                    Format fm = new SimpleDateFormat("HH:mm a");
                    Button btn= new Button();

                    btn.setText(fm.format(narr2.get(a).getTime()));
                    btn.setPrefWidth(120.0);btn.setPrefHeight(50.0);btn.setAlignment(Pos.CENTER);
                    btn.getStyleClass().add("btn-date");btn.setFont(Font.font(18));
                    if(Data.showtime_time_selected!=null){
                        if(fm.format(Data.showtime_time_selected.getTime()).equals(fm.format(narr2.get(a).getTime()))
                                && Data.type_room_selected.matches(e.getName())){
                            btn.setStyle("-fx-background-color:  #DE9F54");
                        }
                    }

                    btn.setOnAction(bt->{
                        Data.showtime_time_selected=narr2.get(x);
                        Data.type_room_selected=e.getName();
                        fromlistfilm=false;
                    });
                    gp.add(btn,j,i,1,1);
                    a++;
                }
            }
            gp.setHgap(4.3);
            gp.setVgap(4.3);
            gp.setPadding(new Insets(5,0,5,3));

            vboxCenter.getChildren().add(anp);
            vboxCenter.getChildren().add(gp);
        });
    }

    public void toRoom(ActionEvent actionEvent) {
        if(Data.showtime_time_selected!=null){
            try {
                db.getRoomStructure();

                Main.editV.room();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }else{
            Alert al= new Alert(Alert.AlertType.WARNING);
            al.setContentText("Please select time ..!");
            al.getButtonTypes().add(ButtonType.CLOSE);
            al.show();
        }

    }

    public void CancelEdit(ActionEvent actionEvent) throws IOException {
        Main.editV.CancalEdit();
    }
}
