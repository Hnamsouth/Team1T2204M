package Admin.CTL;

import Admin.config.ControllerDB;
import Admin.config.Data;
import Admin.entity.Showtime;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ResourceBundle;

public class CreateShowtimeCtl implements Initializable {

    public ComboBox<Pair<Integer,String>> CbFilm,CbRoom,CbCinemaBase;
    public ComboBox<String> CbFilmStatus,CbCity;
    public JFXTimePicker TimePK;
    public JFXDatePicker DatePK;

    public static Showtime ShowtimeEdit;
    public static boolean STeditSTT = false;

    public static ObservableList<Pair<Integer,String>> listFilm= FXCollections.observableArrayList();
    private ObservableList<Pair<Integer,String>> base = FXCollections.observableArrayList();
    private ObservableList<Pair<Integer,String>> room = FXCollections.observableArrayList();
    ControllerDB db;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        db = new ControllerDB();
//film
        CbFilmStatus.setItems(Data.filmStatus);CbFilmStatus.getSelectionModel().select(0);
        CbFilmStatus.setOnAction(e->{filmSTTchange() ;});
                                        try {     db.getFilm(CbFilmStatus.getValue());                                }catch (Exception e) {throw new RuntimeException(e);}
        CbFilm.setItems(listFilm);//     CbFilm.getSelectionModel().select(0);
//city
        CbCity.setItems(Data.city);CbCity.getSelectionModel().select(0);
        CbCity.setOnAction(this::citychange);   citychange(null);
//base
        CbCinemaBase.setItems(base);CbCinemaBase.getSelectionModel().select(0);
        CbCinemaBase.setOnAction(e->{baseChange();}); baseChange();
//room
        CbRoom.setItems(room);CbRoom.getSelectionModel().select(0);
        if(STeditSTT){
//film edit
            ObservableList<String>  fstt=FXCollections.observableArrayList("Id Film","Now Showing","Coming Soon","Stop Showing");
            CbFilmStatus.setItems(fstt); CbFilmStatus.getSelectionModel().select(0);
            CbFilm.getSelectionModel().select(0);
// room edit
            R_ST_F_CNM.Room_List.forEach(e->{
               if(e.getId().equals(Data.id_roomEdit_ST)){
                   R_ST_F_CNM.CNM_List.forEach(a->{
                      if(a.getId().equals(e.getId_cinema())){
                          CbCity.setValue(a.getCity());
                          CbCinemaBase.setValue(new Pair<>(a.getId(),a.getName()));
                          CbRoom.setValue(new Pair<>(e.getId(),e.getName()));
                      }
                   });
               }
            });
// date and time edit
            DatePK.setValue(ShowtimeEdit.getDate().toLocalDate());
            TimePK.setValue(ShowtimeEdit.getTime().toLocalTime());

        }
    }
    public void filmSTTchange()  {
        try {   db.getFilm(CbFilmStatus.getValue());  CbFilm.setItems(listFilm);                    }catch (Exception e) {throw new RuntimeException(e);}
    }

    private void baseChange() {
        try {     db.getRoom(CbCity.getValue(),CbCinemaBase.getValue().getValue());                 }catch (Exception e) {throw new RuntimeException(e);}
        ObservableList<Pair<Integer,String>> r = FXCollections.observableArrayList();
        R_ST_F_CNM.Room_List.forEach(e->{
                r.add(new Pair<>(e.getId(),e.getName()));
        });
        room=r;
        if(r.isEmpty()){
            CbRoom.setItems(null);
        }else{
            CbRoom.setItems(r);CbRoom.getSelectionModel().select(0);
        }
    }

    private void citychange(ActionEvent actionEvent) {
        ObservableList<Pair<Integer,String>> b = FXCollections.observableArrayList();
        R_ST_F_CNM.CNM_List.forEach(e->{

            if(e.getCity().equals(CbCity.getValue())){
                b.add(new Pair<>(e.getId(),e.getName()));
            }
        });
        base=b;
        CbCinemaBase.getItems().clear();
        CbCinemaBase.setItems(base);CbCinemaBase.getSelectionModel().select(0);
    }


    public void Create(ActionEvent actionEvent) throws SQLException {
        Data.create_showtime= new Showtime(
                STeditSTT?ShowtimeEdit.getId():null,CbFilm.getValue().getKey(),
                CbRoom.getValue().getKey(),
                Date.valueOf(DatePK.getValue()),
                Time.valueOf(TimePK.getValue())
        );
        if(STeditSTT){
            db.updateShowtime();
            Close(null);
        }else{
            db.saveShowtime();
        }
        db.getShowtime(CbCinemaBase.getValue().getValue(),CbCity.getValue());
        R_ST_F_CNM.tableV.setItems(R_ST_F_CNM.Showtime_List);


    }

    public void Close(ActionEvent actionEvent) {
        if(STeditSTT){
            Showtime.dal.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Showtime.dal.close();
            STeditSTT=false;
        }else{
            R_ST_F_CNM.dal.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            R_ST_F_CNM.dal.close();
        }

    }

}
