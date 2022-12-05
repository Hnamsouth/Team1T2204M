package Admin.entity;

import Admin.CTL.CreateCinemaCtl;
import Admin.CTL.CreateShowtimeCtl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.stage.Window;

public class Cinema {

    private  Integer id;
    private String name,city,address;
    private Integer id_manager;
    private  double Sale;
    Button Edit= new Button("Edit");

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getId_manager() {
        return id_manager;
    }
    public void setId_manager(Integer id_manager) {
        this.id_manager = id_manager;
    }
    public Button getEdit() {
        return Edit;
    }
    public void setEdit(Button edit) {
        Edit = edit;
    }

    public Cinema() {

    };
    public static Dialog dal;
    //get
    public Cinema(Integer id,String name,String city, String address, Integer id_manager) {
        this.id=id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.id_manager = id_manager;

        Edit.setStyle("-fx-text-fill: black");
        this.Edit.setOnAction(e->{
            CreateCinemaCtl.CNMedit=this;
            CreateCinemaCtl.CinemaEdit=true;
            try {
                Parent filmXML= FXMLLoader.load(Film.class.getResource("/Admin/FXML/CreateCinema.fxml"));
                dal= new Dialog<>();dal.getDialogPane().setPrefSize(600,400);
                dal.getDialogPane().setGraphic(filmXML);
                dal.show();
                Window window = dal.getDialogPane().getScene().getWindow();
                window.setOnCloseRequest((a) -> {
                    dal.close();
                });
            }catch (Exception ex) {}
        });
    }
    // create & edit
    public Cinema(Integer id, String name, String city, String address) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
    }
}
