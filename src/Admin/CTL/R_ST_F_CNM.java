package Admin.CTL;

import Admin.config.ControllerDB;
import Admin.config.Data;
import Admin.entity.Cinema;
import Admin.entity.Film;
import Admin.entity.Room;
import Admin.entity.Showtime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;
import static javafx.scene.control.TableView.UNCONSTRAINED_RESIZE_POLICY;

public class R_ST_F_CNM implements Initializable {
    public ComboBox<String> CbCity;
    public ComboBox<String> CbBase;
    public TextField search;
    public static TableView tableV;
    public BorderPane BdPane;

    public static String typeAction="Cinema";
    public static ObservableList<Cinema> CNM_List= FXCollections.observableArrayList();
    private ObservableList<Cinema> CNMCity= FXCollections.observableArrayList();
    public static ObservableList<Film> Film_List = FXCollections.observableArrayList();
    public static ObservableList<Showtime> Showtime_List = FXCollections.observableArrayList();
    public static ObservableList<Room> Room_List = FXCollections.observableArrayList();
    public ComboBox<String> CbFilmStatus;
    public ComboBox<String> CbRoom;
    ControllerDB db;

    public R_ST_F_CNM(){};
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         db= new ControllerDB();

        CbCity.setItems(Data.city);CbCity.getSelectionModel().select(0);      CbRoom.setItems(Data.room_showtime);
        cityChange(null);

        CbCity.setOnAction(this::cityChange);

        CbRoom.setOnAction(e->{

        });

