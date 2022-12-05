package Admin.entity;

public class SeatStructure {
    private String typeSeat,colorSeat;
    private Integer row; //
    private Integer col;

    public SeatStructure(String typeSeat, String colorSeat, Integer row, Integer col) {
        this.typeSeat = typeSeat;
        this.colorSeat = colorSeat;
        this.row = row;
        this.col = col;
    }

    public String getTypeSeat() {
        return typeSeat;
    }

    public void setTypeSeat(String typeSeat) {
        this.typeSeat = typeSeat;
    }

    public String getColorSeat() {
        return colorSeat;
    }

    public void setColorSeat(String colorSeat) {
        this.colorSeat = colorSeat;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }
}
