package Main;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage Mstage;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Mstage=stage;
        editV.ListFlim();
//        editV.showtime();
//        editview.room();
        stage.show();
    }
}