        if(typeAction.matches("Cinema")){                                            // Cinema
             tableV= new TableView<Cinema>();
            CbBase.setVisible(false);
             setCinemaTB();

        }else if(typeAction.matches("Film")){                                       // Film

            CbCity.setVisible(false);    CbBase.setVisible(false);    CbFilmStatus.setVisible(true);
            CbFilmStatus.setItems(Data.filmStatus); CbFilmStatus.getSelectionModel().select(0);
            CbFilmStatus.setOnAction(e->{ try {    db.getFilm(CbFilmStatus.getValue());     tableV.setItems(Film_List);     }catch (Exception a) {throw new RuntimeException(a);} });
                                          try {     db.getFilm(CbFilmStatus.getValue());                                    }catch (Exception e) {throw new RuntimeException(e);}
            tableV= new TableView<Film>();setFilmTB();
        }else if(typeAction.matches("Room")){                                       // Room
            try {     db.getRoom(CbCity.getValue(),  CbBase.getValue());      db.getRoomStructure();        } catch (SQLException e) { e.printStackTrace();}
            tableV= new TableView<Room>();      setRoomTB();
        }else if(typeAction.matches("Showtime")){
            CbRoom.setVisible(true);
            try {     db.getShowtime(CbBase.getValue(),     CbCity.getValue());                             } catch (SQLException e) { e.printStackTrace();}// Showtime
            tableV= new TableView<Showtime>();  setShowtimeTB();
        }
        tableV.styleProperty().setValue("-fx-background-color:#232323");

    }
    public static String cityD,baseD;
    public void baseC(ActionEvent actionEvent) {
        baseD= CbBase.getValue();
        if(typeAction.matches("Room")){
            try {     db.getRoom(CbCity.getValue(),  CbBase.getValue());      db.getRoomStructure();        } catch (SQLException a) { a.printStackTrace();}
            tableV.setItems(Room_List);
        }else if(typeAction.matches("Showtime")){
            try {     db.getShowtime(CbBase.getValue(),     CbCity.getValue());                             } catch (SQLException e) { e.printStackTrace();}// Showtime
            tableV.setItems(Showtime_List);
        }

    }
    @FXML
    private void cityChange(ActionEvent actionEvent) {
        ObservableList<String> base= FXCollections.observableArrayList();
        CNM_List.forEach(e->{
            if(e.getCity().equals(CbCity.getValue())){
                base.add(e.getName());
            }
        });
        CbBase.setItems(base);CbBase.getSelectionModel().select(0);
        cityD=CbCity.getValue();
        if(typeAction.matches("Cinema")){
            CNMCity=CNM_List.stream().filter(e->e.getCity().equals(CbCity.getValue())).collect(Collectors.toCollection(FXCollections::observableArrayList));
            if(tableV!=null){
                tableV.setItems(CNMCity);
            }
        }
    }
    public  static Dialog<ButtonType> dal;

    public void Create(ActionEvent actionEvent) throws IOException {
        if(typeAction.matches("Cinema")){
//            dialog
            Parent filmXML= FXMLLoader.load(R_ST_F_CNM.class.getResource("/Admin/FXML/CreateCinema.fxml"));
            dal= new Dialog<>();dal.getDialogPane().setPrefSize(600,400);
            dal.getDialogPane().setGraphic(filmXML);
            dal.show();
//
            closeDialog();
        }else if(typeAction.matches("Film")){
            Parent filmXML= FXMLLoader.load(R_ST_F_CNM.class.getResource("/Admin/FXML/CreateFilm.fxml"));
             dal= new Dialog<>();dal.getDialogPane().setPrefSize(800,600);
            dal.getDialogPane().setGraphic(filmXML);
            dal.show();
//
            closeDialog();
        }else if(typeAction.matches("Room")){
            View.CreateRoom();
        }else if(typeAction.matches("Showtime")){
            Parent filmXML= FXMLLoader.load(R_ST_F_CNM.class.getResource("/Admin/FXML/CreateShowtime.fxml"));
            dal= new Dialog<>();dal.getDialogPane().setPrefSize(650,400);
            dal.getDialogPane().setGraphic(filmXML);
            dal.show();
//
            closeDialog();
        }
    }
    public static void closeDialog(){
        Window window = dal.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            dal.close();
        });
    }
    public void setCinemaTB(){
        TableColumn<Cinema,String> colName= new TableColumn<Cinema, String>("Name");
        TableColumn<Cinema,String> colAddress= new TableColumn<Cinema, String>("Address");
        TableColumn<Cinema,String> colManager= new TableColumn<Cinema, String>("ID Manager");
        TableColumn<Cinema,Button> colAction= new TableColumn<Cinema, Button>("Action");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colManager.setCellValueFactory(new PropertyValueFactory<>("id_manager"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("Edit"));
        tableV.getColumns().addAll(colName,colAction,colManager,colAddress);
        tableV.setItems(CNMCity);

        tableV.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        BdPane.setCenter(tableV);
    }
    public void setFilmTB(){
        TableColumn<Film,Integer> colID= new TableColumn<Film, Integer>("ID");
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Film, Button> colImage= new TableColumn<Film, Button>("Image");
        colImage.setCellValueFactory(new PropertyValueFactory<>("previewIMG"));
        TableColumn<Film,String> colName= new TableColumn<Film, String>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Film,HBox> colAction= new TableColumn<Film, HBox>("colAction");
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        TableColumn<Film,Double> colPrice= new TableColumn<Film, Double>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory< >("price"));
        TableColumn<Film,Integer> colDuration= new TableColumn<Film, Integer>("Duration");
        colDuration.setCellValueFactory(new PropertyValueFactory<>("running_time"));
        TableColumn<Film,String> colDirector= new TableColumn<Film, String>("Director");
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        TableColumn<Film,String> colType= new TableColumn<Film, String>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("genre"));
        TableColumn<Film,String> colCasts= new TableColumn<Film, String>("The Cast");
        colCasts.setCellValueFactory(new PropertyValueFactory<>("cast"));
        TableColumn<Film, Date> colPremiere= new TableColumn<Film, Date>("Premiere");
        colPremiere.setCellValueFactory(new PropertyValueFactory< >("release_date"));
        TableColumn<Film,String> colStatus= new TableColumn<Film, String>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory< >("status"));
        TableColumn<Film,String> colLanguage= new TableColumn<Film, String>("Language");
        colLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));



        tableV.getColumns().addAll(colID,colImage,colName,colPrice,colAction,colPremiere,colDuration,colType,colLanguage,colDirector,colCasts);
        tableV.setItems(Film_List);
        tableV.setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);
        BdPane.setCenter(tableV);
    }
    public void setRoomTB(){
        TableColumn<Room,Integer> colID= new TableColumn<Room, Integer>("id");
        TableColumn<Room,String> colName= new TableColumn<Room, String>("Name");
        TableColumn<Room,String> colType= new TableColumn<Room, String>("Type");
        TableColumn<Room,Integer> colCinema= new TableColumn<Room, Integer>("ID Cinema");
        TableColumn<Room, HBox> colAction= new TableColumn<Room, HBox>("Action");

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("id_type_room"));
        colCinema.setCellValueFactory(new PropertyValueFactory<>("id_cinema"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        tableV.getColumns().addAll(colID,colName,colType,colCinema,colAction);
        tableV.setItems(Room_List);

        tableV.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        BdPane.setCenter(tableV);
    }
    public void setShowtimeTB(){
        TableColumn<Showtime,Integer> colID= new TableColumn<Showtime, Integer>("ID");
        TableColumn<Showtime,String> colFilm= new TableColumn<Showtime, String>("Film");
        TableColumn<Showtime,String> colRoom= new TableColumn<Showtime, String>("Room");
        TableColumn<Showtime,Date> colDate= new TableColumn<Showtime, Date>("Date");
        TableColumn<Showtime, Time> colTime= new TableColumn<Showtime, Time>("Time");
        TableColumn<Showtime, HBox> colAction= new TableColumn<Showtime, HBox>("Action");

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFilm.setCellValueFactory(new PropertyValueFactory<>("name_film"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        tableV.getColumns().addAll(colID,colFilm,colRoom,colDate,colTime,colAction);
        tableV.setItems(Showtime_List);
        tableV.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        BdPane.setCenter(tableV);
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        View.Home();
    }



}
