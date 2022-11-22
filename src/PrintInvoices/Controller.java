package PrintInvoices;

import DBcontroller.Data;
import Main.*;
import entity.Order;
import entity.OrderFoodItem;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TableView<Order> tableV;
    public TableColumn<Order ,String> colSeat,coltypeSeat,colFilm,colRoomname;
    public TableColumn<Order ,Double> colPrice;
    public TableColumn<Order , Date> colDate;
    public TableColumn<Order , Time> colTime;
    public TableView<OrderFoodItem> tableFood;
    public TableColumn<OrderFoodItem,String> colFoodName;
    public TableColumn<OrderFoodItem,Integer> colFoodAmount;
    public TableColumn<OrderFoodItem,Double> colFoodPrice;
    public Button CancelEdit;
    public BorderPane BdPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Data.EditSTS){
            CancelEdit.setVisible(true);
        }
        colSeat.setCellValueFactory(new PropertyValueFactory<>("name_seat"));
        coltypeSeat.setCellValueFactory(new PropertyValueFactory<>("name_type_seat"));
        colFilm.setCellValueFactory(new PropertyValueFactory<>("_name_film"));
        colRoomname.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        tableV.setItems(Data.Order_item);

        colFoodName.setCellValueFactory(new PropertyValueFactory<>("id_combo_food"));
        colFoodAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colFoodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableFood.setItems(Data.order_food_item);


    }

    public void toListFilm(ActionEvent actionEvent) throws IOException {
        WritableImage snapshot = BdPane.snapshot(new SnapshotParameters(), null);
        File f= new File("F:\\Github\\Team1\\Team1T2204M\\src\\PDFexport\\demo.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null),"png",f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Data.setValueEmpty();
        editV.ListFlim();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        editV.room();
    }

    public void print(ActionEvent actionEvent) throws IOException {
//            SAVE DATA into database
//        try {
//            DBcontroller db=new DBcontroller();
//            if(Data.EditSTS){
//                db.updateOrder(Data.Id_Order);
//            }else{
//                db.addOrderItem();
//                db.addFood();
//            }
//
//        }catch (Exception e) {
//
//        }
//        editV.CancalEdit();
        editV.PDFcreate();

    }

    public void CancelEdit(ActionEvent actionEvent) throws IOException {
        editV.CancalEdit();
    }
}
