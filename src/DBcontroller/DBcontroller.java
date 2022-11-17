package DBcontroller;

import ListFilm.ControllerOrder.orderEdit;
import ListFilm.ControllerOrder.order;
import config.Connector;
import entity.*;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static DBcontroller.Data.*;
import static DBcontroller.Data.showtime_time_selected;
import static ListFilm.ControllerOrder.orderFD;

public class DBcontroller {
    Connector cnn;
    public DBcontroller() {
        cnn= Connector.getInstance();
    }
    public void getAllFilm()throws Exception{
            String sql="select * from list_film where id IN (select id_film from showtime where date = CURRENT_DATE())";
            ResultSet rs=cnn.queryRS(sql);

            ArrayList<Film> arr= new ArrayList<>();
            while (rs.next()) {
                Blob bl= rs.getBlob("image");
                InputStream in= bl.getBinaryStream();
                BufferedImage img= ImageIO.read(in);

                arr.add(new Film(
                        rs.getInt("id"),
                        rs.getInt("duration"),
                        rs.getString("name"),
                        rs.getString("directors"),
                        rs.getString("type_film"),
                        rs.getString("language"),
                        rs.getString("rated"),
                        rs.getString("the_cast"),
                        rs.getString("status"),
                        rs.getDate("premiere"),
                        rs.getDouble("price"),
                        SwingFXUtils.toFXImage(img,null )
                ));
            }
            Data.list_film= arr;
    }

