package entity;

import java.sql.Date;

public class token {
    private String id, id_voucher;
    private Date end_date;
    private Boolean usage_status;

    public token(String id, String id_voucher, Date end_date, Boolean usage_status) {
        this.id = id;
        this.id_voucher = id_voucher;
        this.end_date = end_date;
        this.usage_status = usage_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_voucher() {
        return id_voucher;
    }

    public void setId_voucher(String id_voucher) {
        this.id_voucher = id_voucher;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Boolean getUsage_status() {
        return usage_status;
    }

    public void setUsage_status(Boolean usage_status) {
        this.usage_status = usage_status;
    }
}
