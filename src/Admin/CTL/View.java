package Admin.CTL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;


import static Admin.Main.Main.MainStage;


public class View {
    public static void SignIn() throws IOException {
        Parent root = FXMLLoader.load(View.class.getResource("/Admin/SignIn/SignIn.fxml"));
        Scene sc= new Scene(root,600,400);
        sc.getStylesheets().add(View.class.getResource("RootStyle.css").toExternalForm());
        MainStage.setScene(sc);
        MainStage.centerOnScreen();
    }
    public static void Home() throws IOException {
        Parent root = FXMLLoader.load(View.class.getResource("/Admin/FXML/admin.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(View.class.getResource("RootStyle.css").toExternalForm());
        MainStage.setScene(sc);
        MainStage.centerOnScreen();
    }
    public static void Show() throws IOException {
        Parent root = FXMLLoader.load(View.class.getResource("/Admin/FXML/Show.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(View.class.getResource("RootStyle.css").toExternalForm());
        MainStage.setScene(sc);
    }
    public static void CreateRoom() throws IOException {
        Parent root = FXMLLoader.load(View.class.getResource("/Admin/FXML/CreateRoom.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(View.class.getResource("RootStyle.css").toExternalForm());
        MainStage.setScene(sc);
    }
}
