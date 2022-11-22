package PDFexport;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import Main.editV;
import entity.Order;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;

import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void toListFilm(ActionEvent actionEvent) throws IOException {
        editV.CancalEdit();
        editV.ListFlim();
    }

    public void Print(ActionEvent actionEvent) throws IOException {
        LocalDateTime dt= LocalDateTime.now();
        entity.Showtime data= Data.showtime_time_selected;
        ObservableList<Order> odi = Data.Order_item;

        PDDocument doc = new PDDocument();

        for(int i=0;i<odi.size();i++){
            CurrentDateTime.setText(dt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) +" \t "+ dt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
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

            PDPage page  = new PDPage();
            doc.addPage(page);
            try {
//                convert to byte[]
                ImageIO.write(image, "png", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray(); // you have the data in byte array
                baos.close();
//
                PDPageContentStream contents = new PDPageContentStream(doc, page);
                PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc,imageInByte,"");
                contents.drawImage(pdImage,0,0);
                contents.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            doc.save(LocalDate.now().toString()+LocalTime.now().getNano()+"TK.pdf");
            doc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            DBcontroller db=new DBcontroller();
            if(Data.EditSTS){
                db.updateOrder(Data.Id_Order);
            }else{
                db.addOrderItem();
                if(!Data.order_food_item.isEmpty()){
                    db.addFood();
                }
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        editV.CancalEdit();
    }
}
