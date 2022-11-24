package entity;

public class voucher {
    private Integer percent;
    private Double card;

    public voucher(Integer percent, Double card) {
        this.percent = percent;
        this.card = card;
    }

    public voucher() {

    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Double getCard() {
        return card;
    }

    public void setCard(Double card) {
        this.card = card;
    }
}
