package Admin.SignIn;

import Admin.CTL.View;
import Admin.config.ControllerDB;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button btnSignIn;
    ControllerDB db;

    public void SignIn(ActionEvent actionEvent) throws SQLException, IOException {
        if(!txtUserName.getText().isEmpty() && !txtPassword.getText().isEmpty()){
            if(db.CheckAcc(txtUserName.getText(),txtPassword.getText())){
                View.Home();
            }else{
                alshow("user name or password invalid");
            }
        }else{
            alshow("missing user name or password");
        }
    }
    public void alshow(String ct){
        Alert al= new Alert(Alert.AlertType.WARNING);
        al.setContentText(ct);
        al.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        db=new ControllerDB();
    }
}
