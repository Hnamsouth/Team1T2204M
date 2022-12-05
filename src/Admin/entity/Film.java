package Admin.entity;

import Admin.CTL.CreateFilmCtl;
import Admin.CTL.R_ST_F_CNM;
import Admin.config.ControllerDB;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;


public class Film {
    private Integer id,running_time;
    private String name,director,genre,language,rated,cast,status;
    private Date release_date;
    private Double price;
    private Image img;
    private ImageView imgV= new ImageView();
    private HBox action= new HBox();
    private Button Edit= new Button("Edit"), Delete= new Button("Delete"),previewIMG=new Button("Preview");

    public static Dialog dal;
    public Film(Integer id, Integer running_time, String name, String director, String genre, String language, String rated, String cast, String status, Date release_date, Double price, Image img) {
        this.id = id;
        this.running_time = running_time;
        this.name = name;
        this.director = director;
        this.genre = genre;
        this.language = language;
        this.rated = rated;
        this.cast = cast;
        this.status = status;
        this.release_date = release_date;
        this.price = price;
        this.img = img;
        this.previewIMG.setOnAction(e->{
            Dialog dal= new Dialog<>();dal.getDialogPane().setPrefSize(600,400);

            this.imgV.setImage(getImg());
            this.imgV.setPreserveRatio(true);
            this.imgV.setFitHeight(400);
            this.imgV.maxWidth(600);
            dal.getDialogPane().setGraphic(this.imgV);
            dal.show();
            Window window = dal.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest((a) -> {
                dal.close();
            });
        });

        previewIMG.setStyle("-fx-text-fill: black");
        Edit.setStyle("-fx-text-fill: black");
        Delete.setStyle("-fx-text-fill: black");
        this.action.getChildren().addAll(Edit,Delete);
        this.action.setSpacing(7);
        Edit.setOnAction(e->{
            CreateFilmCtl.FilmEdit=this;
            CreateFilmCtl.FilmEditSTT=true;
            try {
                Parent filmXML= FXMLLoader.load(Film.class.getResource("/Admin/FXML/CreateFilm.fxml"));
                dal= new Dialog<>();dal.getDialogPane().setPrefSize(800,600);
                dal.getDialogPane().setGraphic(filmXML);
                dal.show();
                Window window = dal.getDialogPane().getScene().getWindow();
                window.setOnCloseRequest((a) -> {
                    CreateFilmCtl.FilmEditSTT=false;
                    dal.close();
                });
            }catch (Exception ex) {}
        });
        Delete.setOnAction(e->{
            ControllerDB db= new ControllerDB();
            try {
                Alert al= new Alert(Alert.AlertType.CONFIRMATION);
                al.getDialogPane().getButtonTypes().clear();
                al.getDialogPane().getButtonTypes().addAll(ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> rs= al.showAndWait();
                if(rs.get().equals(ButtonType.YES)){
                    db.deleteFilm(this.id);
                    R_ST_F_CNM.Film_List.removeIf(a->a.id.equals(this.id));
                }else{
                    al.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

    }
    public Film() {

    }

    public Button getPreviewIMG() {
        return previewIMG;
    }

    public void setPreviewIMG(Button previewIMG) {
        this.previewIMG = previewIMG;
    }

    public HBox getAction() {
        return action;
    }
    public void setAction(HBox action) {
        this.action = action;
    }
    public ImageView getImgV() {
        return imgV;
    }
    public void setImgV(ImageView imgV) {
        this.imgV = imgV;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getRunning_time() {
        return running_time;
    }
    public void setRunning_time(Integer running_time) {
        this.running_time = running_time;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getRated() {
        return rated;
    }
    public void setRated(String rated) {
        this.rated = rated;
    }
    public String getCast() {
        return cast;
    }
    public void setCast(String cast) {
        this.cast = cast;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getRelease_date() {
        return release_date;
    }
    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Image getImg() {
        return img;
    }
    public void setImg(Image img) {
        this.img = img;
    }
}
