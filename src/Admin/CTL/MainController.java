package Admin.CTL;

import Admin.config.Connector;
import Admin.config.Data;
import Admin.entity.Cinema;
import Admin.config.ControllerDB;
import Admin.entity.TotalCinema;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static Admin.CTL.R_ST_F_CNM.typeAction;

public class MainController implements Initializable {
    public BorderPane BdPane;
    public Label accPermission;
    public ListView<String> Lview;
    public Button btnFilm,  btnShowtime,   btnRoom ,    btnFood,    btnVoucher, btnCinema,  btnPersonnel,   btnManager;
    public TextField txtSearch;
    public static String permission="admin";
    public BorderPane BdPaneAcc;
    public AreaChart Achart;
    public PieChart pieChart;
    public ComboBox<String> City;
    public Label date;
    ControllerDB db;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         db= new ControllerDB();
//        if(db.CheckAcc())
        try {  db.getCinema();    } catch (SQLException e) {   throw new RuntimeException(e);   }
        if(!permission.matches("admin")){
            btnCinema.setVisible(false);
            btnPersonnel.setVisible(false);
            btnManager.setVisible(false);
        }
        City.setItems(Data.city);
        City.getSelectionModel().select(0);
        try {     db.getTotalCinema(City.getValue()); } catch (SQLException e) {   throw new RuntimeException(e);   }

        pieChart.setData(Data.TotalCinemaInWeek);

        City.setOnAction(e->{
            try {     db.getTotalCinema(City.getValue()); } catch (SQLException a) {   throw new RuntimeException(a);   }
            pieChart.getData().clear();
            pieChart.setData(Data.TotalCinemaInWeek);
            setXYChartSeries(Data.TotalCinema_List.get(0),Data.TotalCinema_List.get(0).get(0).getCinemaName());
            CmnTotalSelected();
        });
        if(!Data.TotalCinema_List.isEmpty()){
            setXYChartSeries(Data.TotalCinema_List.get(0),Data.TotalCinema_List.get(0).get(0).getCinemaName());

        }

        CmnTotalSelected();
        LocalDate d=LocalDate.now();
        date.setText(d.getMonth().name().substring(0,3)+" "+d.getDayOfMonth()+" - "+d.minusDays(7).getMonth().name().substring(0,3)+" "+d.minusDays(7).getDayOfMonth());
    }

    public void CmnTotalSelected() {
        pieChart.getData().forEach(a->{
            a.getNode().setOnMouseClicked(k->{
                for(ArrayList<TotalCinema> i:Data.TotalCinema_List){
                    if(i.get(0).getCinemaName().matches(a.getName())){
                        setXYChartSeries(i,a.getName());
                        break;
                    }
                }
            });
        });
    }

    public void setXYChartSeries(ArrayList<TotalCinema> i,String n){
        ObservableList<XYChart.Series> ob= FXCollections.observableArrayList();
        XYChart.Series sr= new XYChart.Series();
        i.forEach(e->{
            LocalDate d=e.getDate().toLocalDate();
            sr.getData().add(new XYChart.Data(d.getMonth().name().substring(0,3)+"-"+d.getDayOfMonth(),e.getTotal()));
        });
        ob.add(sr);
        Achart.setTitle(n);
        Achart.setData(ob);
    }

    public void Film(ActionEvent actionEvent) throws IOException {
        typeAction="Film";
        View.Show();
    }

    public void Showtime(ActionEvent actionEvent) throws IOException  {
        typeAction="Showtime";
        View.Show();
    }
    public void Room(ActionEvent actionEvent) throws IOException  {
        typeAction="Room";
//        get data room
        View.Show();
    }
    public void Cinema(ActionEvent actionEvent) throws IOException  {
        typeAction="Cinema";
        View.Show();
    }
    public void Food(ActionEvent actionEvent) throws IOException  {
    }
    public void Voucher(ActionEvent actionEvent) {
    }
    public void Account(ActionEvent actionEvent) {
    }
    public void SignOut(ActionEvent actionEvent) throws IOException {
        View.SignIn();
    }
    public void Create(ActionEvent actionEvent) {

    }
    public void ReadEditDelete(ActionEvent actionEvent) {
    }
    public void home(ActionEvent actionEvent) {
    }



}
