package Admin.entity;

import Admin.CTL.CreateFilmCtl;
import Admin.CTL.CreateShowtimeCtl;
import Admin.CTL.R_ST_F_CNM;
import Admin.config.ControllerDB;
import Admin.config.Data;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Optional;

public class Showtime {
    private Integer id,id_film,id_room;
    private String name_film,room_name;
    private Date date;
    private Time time;

    private HBox action= new HBox();
    private Button Edit= new Button("Edit"), Delete= new Button("Delete");
    public static Dialog dal;

    public Showtime(Integer id, Integer id_film, Integer id_room, Date date, Time time) {
        this.id = id;
        this.id_film = id_film;
        this.id_room = id_room;
        this.date = date;
        this.time = time;
    }

    public Showtime(Integer id, Integer id_film, Integer id_room, String name_film, String room_name, Date date, Time time) {
        this.id = id;
        this.id_film = id_film;
        this.id_room = id_room;
        this.name_film = name_film;
        this.room_name = room_name;
        this.date = date;
        this.time = time;

        Edit.setStyle("-fx-text-fill: black");
        Delete.setStyle("-fx-text-fill: black");
        this.action.getChildren().addAll(Edit,Delete);
        this.action.setSpacing(7);
        Edit.setOnAction(e->{
            CreateShowtimeCtl.ShowtimeEdit=this;
            CreateShowtimeCtl.STeditSTT=true;
            Data.id_filmEdit_ST=this.id;
            Data.id_roomEdit_ST=this.id_room;
            try {
                Parent filmXML= FXMLLoader.load(Film.class.getResource("/Admin/FXML/CreateShowtime.fxml"));
                dal= new Dialog<>();dal.getDialogPane().setPrefSize(600,400);
                dal.getDialogPane().setGraphic(filmXML);
                dal.show();
                Window window = dal.getDialogPane().getScene().getWindow();
                window.setOnCloseRequest((a) -> {
                    CreateShowtimeCtl.STeditSTT=false;
                    dal.close();
                });
            }catch (Exception ex) {}
        });
        Delete.setOnAction(e->{
            ControllerDB db= new ControllerDB();
            try {
                Alert al= new Alert(Alert.AlertType.CONFIRMATION);
                al.getDialogPane().getButtonTypes().clear();
                al.getDialogPane().getButtonTypes().addAll(ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> rs= al.showAndWait();
                if(rs.get().equals(ButtonType.YES)){
                    db.deleteShowtime(this.id);
                    R_ST_F_CNM.Showtime_List.removeIf(a->a.id.equals(this.id));
                }else{
                    al.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public Showtime(){}

    public HBox getAction() {
        return action;
    }

    public void setAction(HBox action) {
        this.action = action;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getName_film() {
        return name_film;
    }

    public void setName_film(String name_film) {
        this.name_film = name_film;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_film() {
        return id_film;
    }

    public void setId_film(Integer id_film) {
        this.id_film = id_film;
    }

    public Integer getId_room() {
        return id_room;
    }

    public void setId_room(Integer id_room) {
        this.id_room = id_room;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
