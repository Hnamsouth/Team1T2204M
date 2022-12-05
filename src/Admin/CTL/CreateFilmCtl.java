package Admin.CTL;

import Admin.config.ControllerDB;
import Admin.config.Data;
import Admin.entity.Film;
import Main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateFilmCtl implements Initializable {
    public TextField txtFilmName,txtDirector,txtPrice,txtRunningTime;
    public ComboBox<String> GenreCb,RatedCb,LanguageCb,StatusCb;
    public DatePicker ReleaseDate;
    public TextArea txtCast;
    public ImageView imV;

    public static File file;
    Image img;

    public static Film FilmEdit;
    public static boolean FilmEditSTT=false;

    ObservableList<String> GenreList= FXCollections.observableArrayList(
            "Action", "Comedy", "Crime", "Adventure","Fantasy","Science Fiction","Thriller","Mystery","Horror","Drama","Animation","Family","Musicals","Romance"
    );
    ObservableList<String> RatedList= FXCollections.observableArrayList(
            "C18 - NO CHILDREN UNDER 18 YEARS OLD","C13 - NO CHILDREN UNDER 13 YEARS OLD","P - GENERAL MOVIE TO ALL CUSTOMERS",
            "C16 - NO CHILDREN UNDER 16 YEARS OLD"
    );
    ObservableList<String> LanguageList= FXCollections.observableArrayList(
            "English with Vietnamese subtitle","English with Vietnamese and Korean subtitle","Vietnamese Dubbing","Thai with Vietnamese and English subtitle",
            "Japanese with Vietnamese subtitle","English with Vietnamese subtitle, dubbing"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GenreCb.setItems(GenreList);
        RatedCb.setItems(RatedList);
        LanguageCb.setItems(LanguageList);
        StatusCb.setItems(Data.filmStatus);
        if(FilmEditSTT && FilmEdit!=null){
//            txtFilmName,txtDirector,txtPrice,txtRunningTime,txtCast;
            txtFilmName.setText(FilmEdit.getName());
            txtDirector.setText(FilmEdit.getDirector());
            txtPrice.setText(String.valueOf(FilmEdit.getPrice()));
            txtRunningTime.setText(String.valueOf(FilmEdit.getRunning_time()));
            txtCast.setText(FilmEdit.getCast());
//            GenreCb,RatedCb,LanguageCb,StatusCb;
            GenreCb.setValue(FilmEdit.getGenre());
            RatedCb.setValue(FilmEdit.getRated());
            LanguageCb.setValue(FilmEdit.getLanguage());
            StatusCb.setValue(FilmEdit.getStatus());
//            ReleaseDate,imV,img
            ReleaseDate.setValue(FilmEdit.getRelease_date().toLocalDate());
            img=FilmEdit.getImg();
            imV.setImage(img);
        }
    }
    public void ChooserIMG(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        file = fileChooser.showOpenDialog(Main.Mstage);
//        cấu hình đường dẫn mặc định khi mở file
        configureFileChooser(fileChooser);
        System.out.println(file.getName());
//        get img file
        if(file.exists()){
            long MGbytes = file.length();
            if((MGbytes/1024/1024) <5){
                BufferedImage image = ImageIO.read(file);
                img= SwingFXUtils.toFXImage(image,null );
                imV.setImage(img);
            }else{
                showAlert("Image size is too large");
                        file=null;
            }
        }

    }
    private static void configureFileChooser ( final FileChooser fileChooser){
//        config
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
//        filter
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif","*.svg","*.webp","*.*")
        );

    }
    public void createFilm(ActionEvent actionEvent) throws SQLException, IOException {

        if(txtRunningTime.getText().matches("[0-9]*[0-9]") && txtPrice.getText().matches("[0-9]*[0-9].[0-9]") && img!=null && !txtFilmName.getText().isEmpty()&&
                !txtDirector.getText().isEmpty() && !GenreCb.getValue().isEmpty() && !RatedCb.getValue().isEmpty() && !LanguageCb.getValue().isEmpty() && !StatusCb.getValue().isEmpty()
                && ReleaseDate.getValue()!=null && !txtCast.getText().isEmpty())  {
            Data.create_film=new Film(null,
                Integer.parseInt(txtRunningTime.getText()),
                txtFilmName.getText(),
                txtDirector.getText(),
                GenreCb.getValue(),
                LanguageCb.getValue(),
                RatedCb.getValue(),
                txtCast.getText(),
                StatusCb.getValue(),
                java.sql.Date.valueOf(ReleaseDate.getValue()),
                Double.parseDouble(txtPrice.getText()),
                img
            );
            ControllerDB db= new ControllerDB();
            if(FilmEditSTT){
                db.updateFilm();
                Close(null);
            }else{
                db.SaveFilm();
            }
            db.getFilm(StatusCb.getValue());
            R_ST_F_CNM.tableV.setItems(R_ST_F_CNM.Film_List);

        }else{
            showAlert("Missing data");
        }
    }
    public void showAlert(String txt){
        Alert al= new Alert(Alert.AlertType.WARNING);
        al.setContentText(txt);
        al.show();
    }
    public void Close(ActionEvent actionEvent) {
        if(FilmEditSTT){
            FilmEditSTT=false;
            Film.dal.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Film.dal.close();
        }else{
            R_ST_F_CNM.dal.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            R_ST_F_CNM.dal.close();
        }

    }

}
