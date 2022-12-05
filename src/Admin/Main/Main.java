package Admin.Main;

import Admin.CTL.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage MainStage;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainStage=stage;
//        stage.getIcons().add
        View.SignIn();
//        View.Home();
        stage.show();
    }
}
