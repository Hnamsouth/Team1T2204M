package Main;

import Admin.CTL.View;
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
        editV.SignIn();
//        editV.ListFlim();
//        stage.getIcons().add
//        View.Home();
        stage.show();
    }
}
