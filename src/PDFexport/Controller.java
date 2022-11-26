package PDFexport;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import Main.editV;
import Rooms.Controller.Mctl;
import entity.Order;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;

import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Label CurrentDateTime;
    public Label FilmName;
    public Label Showtime;
    public Text RoomName;
    public Text NameSeat;
    public Text TypeSeat;
    public Text Price;
    public AnchorPane CinemaTicket;

    public AnchorPane CinemaFood;
    public Label CurrentDateTimeFood;
    public Text priceFood;
    public GridPane foodlist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CinemaTicket.setVisible(!Data.Order_item.isEmpty());
        CinemaFood.setVisible(!Data.order_food_item.isEmpty());
    }


    public void Print(ActionEvent actionEvent) throws IOException {
        LocalDateTime dt= LocalDateTime.now();
        entity.Showtime data= Data.showtime_time_selected;
        ObservableList<Order> odi = Data.Order_item;

        PDDocument doc = new PDDocument();
        CurrentDateTime.setText(dt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) +" \t "+ dt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        CurrentDateTimeFood.setText(dt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) +" \t "+ dt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
//      ticket
        if(!odi.isEmpty()){
            for(int i=0;i<odi.size();i++){
                FilmName.setText(Data.film_selected.getName());
                Showtime.setText(data.getDate().toString()+"  "+data.getTime().toLocalTime().format((DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
                        + " ~ " +  data.getDate().toString()+"  "+data.getTime().toLocalTime().plusMinutes(Data.film_selected.getDuration()).format((DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
                );
                RoomName.setText(odi.get(i).getRoom_name());
                NameSeat.setText(odi.get(i).getName_seat());
                TypeSeat.setText(odi.get(i).getName_type_seat());
                Price.setText(odi.get(i).getPrice().toString());

                WritableImage snapshot = CinemaTicket.snapshot(new SnapshotParameters(), null);
                BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                convertIMG(doc,image,baos);
            }
        }
        //        food
        if(!Data.order_food_item.isEmpty()){
            int a=0;
            for(int i=0;i<(Data.order_food_item.size()%2==0?Data.order_food_item.size()/2:Data.order_food_item.size()/2+1);i++){
                for(int j=0;j<Math.min((Data.order_food_item.size()-i-1 ),2);j++){
                    Text txt= new Text(Data.order_food_item.get(a).getId_combo_food()+" x"+Data.order_food_item.get(a).getAmount()+", ");
                    txt.setFont(Font.font("",18));
                    foodlist.add(txt,j,i,1,1);
                    a++;
                    if(a==Data.order_food_item.size()){break;}
                }
            }
            priceFood.setText(String.valueOf(Mctl.totalCombo));

            WritableImage snapshot = CinemaFood.snapshot(new SnapshotParameters(), null);
            BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            convertIMG(doc,image,baos);
        }

//        save and close file pdf
        doc.save(LocalDate.now().toString()+LocalTime.now().getNano()+"TK.pdf");
        doc.close();
//        insert data
        try {
            DBcontroller db=new DBcontroller();
            if(Data.EditSTS){
                db.updateOrder(Data.Id_Order);
            }else{
                if(!Data.Order_item.isEmpty()){db.addOrderItem();};
                if(!Data.order_food_item.isEmpty()){db.addFood();};
            }
            Thread.sleep(3000);
        }catch (Exception e) {
            e.printStackTrace();
        }
//       delete old data and back to listfilm
//        editV.CancalEdit();
    }


    public void convertIMG(PDDocument doc,BufferedImage image,ByteArrayOutputStream baos){

        PDPage page  = new PDPage();
        doc.addPage(page);
        try {
//                convert to byte[]
            ImageIO.write(image, "png", baos); // baos la` output
            baos.flush();
            byte[] imageInByte = baos.toByteArray(); // you have the data in byte array
            baos.close();
//
            PDPageContentStream contents = new PDPageContentStream(doc, page);
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc,imageInByte,"");
            contents.drawImage(pdImage,0,0);
            contents.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void toListFilm(ActionEvent actionEvent) throws IOException {
        editV.CancalEdit();
        editV.ListFlim();
    }
}
