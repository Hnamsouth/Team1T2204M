package Main;

import DBcontroller.Data;
import ListFilm.ControllerOrder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;
import java.io.IOException;

public class editV {

    public static void ListFlim() throws IOException {
        Parent root = FXMLLoader.load(editV.class.getResource("/ListFilm/ShowLF.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(editV.class.getResource("RootStyle.css").toExternalForm());
        Main.Mstage.setScene(sc);
    }

    public static void showtime() throws IOException {
        Parent root = FXMLLoader.load(editV.class.getResource("/Showtime/Showtime.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(editV.class.getResource("RootStyle.css").toExternalForm());
        Main.Mstage.setScene(sc);
    }

    public static void room() throws IOException {
        Parent root = FXMLLoader.load(editV.class.getResource("/Rooms/B1.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(editV.class.getResource("RootStyle.css").toExternalForm());
        Main.Mstage.setScene(sc);
    }
    public static void PrintInvoices() throws IOException {
        Parent root = FXMLLoader.load(editV.class.getResource("/PrintInvoices/PrintInvoices.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(editV.class.getResource("RootStyle.css").toExternalForm());
        Main.Mstage.setScene(sc);
    }
    public static void PDFcreate() throws IOException {
        Parent root = FXMLLoader.load(editV.class.getResource("/PDFexport/PrintOrder.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(editV.class.getResource("RootStyle.css").toExternalForm());
        Main.Mstage.setScene(sc);
    }

    public static void CancalEdit() throws IOException {
        ControllerOrder.orderFD.clear();
        ControllerOrder.orderinfoFD.clear();
        Data.setValueEmpty();
        Data.EditSTS=false;
        ListFlim();
    }
    public static void CheckOut() throws IOException {
        Parent root = FXMLLoader.load(editV.class.getResource("/CheckOut/CheckOut.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(editV.class.getResource("RootStyle.css").toExternalForm());
        Main.Mstage.setScene(sc);
    }

}
