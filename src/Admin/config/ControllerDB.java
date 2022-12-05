package Admin.config;


import Admin.CTL.CreateFilmCtl;
import Admin.CTL.CreateRoomCtl;
import Admin.CTL.CreateShowtimeCtl;
import Admin.CTL.R_ST_F_CNM;
import Admin.entity.*;
import Admin.config.Connector;
import Admin.config.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class ControllerDB {
    Connector cnn;
    public ControllerDB() {
        cnn= Connector.getInstance();
    }

    public boolean CheckAcc(String un, String pw) throws SQLException {
        String sql=String.format("SELECT * FROM `acc_manager` WHERE `user_name` like '%s' and password like '%s' and permissions like 'Manager' or `user_name` like '%s' and password like '%s' and permissions like 'Admin'",un,pw,un,pw);
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

    //    cinema
    public  void getCinema() throws SQLException {
        ObservableList<String> city= FXCollections.observableArrayList();
        ObservableList<String> base= FXCollections.observableArrayList();
        ObservableList<Cinema> Cinema= FXCollections.observableArrayList();

        String sql= "SELECT * FROM `cinema` WHERE id in (select id_cinema from manager_cinema where id_manager = "+Data.id_manager+")";
        ResultSet rs= cnn.queryRS(sql);
        while (rs.next()) {
            String ct = rs.getString("city");
            if(city.stream().noneMatch(e->e.equals(ct))){
                city.add(rs.getString("city"));
            };
            base.add(rs.getString("name"));
            Cinema.add(new Cinema(
                    rs.getInt("id"),
                    rs.getString("name"),
                    ct,
                    rs.getString("address"),
                    Data.id_manager
            ));
        }
        Data.city=city;
//        Data.Cinema=Cinema;
        R_ST_F_CNM.CNM_List=Cinema;
    }
    int total=0;
    public void getTotalCinema(String city)throws SQLException{
//        CINEMA name , date, total sales of day
        System.out.println(city);
        ObservableList<ArrayList<TotalCinema>> obb= FXCollections.observableArrayList();
        ObservableList<Cinema> CNM=R_ST_F_CNM.CNM_List.stream().filter(e->e.getCity().matches(city)).collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<PieChart.Data> data= FXCollections.observableArrayList();
        for(Cinema a:CNM){
            ArrayList<TotalCinema> arr = new ArrayList<>();
            total=0;
            for (int i=0;i<7;i++){
                String sql= String.format("select SUM(lf.price+(lf.price*st.plus_original_price/100)) as total from order_ticket_items as oti JOIN seat_type as st ON oti.id_type_seat=st.id JOIN order_ticket as ot ON oti.id_order_ticket= ot.id JOIN showtime ON ot.id_showtime=showtime.id JOIN list_film as lf ON showtime.id_film= lf.id WHERE  CURRENT_DATE()-%d = showtime.date AND showtime.id_room in (SELECT id FROM room where id_cinema in (SELECT id from cinema where city like '%s' AND name like '%s'))",i+1,a.getCity(),a.getName());
                ResultSet rs=cnn.queryRS(sql);
                if(rs!=null){
                    while (rs.next()) {
                        arr.add(new TotalCinema(
                                a.getName(),
                                Date.valueOf(LocalDate.now().minusDays(i+1)),
                                rs.getDouble("total")
                        ));
                        total+=rs.getDouble("total");
                    }
                }else{
                    arr.add(new TotalCinema(  a.getName(),  Date.valueOf(LocalDate.now().minusDays(i+1)),     0.0   ));
                }
            }
            data.add(new PieChart.Data(a.getName(),total));
            obb.add(arr);
        }

        Data.TotalCinemaInWeek=data;
        Data.TotalCinema_List=obb;


    }
    public void saveCinema()throws SQLException{
        Cinema cnm= Data.create_cinema;
        String sql=String.format("INSERT INTO `cinema`( `name`, `city`, `address`) VALUES ('%s','%s','%s')",cnm.getName(),cnm.getCity(),cnm.getAddress());
        if(cnn.queryUD(sql) == 1){
            System.out.println("insert cinema done");
        }else{
            showAlert("insert cinema failed");
        }
    }
    public void updateCinema()throws SQLException{
        Cinema cnm= Data.create_cinema;
        String sql=String.format("update cinema set name ='%s',city='%s',address='%s' where id =%d",cnm.getName(),cnm.getCity(),cnm.getAddress(),cnm.getId());
        if(cnn.queryUD(sql) == 1){
            System.out.println("update cinema done");
        }else{
            showAlert("update cinema failed");
        }
    }

    //    ROOM
    public  void SaveRoom() throws SQLException {
        Room r = Data.create_room;
        String insertROOM= String.format("insert into room (name,id_type_room,id_cinema) values('%s','%s',%d)",r.getName(),r.getId_type_room(),r.getId_cinema());
        if(cnn.queryUD(insertROOM)==1){
//            get id room
            String getRoom = String.format("select * from room where name like '%s' and id_cinema like '%s'",r.getName(),r.getId_cinema());
            ResultSet rs2=cnn.queryRS(getRoom);

            while (rs2.next()) {
                int id_room=rs2.getInt("id");

//                insert room_structure
                RoomStructure rstt= Data.create_room_structure;
                String insertR_STT= String.format("insert into room_structure (rowR,colR,id_room) values(%d,%d,%d)",rstt.getRow(),rstt.getCol(),id_room);
                if(cnn.queryUD(insertR_STT)!=1){
                    throw new RuntimeException("insert room_structure failed");
                }

//                insert room_structure_detail
                ArrayList<SeatStructure> SeatSTT = rstt.getRoomStt();
                SeatSTT.forEach(e->{
                    String insertR_STT_Detail =String.format("insert into room_structure_detail (type_seat,color,row_seat,col_seat,id_room) values ('%s','%s',%d,%d,%d)",e.getTypeSeat(),e.getColorSeat(),e.getRow(),e.getCol(),id_room);
                    try {
                        cnn.queryUD(insertR_STT_Detail);
                    } catch (SQLException ex) {
                        System.out.println("insert room_structure failed");
                        throw new RuntimeException(ex);
                    }
                });
            }
        }
    }
    public void getRoom(String city,String base)  throws SQLException {
        System.out.println(city+ "\t"+base);
        ObservableList<Room> RL = FXCollections.observableArrayList();

        String sql =String.format("select * from room where id_cinema = (select id from cinema where name like '%s' and city like '%s')",base,city);
        String sql2 ="select * from room where id_cinema = (select id from cinema where name like '"+base+"' and city like '"+city+"')";
        ResultSet rs = cnn.queryRS(sql2);
        while (rs.next()) {
            RL.add(new Room(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("id_type_room"),
                    rs.getInt("id_cinema")
            ));
        }
        System.out.println(RL.size());
        R_ST_F_CNM.Room_List=RL;

    }
    public void getRoomStructure() throws SQLException {
        ObservableList<RoomStructure> lt = FXCollections.observableArrayList();
        ObservableList<Room> Rl = R_ST_F_CNM.Room_List;
        for(int i=0;i<Rl.size(); i++){
            ArrayList<SeatStructure> lseat= new ArrayList<>();
            String sql1= "select  * from room_structure_detail where id_room ="+Rl.get(i).getId();
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
                String sql2="select * from room_structure where id_room ="+Rl.get(i).getId();
                ResultSet rs2= cnn.queryRS(sql2);
                while (rs2.next()) {
                    lt.add(new RoomStructure(
                            rs2.getInt("rowR"),
                            rs2.getInt("colR"),
                            lseat,
                            rs2.getInt("id_room")
                    ));
                }
            }
        }
        Data.listRoomStructure=lt;
    }
    public void getRoomStructureEdit(Integer id_room) throws SQLException{
        String sql2="select * from room_structure where id_room ="+id_room;
        ResultSet rs2= cnn.queryRS(sql2);
        while (rs2.next()) {
            CreateRoomCtl.room_structureEdit=new RoomStructure(
                    rs2.getInt("rowR"),
                    rs2.getInt("colR"),
                    null,
                    rs2.getInt("id_room")
            );
        }
    }
    public void deleteRoom(Integer id) throws SQLException{
        String sql="delete from room_structure where id_room ="+id;
        if(cnn.queryUD(sql)!=1){  showAlert("delete failed 1"); }

        String sql2="delete from room_structure_detail where id_room ="+id;
        if(cnn.queryUD(sql2)!=1){  showAlert("delete failed 2");  }

        String sql3="delete from room where id ="+id;
        if(cnn.queryUD(sql3)!=1){  showAlert("delete failed 3");    }
    }

    //    FILM
    public void SaveFilm()throws SQLException{
    String sql="INSERT INTO `list_film`( `name`, `directors`, `genre`, `release_date`, `running_time`, `language`, `rated`, `cast`, `status`, `price`, `image`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    ArrayList data= new ArrayList();
    data.add(Data.create_film.getName());
    data.add(Data.create_film.getDirector());
    data.add(Data.create_film.getGenre());
    data.add(Data.create_film.getRelease_date());
    data.add(Data.create_film.getRunning_time());
    data.add(Data.create_film.getLanguage());
    data.add(Data.create_film.getRated());
    data.add(Data.create_film.getCast());
    data.add(Data.create_film.getStatus());
    data.add(Data.create_film.getPrice());
    data.add(Data.create_film.getImg());
    if(cnn.queryUD(sql,data)){System.out.println("insert film done");}
    else{showAlert("insert film failed");;};
}
    public void getFilm(String stt) throws SQLException, IOException {
        ObservableList<Film> arr= FXCollections.observableArrayList();
        ObservableList<Pair<Integer,String>> arrST= FXCollections.observableArrayList();
        String sql="select * from list_film where status like '"+stt+"'";
        String sql2="select * from list_film where id ="+Data.id_filmEdit_ST;

        ResultSet rs = cnn.queryRS(stt.matches("Id Film")?sql2:sql);
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
            arrST.add(new Pair<>(rs.getInt("id"),rs.getString("name")));
        }
        CreateShowtimeCtl.listFilm=arrST;
        R_ST_F_CNM.Film_List=arr;
    }
    public void updateFilm()throws SQLException {
        String sql="UPDATE `list_film` SET `name`=?,`directors`=?,`genre`=?,`release_date`=?,`running_time`=?,`language`=?," +
                "`rated`=?,`cast`=?,`status`=?,`price`=?,`image`=? WHERE id=?";
        ArrayList data= new ArrayList();
        data.add(CreateFilmCtl.FilmEdit.getName());
        data.add(CreateFilmCtl.FilmEdit.getDirector());
        data.add(CreateFilmCtl.FilmEdit.getGenre());
        data.add(CreateFilmCtl.FilmEdit.getRelease_date());
        data.add(CreateFilmCtl.FilmEdit.getRunning_time());
        data.add(CreateFilmCtl.FilmEdit.getLanguage());
        data.add(CreateFilmCtl.FilmEdit.getRated());
        data.add(CreateFilmCtl.FilmEdit.getCast());
        data.add(CreateFilmCtl.FilmEdit.getStatus());
        data.add(CreateFilmCtl.FilmEdit.getPrice());
        data.add(CreateFilmCtl.FilmEdit.getImg());
        data.add(CreateFilmCtl.FilmEdit.getId());
        if(cnn.queryUD(sql,data)){System.out.println("update film done");}
        else{showAlert("update film failed");};
    }
    public void deleteFilm(Integer id) throws SQLException {
        String sql ="DELETE FROM `list_film` WHERE id ="+id;
        if(cnn.queryUD(sql) == 1){
            System.out.println("delete film done");}
        else{showAlert("delete film failed");};
    }

    // Showtime
    public void getShowtime(String base , String city)throws SQLException{
        String sql=String.format("select list_film.name as name_film,showtime.id,showtime.id_film,showtime.id_room,room.name as name_room,showtime.date,showtime.time from showtime JOIN room ON id_room=room.id JOIN list_film ON id_film=list_film.id where id_room in (select id from room where id_cinema =(SELECT id from cinema where name like '%s' and city like '%s')) AND list_film.status like 'Now Showing'",base,city);
        ResultSet rs = cnn.queryRS(sql);
        ObservableList<Showtime> list= FXCollections.observableArrayList();
        ObservableList<String> room= FXCollections.observableArrayList();
        room.add("All");
        while (rs.next()) {
            list.add(new Showtime(
               rs.getInt("id"),
               rs.getInt("id_film"),
               rs.getInt("id_room"),
               rs.getString("name_film"),
               rs.getString("name_room"),
               rs.getDate("date"),
               rs.getTime("time")
            ));
            room.add(rs.getString("name_room"));
        }
        Data.room_showtime = room;
        R_ST_F_CNM.Showtime_List=list;
    }
    public void saveShowtime() throws SQLException {
        Showtime st=Data.create_showtime;
      String sql= String.format("INSERT INTO `showtime`( `id_film`, `id_room`, `date`, `time`) VALUES (%d,%d,'%s','%s')",st.getId_film(),st.getId_room(),st.getDate(),st.getTime());
      if(cnn.queryUD(sql)==1){
          System.out.println("insert showtime done");}
      else{showAlert("insert showtime failed");;};
    }
    public void updateShowtime() throws SQLException {
        Showtime st=Data.create_showtime;
        String sql=String.format("UPDATE `showtime` SET `id_film`=%d,`id_room`=%d,`date`='%s',`time`='%s' WHERE id = %d",st.getId_film(),st.getId_room(),st.getDate(),st.getTime(),st.getId());
        if(cnn.queryUD(sql)==1){System.out.println("update showtime done");}
        else{showAlert("update showtime failed");;};
    }
    public void deleteShowtime(Integer id) throws SQLException {
        String sql= "delete from showtime where id ="+id;
        if(cnn.queryUD(sql) == 1){
            System.out.println("delete showtime done");}
        else{showAlert("delete showtime failed");};
    }

//    alert
    public void showAlert(String ct){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(ct);
        alert.show();
    }
}
