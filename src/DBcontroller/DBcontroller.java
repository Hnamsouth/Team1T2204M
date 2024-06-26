package DBcontroller;

import Admin.CTL.R_ST_F_CNM;
import Admin.entity.Room;
import ListFilm.ControllerOrder.order;
import Rooms.Controller.Mctl;
import config.Connector;
import entity.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static DBcontroller.Data.*;
import static ListFilm.ControllerOrder.orderFD;

public class DBcontroller {
    Connector cnn;
    public DBcontroller() {
        cnn= Connector.getInstance();
    }

    public boolean CheckAcc(String u,String pw) throws SQLException {
        String sql=String.format("SELECT * FROM `acc_manager` WHERE `user_name` like '%s' and password like '%s' and permissions like 'Counter Staff'",u,pw);
        ResultSet rs =cnn.queryRS(sql);
        Integer id=null;
        if(rs!=null){
            while (rs.next()) {
                id=rs.getInt("id");
                Data.id_manager=id;
            }
        }
        return id!=null;
    }
    public void getAllFilm()throws Exception{
        String sql2=String.format("select * from list_film where id in(select id_film from showtime where date = CURRENT_DATE() and time > CURRENT_TIME() and id_room in(select id from room where id_cinema in (select id from cinema where id in (select id_cinema from manager_cinema where id_manager = %d))))",Data.id_manager);
            ResultSet rs=cnn.queryRS(sql2);
            ArrayList<Film> arr= new ArrayList<>();
            while (rs.next()) {
                Blob bl= rs.getBlob("image");
                InputStream in= bl.getBinaryStream();
                BufferedImage img= ImageIO.read(in);

                arr.add(new Film(
                        rs.getInt("id"),
                        rs.getInt("running_time"),
                        rs.getString("name"),
                        rs.getString("directors"),
                        rs.getString("genre"),
                        rs.getString("language"),
                        rs.getString("rated"),
                        rs.getString("cast"),
                        rs.getString("status"),
                        rs.getDate("release_date"),
                        rs.getDouble("price"),
                        SwingFXUtils.toFXImage(img,null )
                ));
            }
            Data.list_film= arr;
        System.out.println(list_film.size() +"lffffffffffffffff");

    }
    public void getAllFilmOfMonth()throws Exception{
        String sql="select * from list_film where id IN (  SELECT id_film FROM `showtime` WHERE CURRENT_DATE() <=date or CURRENT_DATE()+29 >= date)";
        String sql2=String.format("select * from list_film where id in(select id_film from showtime as st where st.date <= CURRENT_DATE() and id_room in(select id from room where id_cinema in (select id from cinema where id in (select id_cinema from manager_cinema where id_manager = %d))))",Data.id_manager);
        ResultSet rs=cnn.queryRS(sql2);
        ArrayList<Film> arr= new ArrayList<>();
        while (rs.next()) {
            Blob bl= rs.getBlob("image");
            InputStream in= bl.getBinaryStream();
            BufferedImage img= ImageIO.read(in);

            arr.add(new Film(
                    rs.getInt("id"),
                    rs.getInt("running_time"),
                    rs.getString("name"),
                    rs.getString("directors"),
                    rs.getString("genre"),
                    rs.getString("language"),
                    rs.getString("rated"),
                    rs.getString("cast"),
                    rs.getString("status"),
                    rs.getDate("release_date"),
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
                String sql2=String.format("select * from showtime where id_film = %d and date = CURRENT_DATE() and time > CURRENT_TIME() and id_room in(select id from room where id_cinema in (select id from cinema where id in (select id_cinema from manager_cinema where id_manager = %d)))",Data.film_selected.getId(),Data.id_manager);
                ResultSet rs2 = cnn.queryRS(sql2);
                while(rs2.next()){
                    arr.add( new Showtime(
                            rs2.getInt("id"),
                            rs2.getInt("id_film"),
                            rs2.getInt("id_room"),
                            rs2.getDate("date"),
                            rs2.getTime("time")
                    ));
                }
                Data.showtime_film_selected=arr;
                System.out.println(showtime_film_selected.size());
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
                        rs2.getInt("id_room"),
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
        String sql= String.format("SELECT * from room where id in (select id_room from showtime where date = CURRENT_DATE() and id_film =%d) and id_cinema =(select id from cinema where id = (select id_cinema from manager_cinema where id_manager = %d))",Data.film_selected.getId(), Data.id_manager);

        String sql2=String.format("SELECT * from room where id in (select id_room from showtime where date = '"+date+"' and id_film =%d) and id_cinema =(select id from cinema where id = (select id_cinema from manager_cinema where id_manager = %d))",Data.film_selected.getId(), Data.id_manager);
        ResultSet rs = cnn.queryRS(i==1?sql:sql2);
        ArrayList<room> arr= new ArrayList<>();
        while (rs.next()) {
            arr.add(new room(
                    rs.getInt("id"),
                    rs.getInt("id_cinema"),
                    rs.getString("name"),
                    rs.getString("id_type_room")));
        }
        Data.room_of_film= arr;
    }
//    get room Structure
public void getRoomStructure() throws SQLException {
        Showtime st=Data.showtime_time_selected;
        ArrayList<SeatStructure> lseat= new ArrayList<>();
    RoomStructure rstt= new RoomStructure();
    room room= new room();
        String sql1= "select  * from room_structure_detail where id_room ="+st.getId_room();
        ResultSet rs= cnn.queryRS(sql1);
        while(rs.next()){
            lseat.add(new SeatStructure(
                    rs.getString("type_seat"),
                    rs.getString("color"),
                    rs.getInt("row_seat"),
                    rs.getInt("col_seat")
            ));
        }
        if(!lseat.isEmpty()){
            String sql2="SELECT * FROM room_structure as rstt JOIN room ON rstt.id_room=room.id where rstt.id_room="+st.getId_room();
            ResultSet rs2= cnn.queryRS(sql2);
            while (rs2.next()) {
                rstt= new RoomStructure(
                        rs2.getInt("rowR"),
                        rs2.getInt("colR"),
                        lseat,
                        rs2.getInt("id_room")
                );
                Data.room_selected =new room(
                        rs2.getInt("id_room"),
                        rs2.getInt("id_cinema"),
                        rs2.getString("name"),
                        rs2.getString("id_type_room")

                );
            }
        }
    Data.roomStructure=rstt;
}
    //    tính giá khi chọn chỗ ngồi
    public void plusprice() throws Exception{
//        type seat
        ArrayList<Seat_type> ar= new ArrayList<>();
    HashMap<Integer,String> hm= new HashMap<>();
        String sql= "select name,plus_original_price from seat_type where id_type_room like '"+Data.type_room_selected+"'";
//         AND name like '"+Data.type_seat_selected+"'
        ResultSet rs= cnn.queryRS(sql);
        while (rs.next()) {
            ar.add(new Seat_type(rs.getString("name"),rs.getInt("plus_original_price")));
            hm.put(rs.getInt("plus_original_price"),rs.getString("name"));
        }

        Data.plus_of_type=hm;
        Data.seat_type_room_selected=ar;
}
// insert order item
    String dt_current="";
    public void addOrderItem() throws Exception {
//        create order_ticket
        LocalTime dt= LocalTime.now();
         dt_current=dt +""+ dt.getNano()+1;
         String id_token=Data.token==null?"":Data.token.getId();
        String sql1 = String.format("insert into order_ticket(id_showtime,dt_current,id_token,print_status) values (%d,'%s','%s',%d)",Data.showtime_time_selected.getId(),dt_current,id_token,1);
        String sql3 = String.format("insert into order_ticket(id_showtime,dt_current,print_status) values (%d,'%s',%d)",Data.showtime_time_selected.getId(),dt_current,1);
         int rs= cnn.createSTM().executeUpdate(Data.token==null?sql3:sql1);

        if(rs==1){
            ResultSet rs2= cnn.queryRS("select id from order_ticket where dt_current like '"+dt_current+"'");
            while(rs2.next()){
                Data.Order_item.forEach(e->{
                    try {
                        String sql2= String.format("insert into order_ticket_items(id_order_ticket,nameseat,id_type_seat) values(%d,'%s',%d)", rs2.getInt("id"),e.getName_seat(),e.getId_seat_type());
                        int rs3=cnn.createSTM().executeUpdate(sql2);
                        if(rs3==1){
                            System.out.println("insert done");
                        }else{
                            throw new SQLException("insert order item failed");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        }
    }
    public void getSeatSelected() throws SQLException {
        ArrayList<String> arr= new ArrayList<>();
        Showtime st=Data.showtime_time_selected;
        String sql2=String.format("select * from order_ticket_items where id_order_ticket in (select id from order_ticket where id_showtime =(select id from showtime WHERE date = '%s' and id_room LIKE '%s' AND time ='%s' ))",st.getDate(),st.getId_room(),st.getTime());
        ResultSet rs= cnn.queryRS(sql2);
        if(rs!=null){
            while (rs.next()) {
                arr.add(rs.getString("nameseat"));
            }
            Data.seat_selected=arr;
        }
    }
//    get Order
    public boolean  getOrderid(Integer id)throws SQLException{
        String sql= String.format("select * from showtime where id = (select id_showtime from order_ticket where id= %d)",id);
        ResultSet rs1= cnn.queryRS(sql);
        while (rs1.next()) {
            Date d = rs1.getDate("date");
            Time t = rs1.getTime("time");
            LocalTime time= LocalTime.now();
            int date=d.toLocalDate().compareTo(LocalDate.now());
            if(date==0){
                if(!time.isBefore(t.toLocalTime().minusMinutes(30))){
                    return false;
                }else{
                    getOrderidRS(id);
                }
            }else if(date >0){
                getOrderidRS(id);
            }else{
                return false;
            }
        }
        return true;
    }
    public void getOrderidRS(Integer id)throws SQLException{
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
    public  boolean deleteOrder(Integer id)throws SQLException {
        String sql2="delete from order_ticket_items where id_order_ticket = "+id;
        int rs2=cnn.createSTM().executeUpdate(sql2);

        String sql="delete from order_ticket where id = "+id;
        int rs=cnn.createSTM().executeUpdate(sql);
        if(rs==1){
            return true;
        }
        return false;
    }
//  UPDATE---------------
    private boolean check=true;
    public boolean updateOrder(Integer id) throws SQLException {
        check=true;
        String udOrder= String.format("update order_ticket set id_showtime = %d where id=%d",Data.showtime_time_selected.getId(),id);
        int rs1=cnn.createSTM().executeUpdate(udOrder);
        if(rs1==1){
//            String getItem ="SELECT COUNT(*)as amount FROM `order_ticket_items` where id_order = "+id;
            String getItem ="delete from `order_ticket_items` where id_order_ticket = "+id;
            int rs2=cnn.createSTM().executeUpdate(getItem);
            Data.Order_item.forEach(e->{
                String sql2= String.format("insert into order_ticket_items(id_order_ticket,nameseat,id_type_seat) values(%d,'%s',%d)",id,e.getName_seat(),e.getId_seat_type());
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
        return check;
    }

    public void checkOrderStatus(Integer id) throws SQLException {
        String sql= " select * from order_ticket where id = "+id;
        OrderTicket od= new OrderTicket();
        ResultSet rs=cnn.queryRS(sql);
        while (rs.next()){
            od.setId(rs.getInt("id"));
            od.setId_showtime(rs.getInt("id_showtime"));
            od.setDt_current(rs.getString("dt_current"));
            od.setId_token(rs.getString("id_token"));
            od.setPrint_status(rs.getBoolean("print_status"));
        }
         order_ticket=od;
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
                    rs.getInt("running_time"),
                    rs.getString("name"),
                    rs.getString("directors"),
                    rs.getString("genre"),
                    rs.getString("language"),
                    rs.getString("rated"),
                    rs.getString("cast"),
                    rs.getString("status"),
                    rs.getDate("release_date"),
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
                rs.getInt("id_room"),
                rs.getDate("date"),
                rs.getTime("time")
            );
            System.out.println(rs.getTime("time"));
        }
    }
    double totalSeat=0;String roomOrderEdit="";
    public void OrderSeatSelected(Integer id) throws Exception {
        roomOrderEdit="";
        ArrayList<OrderItems> arr= new ArrayList<OrderItems>();
        ArrayList<Seat_type> st= new ArrayList<Seat_type>();
        ArrayList<String> ods=new ArrayList<>();
        ObservableList<String> seat_selected=FXCollections.observableArrayList();
        String sql="select * from order_ticket_items where id_order_ticket = "+id;
        ResultSet rs = cnn.queryRS(sql);


        String sql2=String.format("select st.id,st.name,st.plus_original_price,st.id_type_room,room.name as room_name from seat_type as st JOIN type_room as tr ON st.id_type_room=tr.id JOIN room on tr.id=room.id_type_room  where st.id in (select id_type_seat from order_ticket_items where id_order_ticket = %d)",id);
        ResultSet rs2 = cnn.queryRS(sql2);
        while (rs2.next()){
            st.add(new Seat_type(rs2.getInt("id"),
                            rs2.getInt("plus_original_price"),
                            rs2.getString("id_type_room"),
                            rs2.getString("name")   ));
            Data.type_room_selected=rs2.getString("id_type_room");
            roomOrderEdit=rs2.getString("room_name");
        }

        while (rs.next()) {
            arr.add(new OrderItems(
                    rs.getInt("id"),
                    rs.getInt("id_order_ticket"),
                    rs.getInt("id_type_seat"),
                    rs.getString("nameseat")
            ));

        }
        totalSeat=0;
        arr.forEach(e->{
            st.forEach(k->{
                if(k.getId().equals(e.getId_type_seat())){
                    Double price =(film_selected.getPrice()+k.getPlus_origin_price()*film_selected.getPrice()/100)/1;
                    totalSeat+=price;
                    Data.Order_item.add(new Order(
                            e.getNameseat(),
                            k.getName(),
                            k.getId(),
                            Data.film_selected.getName(),
                            roomOrderEdit,
                            k.getId_type_room(),
                            price,
                            Data.showtime_time_selected.getDate(),
                            Data.showtime_time_selected.getTime()
                    ));
                }
            });
            ods.add(e.getNameseat());
            seat_selected.add(e.getNameseat());
        });
        Mctl.totalseat=totalSeat;
        Mctl.seat_selected=seat_selected;
        Data.current_seat_amount=arr.size();
        Data.order_seat_selected=ods;
        getSeatType();
    }
//    food
    private Double price;
    public void getAllFood() throws Exception,SQLException{
       String sql="select * from foods";
        ArrayList<food> food= new ArrayList<>();
       ResultSet rs =cnn.queryRS(sql);
       while (rs.next()) {
           food.add(new food(rs.getString("name"),rs.getDouble("price")));
       }
        Data.food=food;
    }
    public void getCombo_food_item() throws Exception,SQLException{
        String sql2="select * from combo_food_item";
        ArrayList<Combo_food_item> combo_food_items = new ArrayList<>();
        ResultSet rs2 =cnn.queryRS(sql2);
        while (rs2.next()) {
            combo_food_items.add(new Combo_food_item(
                    rs2.getInt("id"),rs2.getString("id_combo_food"),
                    rs2.getString("id_food"),rs2.getInt("amount"),
                    rs2.getInt("decrease")
            ));
        }
        Data.Combo_food_item=combo_food_items;
    }
    public void getCombo_food() throws Exception,SQLException{
        String sql3="select * from combo_food";
        ObservableList<ComboFood> Combo_food = FXCollections.observableArrayList();
        ResultSet rs3 =cnn.queryRS(sql3);
        while (rs3.next()) {
            ArrayList<Combo_food_item> combo_food_items2= new ArrayList<>();
            String id=rs3.getString("id");
            price=0.0;
            for(Combo_food_item e:Data.Combo_food_item){
                if(e.getId_combo_food().matches(id)){
                    food.forEach(k->{
                        if(k.getName().matches(e.getId_food())){
                            price+=(k.getPrice()-(k.getPrice()*e.getDecrease()/100))*e.getAmount();
                            combo_food_items2.add(e);
                        }
                    });
                }
            }

            Combo_food.add(new ComboFood(id,price,combo_food_items2));
        }
        Data.Combo_food=Combo_food;
    }
    String dt_currentFood="";
    public void addFood() throws Exception,SQLException{
        LocalTime dt= LocalTime.now();
         dt_currentFood=dt +""+ dt.getNano()+1;
        String sql1= String.format("insert into order_foods (date_time) values('%s')",dt_current);
        int rs= cnn.createSTM().executeUpdate(sql1);
        if(rs==1){
            String sql2=String.format("select id from order_foods where date_time like '%s'",dt_current);
            ResultSet rs2= cnn.queryRS(sql2);
            while(rs2.next()){
                int id=rs2.getInt("id");
                Data.order_food_item.forEach(e->{
                    String sql3=String.format("insert into order_food_items(id_combo_food,id_order_food,amount) value('%s',%d,%d)",e.getId_combo_food(),id,e.getAmount());
                    try {
                        int rs3= cnn.createSTM().executeUpdate(sql3);
                        if(rs3!=1){
                            Alert al= new Alert(Alert.AlertType.WARNING);
                            al.setContentText("insert failed...!");
                            al.show();
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

        }
    }
    public void GetOrderFoodQR(Integer id) throws Exception {
        if(Data.Combo_food.isEmpty()){
            getAllFood();
            getCombo_food_item();
            getCombo_food();
        }

        ObservableList<OrderFoodItem> ofi= FXCollections.observableArrayList();
        String sql="select * from order_food_items where id_order_foods = "+id;
        ResultSet rs=cnn.queryRS(sql);
        while (rs.next()) {
//            Integer id, String id_combo_food, String description, Integer id_order_food, Integer amount,Double price
            Double price=0.0;
            ofi.add(new OrderFoodItem(
                    rs.getInt("id"),
                    rs.getString("id_combo_food"),
                    "",
                    rs.getInt("id_order_food"),
                    rs.getInt("amount"),
                    price
            ));
        }
        Data.order_food_item=ofi;
    }

    public void checkToken(String id)throws Exception{
        String sql1=String.format("select * from token where id like '%s'",id);
                ResultSet rs = cnn.queryRS(sql1);
                while (rs.next()) {
                    Data.token= new token(rs.getString("id"),
                            rs.getString("id_voucher"),
                            rs.getDate("end_date"),
                            rs.getBoolean("usage_status"));
                }

    }
    public void getVoucher(String id) throws SQLException {
        String sql=String.format("select * from voucher where id like '%s'",id);
        ResultSet rs2 = cnn.queryRS(sql);
        while (rs2.next()){
            Data.voucher=new voucher(rs2.getInt("percent"),rs2.getDouble("card"));
        }
    }

    public boolean udOrderStatus(String id_tk) throws SQLException {
        String sql= String.format("update order_ticket set status =1,id_token ='%s'",id_tk);
        int check=cnn.createSTM().executeUpdate(sql);
        if(check!=1){
            return false;
        }
        return true;
    }

}
