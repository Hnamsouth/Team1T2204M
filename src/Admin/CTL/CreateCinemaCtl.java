package Admin.CTL;

import Admin.config.ControllerDB;
import Admin.config.Data;
import Admin.entity.Cinema;
import Admin.entity.Showtime;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateCinemaCtl implements Initializable {
    public TextField txtName,txtCity;
    public TextArea txtAddress;

    public static boolean CinemaEdit=false;
    public static Cinema CNMedit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(CinemaEdit){
            txtName.setText(CNMedit.getName());
            txtCity.setText(CNMedit.getCity());
            txtAddress.setText(CNMedit.getAddress());
        }
    }

    public void Create(ActionEvent actionEvent) throws SQLException {
        ControllerDB db= new ControllerDB();
        if(!txtName.getText().isEmpty() && !txtCity.getText().isEmpty()  && !txtAddress.getText().isEmpty() ){
            Data.create_cinema= new Cinema(
                    CinemaEdit?CNMedit.getId():null,
                    txtName.getText(),
                    txtCity.getText(),
                    txtAddress.getText()
            );
            if(!CinemaEdit){
                db.saveCinema();
            }else{
                db.updateCinema();
            }
            db.getCinema();
            R_ST_F_CNM.tableV.setItems(R_ST_F_CNM.CNM_List);
        }
    }

    public void Close(ActionEvent actionEvent) {
        if(CinemaEdit){
            Cinema.dal.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Cinema.dal.close();
            CinemaEdit=false;
        }else{
            R_ST_F_CNM.dal.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            R_ST_F_CNM.dal.close();
        }
    }


}
