package CheckOut;

import DBcontroller.Data;
import Main.editV;
import Rooms.Controller.Mctl;
import entity.Film;
import entity.Showtime;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public BorderPane BdPane;
    public Label FilmName   ,Date   ,StartEndTime  ,RoomName;
    public HBox ListSeat= new HBox(),ListCombo= new HBox();;
    public ImageView image;
    public ToggleGroup payment;
    public Label totalBF,discount,totalAT;
    public TextField voucherTxt,couponTxt,promoTxt;
    public RadioButton RBcash,RBatmCard,RBmasterCard,RBmomo,RBzalo;
    public ImageView imgATM,imgMasterCard,imgCash,imgMomo,imgZalo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Film f= Data.film_selected;
        Showtime st= Data.showtime_time_selected;

        FilmName.setText(f.getName());
        Date.setText(st.getDate().toString());
        StartEndTime.setText(st.getTime().toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))+" ~ "+
                st.getTime().toLocalTime().plusMinutes(f.getDuration()).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        RoomName.setText(st.getId_room());
        image.setImage(f.getImg());
        System.out.println(Mctl.TotalSeatCB);

        totalBF.setText(String.valueOf((Mctl.totalseat+Mctl.totalCombo)));

        Data.Order_item.forEach(e->{
            Text txt= new Text(e.getName_seat());
            txt.setFont(Font.font("",FontWeight.BOLD,22));
            ListSeat.getChildren().add(txt);
        });

        Data.order_food_item.forEach(e->{
            Text txt= new Text(e.getId_combo_food()+" x"+e.getAmount()+", ");
            txt.setFont(Font.font("",18));
            ListCombo.getChildren().add(txt);
        });
    }


    public void submit(ActionEvent actionEvent) throws IOException {
        if(RBcash.isSelected()){
            Main.editV.PrintInvoices();
        }else if(RBatmCard.isSelected()){

        }else if(RBmasterCard.isSelected()){

        }else if(RBmomo.isSelected()){

        }else{
//            RBzalo
        }
    }

    public void addVoucher(ActionEvent actionEvent) {
        voucherTxt.getText();
    }

    public void addCoupon(ActionEvent actionEvent) {
        couponTxt.getText();
    }

    public void addPromoCode(ActionEvent actionEvent) {
        promoTxt.getText();
    }


    public void toListFilm(ActionEvent actionEvent) throws IOException {
        Data.setValueEmpty();
        editV.ListFlim();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        editV.room();
    }
}
