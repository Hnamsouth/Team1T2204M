package ListFilm;

import DBcontroller.DBcontroller;
import entity.Film;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import DBcontroller.Data;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static Showtime.Controller.fromlistfilm;

public class Controller implements Initializable {
    public ListView<HashMap<Integer,String>> listv;
    public TextField tfields;
//    @../../../img/bp2_official_poster_1_.jpg

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        get film and add to HBox
        DBcontroller db= new DBcontroller();
        try {   db.getAllFilm();} catch (Exception e) {  throw new RuntimeException(e);};
        showfilm(Data.list_film);
        handleListv();
        searfilm();
        System.out.println(Data.Order_item == null?"false":Data.Order_item.size());
    }

    private void searfilm() {
        tfields.textProperty().addListener((ob,ovl,nvl)->{
            ArrayList<Film> arr= Data.list_film.stream().filter(e->
                    e.getName().trim().toLowerCase().contains(nvl.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            showfilm(arr);
            listv.refresh();
        });
    }

    private void showfilm(ArrayList<Film>  lf) {
        if(lf != null){
            ObservableList<HashMap<Integer,String>> obl= FXCollections.observableArrayList();
            ArrayList<Image> arrimg = new ArrayList<Image>();
            lf.forEach(e->{
                HashMap<Integer,String> hm= new HashMap<>();
                hm.put(e.getId(),e.getName());
                arrimg.add(e.getImg());
                obl.add(hm);
            });
            listv.setItems(obl);
            listv.setCellFactory(lv ->new ListCell<HashMap<Integer,String>>(){
                private ImageView img = new ImageView();
                @Override
                public void updateItem(HashMap<Integer,String> name ,boolean empty){
                    super.updateItem(name,empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    }else{
                        for(int i=0;i<obl.size();i++){
                            if(obl.get(i).equals(name)){
                                img.setImage(arrimg.get(i));
                                img.setFitHeight(100);
                                img.setFitWidth(100);
                                setText(obl.get(i).values().toString());
                                setGraphic(img);
                            }
                        }
                    }
                }
            });
        }
    }

    public void handleListv(){
        listv.setOnMouseClicked(e->{
            if(!listv.getSelectionModel().getSelectedItems().isEmpty()){

                ObservableList<HashMap<Integer,String>> obl=listv.getSelectionModel().getSelectedItems();

                Data.list_film.forEach(lf->{
                    if(lf.getId().equals(obl.get(0).keySet().hashCode())){
                        Data.film_selected=lf;
                        fromlistfilm=true;
                        try {
                            Main.editV.showtime();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });
    }

    public void flimOfMonth(ActionEvent actionEvent) {
        DBcontroller db= new DBcontroller();
        try {   db.getAllFilmOfMonth();} catch (Exception e) {  throw new RuntimeException(e);};
        showfilm(Data.list_film);
        listv.refresh();
    }


    public void flimInDay(ActionEvent actionEvent) {
        DBcontroller db= new DBcontroller();
        try {   db.getAllFilm();} catch (Exception e) {  throw new RuntimeException(e);};
        showfilm(Data.list_film);
    }
}
