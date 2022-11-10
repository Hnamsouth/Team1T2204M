package entity;

import java.sql.Time;
import java.util.Date;

public class ChonGhe {
    private String idGhe,idLoaiGhe,idPhong ,date,time;


    public ChonGhe(String idGhe, String idLoaiGhe, String idPhong, String date, String time) {
        this.idGhe = idGhe;
        this.idLoaiGhe = idLoaiGhe;
        this.idPhong = idPhong;
        this.date = date;
        this.time = time;
    }

    public String getIdGhe() {
        return idGhe;
    }

    public void setIdGhe(String idGhe) {
        this.idGhe = idGhe;
    }

    public String getIdLoaiGhe() {
        return idLoaiGhe;
    }

    public void setIdLoaiGhe(String idLoaiGhe) {
        this.idLoaiGhe = idLoaiGhe;
    }

    public String getIdPhong() {
        return idPhong;
    }

    public void setIdPhong(String idPhong) {
        this.idPhong = idPhong;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
