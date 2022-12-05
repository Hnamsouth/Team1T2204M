package Admin.entity;


import java.sql.Date;

public class TotalCinema {
    private String CinemaName;
    private Date date;
    private Double total;

    public TotalCinema(String cinemaName, Date date, Double total) {
        CinemaName = cinemaName;
        this.date = date;
        this.total = total;
    }

    public String getCinemaName() {
        return CinemaName;
    }

    public void setCinemaName(String cinemaName) {
        CinemaName = cinemaName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
