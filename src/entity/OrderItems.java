package entity;

public class OrderItems {
    private Integer id,id_order_ticket,id_type_seat;
    private String nameseat;

    public OrderItems(Integer id, Integer id_order_ticket, Integer id_type_seat, String nameseat) {
        this.id = id;
        this.id_order_ticket = id_order_ticket;
        this.id_type_seat = id_type_seat;
        this.nameseat = nameseat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_order_ticket() {
        return id_order_ticket;
    }

    public void setId_order_ticket(Integer id_order_ticket) {
        this.id_order_ticket = id_order_ticket;
    }

    public Integer getId_type_seat() {
        return id_type_seat;
    }

    public void setId_type_seat(Integer id_type_seat) {
        this.id_type_seat = id_type_seat;
    }

    public String getNameseat() {
        return nameseat;
    }

    public void setNameseat(String nameseat) {
        this.nameseat = nameseat;
    }
}
