package DBcontroller;

import config.Connector;
import entity.Film;
import entity.Seat_type;
import entity.Showtime;
import entity.room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBcontroller {
//    lấy tất cả phim chiếu trong ngày
//    select * from list_film where id IN (select id_film from showtime where date = CURRENT_DATE()+1);
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
//    lấy lịch chiếu của phim trong ngày
//    select * from showtime where date = CURRENT_DATE() and id_film = 1;
    public void getShowtime()throws Exception{
//        ObservableList<ArrayList<Showtime>> obl= FXCollections.observableArrayList();
        //                obl.add(arr);
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
//    select * from seat_type where id = id_type_room
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

//    khi chọn ngày , giờ chiếu xong thì lấy id_type_room từ id_room
//    select * from type_room where id_room = id_room (showtime)


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
    public void plusprice2() throws Exception{
//        type seat
        ArrayList<Seat_type> hm= new ArrayList<>();
        String sql= "select * from seat_type where id_type_room like '"+Data.type_room_selected+"'";
//         AND name like '"+Data.type_seat_selected+"'
        ResultSet rs= cnn.queryRS(sql);
        while (rs.next()) {
            hm.add(new Seat_type(
                    rs.getInt("id"),
                    rs.getInt("plus_origin_price"),
                    rs.getString("id_type_room"),
                    rs.getString("name")
                    ));
        }

        Data.seat_type2=hm;
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


//    khi chọn phim lấy giá phim để tính theo id_film đã chọn
//    select * from list_film where id = id_film  (showtime)

//    check showtime của film theo ngày


//    select * from combo food
//      get combo food item
//    select * from combo_food_item where id_combo_food = id_combo_food


//    convert image
//      Blob bl= rs.getBlob("image");
//      InputStream in = bl.getBinaryStream();
//      BufferedImage img= ImageIO.read(in);
//      Image image = SwingFXUtils.toFXImage(img, null);
//      ImageView im= new ImageView();
//            im.setImage(image);
//            im.setFitHeight(100);
//            im.setFitWidth(100);

//


}
