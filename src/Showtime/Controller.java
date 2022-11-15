package Showtime;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import entity.Film;
import entity.Order;
import entity.Showtime;
import entity.room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import java.util.stream.Stream;

public class Controller implements Initializable {
    public VBox vbox,vboxCenter;
    public static boolean fromlistfilm=false;

    public void toListFlim(ActionEvent actionEvent) throws IOException {
        Main.editV.ListFlim();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(Data.Order_item == null?"false":Data.Order_item.size());
        DBcontroller dbc= new DBcontroller();
        LocalDate daten= LocalDate.now();
        try {
            if( Data.showtime_film_selected==null || fromlistfilm){
                dbc.getShowtime();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        vbox.setAlignment(Pos.CENTER);
        showdate();
        showtime();
    }

    private void showdate() {
        GridPane gp= new GridPane();
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
                label.setGraphic(text); label.getStyleClass().add("text-date");text.setFill(Color.WHITE);
                btn.setGraphic(label);
                btn.getStyleClass().add("btn-date");

                btn.setOnAction(e->{
                    DBcontroller DB= new DBcontroller();
                    Data.showtime_film_selected =  DB.showtimeFilmSelected(date.toString());
                    vboxCenter.getChildren().clear();
                    showtime();
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

    private void showtime() {
        ArrayList<Showtime> st= Data.showtime_film_selected;
        Film f= Data.film_selected;
        System.out.println(f.getName());

        ArrayList<room> typer=Data.room_of_film;
        ObservableList<String> trm= Data.type_room;

        trm.forEach(tr->{
            System.out.println("-----------------"+tr);

//            loc type film
            ArrayList<room>asd= typer.stream().filter(tpr->tpr.getId_type_room().equals(tr)).collect(Collectors.toCollection(ArrayList::new));
            if(!asd.isEmpty()){
                AnchorPane anp= new AnchorPane();
                Text text= new Text(tr);
                text.setFont(Font.font(19));
                anp.getChildren().add(text);
                anp.setTopAnchor(text,10.0);
                anp.setLeftAnchor(text,10.0);
                GridPane gp= new GridPane();

                ObservableList<ArrayList<Showtime>> obl= FXCollections.observableArrayList();
                ArrayList<Showtime> narr= new ArrayList<>();
                asd.forEach(e->{
    //              collect all room have same type_room
                    ArrayList<Showtime> narr2 = st.stream().filter(k -> e.getId_room().matches(k.getId_room())).collect(Collectors.toCollection(ArrayList::new));
                    narr.addAll(narr2);
                });

                int a=0;
                for(int i=0;i < narr.size() / 10 + (narr.size() % 10 != 0 ? 1 : 0) ;i++){
                    for(int j = 0; j<( Math.min(narr.size(), 10)); j++){
                        int x=a;
                        Format fm = new SimpleDateFormat("HH:mm a");
                        Button btn= new Button();
                        btn.setText(fm.format(narr.get(a).getTime()));
                        btn.setPrefWidth(120.0);btn.setPrefHeight(50.0);btn.setAlignment(Pos.CENTER);
                        btn.getStyleClass().add("btn-date");btn.setFont(Font.font(18));

                        btn.setOnAction(bt->{
                            Data.showtime_time_selected=narr.get(x);
                            Data.type_room_selected=tr;
                            fromlistfilm=false;

                            try {
                                Main.editV.room();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

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
            };
        });
    }

}
