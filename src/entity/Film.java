package entity;

import javafx.scene.image.Image;

import java.sql.Date;

public class Film {
    Integer id,duration;
    String name,director,type_film,language,rated,the_cast,status;
    Date premiere;
    Double price;
    Image img;

    public Film(Integer id, Integer duration, String name, String director, String type_film, String language, String rated, String the_cast, String status, Date premiere, Double price, Image img) {
        this.id = id;
        this.duration = duration;
        this.name = name;
        this.director = director;
        this.type_film = type_film;
        this.language = language;
        this.rated = rated;
        this.the_cast = the_cast;
        this.status = status;
        this.premiere = premiere;
        this.price = price;
        this.img = img;
    }

    public Film() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public String getType_film() {
        return type_film;
    }

    public void setType_film(String type_film) {
        this.type_film = type_film;
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

    public String getThe_cast() {
        return the_cast;
    }

    public void setThe_cast(String the_cast) {
        this.the_cast = the_cast;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPremiere() {
        return premiere;
    }

    public void setPremiere(Date premiere) {
        this.premiere = premiere;
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
