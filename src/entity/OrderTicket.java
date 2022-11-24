package entity;

public class OrderTicket {
    private Integer id,id_showtime;
    private String dt_current,id_token;
    private Boolean print_status;

    public OrderTicket(Integer id, Integer id_showtime, String dt_current, String id_token, Boolean print_status) {
        this.id = id;
        this.id_showtime = id_showtime;
        this.dt_current = dt_current;
        this.id_token = id_token;
        this.print_status = print_status;
    }

    public OrderTicket() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_showtime() {
        return id_showtime;
    }

    public void setId_showtime(Integer id_showtime) {
        this.id_showtime = id_showtime;
    }

    public String getDt_current() {
        return dt_current;
    }

    public void setDt_current(String dt_current) {
        this.dt_current = dt_current;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public Boolean getPrint_status() {
        return print_status;
    }

    public void setPrint_status(Boolean print_status) {
        this.print_status = print_status;
    }
}