    public void getAllFilmOfMonth()throws Exception{
        String sql="select * from list_film where id IN (  SELECT id_film FROM `showtime` WHERE CURRENT_DATE() <=date or CURRENT_DATE()+29 >= date)";
        ResultSet rs=cnn.queryRS(sql);

        ArrayList<Film> arr= new ArrayList<>();
        while (rs.next()) {
            Blob bl= rs.getBlob("image");
            InputStream in= bl.getBinaryStream();
            BufferedImage img= ImageIO.read(in);

            arr.add(new Film(
                    rs.getInt("id"),
                    rs.getInt("duration"),
                    rs.getString("name"),
                    rs.getString("directors"),
                    rs.getString("type_film"),
                    rs.getString("language"),
                    rs.getString("rated"),
                    rs.getString("the_cast"),
                    rs.getString("status"),
                    rs.getDate("premiere"),
                    rs.getDouble("price"),
                    SwingFXUtils.toFXImage(img,null )
            ));
        }
        Data.list_film= arr;
    }
//    lấy lịch chiếu của phim đã chọn trong ngày
    public void getShowtime()throws Exception{
            try {
                ArrayList<Showtime> arr= new ArrayList<Showtime>();
                ResultSet rs2 = cnn.queryRS("select * from showtime where date = CURRENT_DATE() and time > CURRENT_TIME() and id_film = "+Data.film_selected.getId());
                while(rs2.next()){
                    arr.add( new Showtime(
                            rs2.getInt("id"),
                            rs2.getInt("id_film"),
                            rs2.getString("id_room"),
                            rs2.getDate("date"),
                            rs2.getTime("time")
                    ));
                }
                Data.showtime_film_selected=arr;
                typeroom(1,"");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
    }
//    lấy showtime film được chọn theo ngày chỉ định
    public ArrayList<Showtime>  showtimeFilmSelected(String date){
        try {
            ArrayList<Showtime> arr= new ArrayList<Showtime>();
            ResultSet rs2 = cnn.queryRS("select * from showtime where date = '"+ date + "' and id_film = "+Data.film_selected.getId());
            while(rs2.next()){

                arr.add( new Showtime(
                        rs2.getInt("id"),
                        rs2.getInt("id_film"),
                        rs2.getString("id_room"),
                        rs2.getDate("date"),
                        rs2.getTime("time")
                ));
            }
            typeroom(2,date);
            return arr;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //  lấy % cộng thêm của chỗ ngồi khách hàng chọn
    public void getSeatType() throws Exception{

        ArrayList<Seat_type> arr= new ArrayList<>();
        String sql= String.format("select * from seat_type where id_type_room like (select id from type_room where id like (select id_type_room from room where id like '%s'))",Data.showtime_time_selected.getId_room()); // id room from showtime
        ResultSet rs = cnn.queryRS(sql);
        while (rs.next()) {
            arr.add(new Seat_type(
                rs.getInt("id"),
                rs.getInt("plus_original_price"),
                rs.getString("id_type_room"),
                rs.getString("name")
            ));
        }
       Data.seat_type= arr;
    }
//    get all type room of film
    public void typeroom(int i,String date)throws Exception{
        String sql= String.format("SELECT * from room where id in (select id_room from showtime where date = CURRENT_DATE() and time >= CURRENT_TIME and id_film = %d)",Data.film_selected.getId());

        String sql2=String.format("SELECT * from room where id in (select id_room from showtime where date = '"+date+"' and id_film =%d)",Data.film_selected.getId());
        ResultSet rs = cnn.queryRS(i==1?sql:sql2);
        ArrayList<room> arr= new ArrayList<>();
        while (rs.next()) {
            arr.add(new room(rs.getString("id"),rs.getString("id_type_room")));
        }
        Data.room_of_film= arr;
    }
    //    tính giá khi chọn chỗ ngồi
    public void plusprice() throws Exception{
//        type seat
    HashMap<Integer,String> hm= new HashMap<>();
        String sql= "select name,plus_original_price from seat_type where id_type_room like '"+Data.type_room_selected+"'";
//         AND name like '"+Data.type_seat_selected+"'
        ResultSet rs= cnn.queryRS(sql);
        while (rs.next()) {
            hm.put(rs.getInt("plus_original_price"),rs.getString("name"));
        }
        Data.plus_of_type=hm;
}

// insert order item
    public void addOrderItem() throws SQLException {
//        create order_ticket
        LocalTime dt= LocalTime.now();
        String dt_current=dt +""+ dt.getNano()+1;
        String sql1 = String.format("insert into order_ticket(id_showtime,dt_current) values (%d,'%s')",Data.showtime_time_selected.getId(),dt_current);
         int rs= cnn.createSTM().executeUpdate(sql1);

        if(rs==1){
            ResultSet rs2= cnn.queryRS("select id from order_ticket where dt_current like '"+dt_current+"'");
            int id=0;
            while(rs2.next()){
                id = rs2.getInt("id");
            }

            int finalId = id;
//      add order item

            Data.Order_item.forEach(e->{
                String sql2= String.format("insert into order_ticket_items values(%d,'%s',%d)", finalId,e.getName_seat(),e.getId_seat_type());
                try {
                    int rs3=cnn.createSTM().executeUpdate(sql2);

                    if (rs3!=1) throw new SQLException("insert order item failed");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

    }
    public ArrayList<String> getSeatSelected() throws SQLException {

        ArrayList<String> arr= new ArrayList<>();

        String sql="select nameseat from order_ticket_items where id_order_ticket in (select id from order_ticket where id_showtime =(select id from showtime WHERE date = CURRENT_DATE() and id_room LIKE 'Cinema 4' AND time ='10:25:00' ))";
        Showtime st=Data.showtime_time_selected;
        String sql2=String.format("select * from order_ticket_items where id_order_ticket in (select id from order_ticket where id_showtime =(select id from showtime WHERE date = '%s' and id_room LIKE '%s' AND time ='%s' ))",st.getDate(),st.getId_room(),st.getTime());
        ResultSet rs= cnn.queryRS(sql2);
        while (rs.next()) {
            arr.add(rs.getString("nameseat"));
        }
        Data.seat_selected=arr;
        return arr;
    }
//    get Order
    public void getOrderid(Integer id)throws SQLException{
        ResultSet rs = cnn.queryRS("select * from order_ticket where id = "+id);

        while (rs.next()) {
            if(orderFD.isEmpty()){
                orderFD.add(0,new order(rs.getInt("id")));
            }else{
                orderFD.set(0,new order(rs.getInt("id")));
            }
        }
    }
//  DELETE--------------

//    Xóa 1 đơn hàng. xóa các item của đơn hàng đó xong xóa đơn hàng đó
//    DELETE FROM order_ticket_items WHERE id_order_ticket=1;
//    DELETE FROM `order_ticket` WHERE id=7;

    public  boolean deleteOrder(Integer id)throws SQLException {
        String sql2="delete from order_ticket_items where id_order_ticket = "+id;
        int rs2=cnn.createSTM().executeUpdate(sql2);
        if(rs2==1){
            String sql="delete from order_ticket where id = "+id;
            int rs=cnn.createSTM().executeUpdate(sql);
            if(rs==1){
                return true;
            }
        }
        return false;
    }

//  UPDATE---------------

//    xửa  tên ghế và loại ghế của item đơn hàng đó
//    update order_ticket_items set nameseat='%s',id_type_seat='%s' where id_order_ticket= %d
//    xửa lịch chiếu , phim,lọại phòng...
private boolean check;
    public boolean updateOrder(Integer id) throws SQLException {
        check=true;
        String udOrder= String.format("update order_ticket set id_showtime = %d where id=%d",Data.showtime_time_selected.getId(),id);
        int rs1=cnn.createSTM().executeUpdate(udOrder);
        if(rs1==1){
//            String getItem ="SELECT COUNT(*)as amount FROM `order_ticket_items` where id_order = "+id;
            String getItem ="delete from `order_ticket_items` where id_order_ticket = "+id;
            int rs2=cnn.createSTM().executeUpdate(getItem);
                if(rs2==1){
                    Data.Order_item.forEach(e->{
                        String sql2= String.format("insert into order_ticket_items values(%d,'%s',%d)", id,e.getName_seat(),e.getId_seat_type());
                        try {
                            int rs3=cnn.createSTM().executeUpdate(sql2);
                            if(rs3!=1){
                                check=false;
                                throw new SQLException("insert order item failed");
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
        }
        return check;
    }


// Edit
    public void GetFilmEditDate(Integer id) throws SQLException, IOException {
        String sql= String.format("select * from list_film where id=(select id_film from showtime where id = (select id_showtime from order_ticket where id=%d))",id);
        ResultSet rs =cnn.queryRS(sql);
        while (rs.next()) {
            Blob bl= rs.getBlob("image");
            InputStream in = bl.getBinaryStream();
            BufferedImage img= ImageIO.read(in);

            Data.film_selected=new Film(
                    rs.getInt("id"),
                    rs.getInt("duration"),
                    rs.getString("name"),
                    rs.getString("directors"),
                    rs.getString("type_film"),
                    rs.getString("language"),
                    rs.getString("rated"),
                    rs.getString("the_cast"),
                    rs.getString("status"),
                    rs.getDate("premiere"),
                    rs.getDouble("price"),
                    SwingFXUtils.toFXImage(img, null)
            );
        }
    }

    public void GetShowtimeEditSeat(Integer id) throws SQLException, IOException{
        String sql=String.format("select * from showtime where id =(select id_showtime from order_ticket where id =%d)",id);
        ResultSet rs = cnn.queryRS(sql);
        while (rs.next()) {
            Data.showtime_time_selected=new Showtime(
                rs.getInt("id"),
                rs.getInt("id_film"),
                rs.getString("id_room"),
                rs.getDate("date"),
                rs.getTime("time")
            );
        }
    }

    public void OrderSeatSelected(Integer id) throws Exception {
        ArrayList<OrderItems> arr= new ArrayList<OrderItems>();
        ArrayList<Seat_type> st= new ArrayList<Seat_type>();
        ArrayList<String> ods=new ArrayList<>();
        String sql="select * from order_ticket_items where id_order_ticket = "+id;
        ResultSet rs = cnn.queryRS(sql);


        String sql2=String.format("select * from seat_type where id in (select id_type_seat from order_ticket_items where id_order_ticket = %d)",id);
        ResultSet rs2 = cnn.queryRS(sql2);
        while (rs2.next()){
            st.add(new Seat_type(rs2.getInt("id"),
                            rs2.getInt("plus_original_price"),
                            rs2.getString("id_type_room"),
                            rs2.getString("name")   ));
            Data.type_room_selected=rs2.getString("id_type_room");
        }

        while (rs.next()) {
            arr.add(new OrderItems(
                    rs.getInt("id"),
                    rs.getInt("id_order_ticket"),
                    rs.getInt("id_type_seat"),
                    rs.getString("nameseat")
            ));

        }

        arr.forEach(e->{
            st.forEach(k->{
                if(k.getId().equals(e.getId_type_seat())){
                    Data.Order_item.add(new Order(
                            e.getNameseat(),
                            k.getName(),
                            k.getId(),
                            Data.film_selected.getName(),
                            Data.showtime_time_selected.getId_room(),
                            k.getId_type_room(),
                            (film_selected.getPrice()+k.getPlus_origin_price()*100/ film_selected.getPrice()),
                            Data.showtime_time_selected.getDate(),
                            Data.showtime_time_selected.getTime()
                    ));
                }
            });
            ods.add(e.getNameseat());
        });
        Data.current_seat_amount=arr.size();
        Data.order_seat_selected=ods;
        getSeatType();

    }



}
