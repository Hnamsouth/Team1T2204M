import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage Mstage;

    public static void main(String[] args) {

       launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Mstage=stage;

//        Parent root = FXMLLoader.load(getClass().getResource("/ListFilm/ShowLF.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/Rooms/B1.fxml"));
        Scene sc= new Scene(root,1280,720);
        sc.getStylesheets().add(getClass().getResource("RootStyle.css").toExternalForm());
        stage.setScene(sc);
        stage.show();
    }
}