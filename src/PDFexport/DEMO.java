//package PDFexport;
//
//
//import javafx.application.Application;
//import javafx.embed.swing.SwingFXUtils;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.SnapshotParameters;
//import javafx.scene.control.Label;
//
//import javafx.scene.image.ImageView;
//import javafx.scene.image.WritableImage;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//
//import java.io.IOException;
//
//public class DEMO extends Application {
//    public AnchorPane CinemaTicket;
//    public Label CurrentDateTime;
//    public Label FilmName;
//    public Label Showtime;
//    public Text RoomName;
//    public Text NameSeat;
//    public Text TypeSeat;
//    public Text Price;
//    public ImageView imgV;
//
//    public static void main(String[] args)throws IOException{
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        Parent root= FXMLLoader.load(getClass().getResource("Printdemo.fxml"));
//        root.getStylesheets().add(getClass().getResource("/Main/RootStyle.css").toExternalForm());
//        stage.setScene(new Scene(root));
//        stage.show();
//
//
//
////        String currentPath="F:/Github/Team1/Team1T2204M/src/PDFexport";
////        String pdfPath=currentPath+"/InsertImage.pdf";
////        PdfWriter writer = new PdfWriter(pdfPath);
////        // the path
////        PdfDocument pdfDoc = new PdfDocument(writer);
////        Document doc = new Document(pdfDoc);
////        ImageData imageData = ImageDataFactory.create( currentPath + "/bgticket.png");
////        com.itextpdf.layout.element.Image img = new com.itextpdf.layout.element.Image(imageData);
//////        BufferedImage o= ImageIO.read(f);
////        doc.add(img);
////        doc.close();
//
//    }
//
//    public void click(ActionEvent actionEvent) throws IOException {
//        WritableImage snapshot = CinemaTicket.snapshot(new SnapshotParameters(), null);
//        BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "png", baos);
//        baos.flush();
//        byte[] imageInByte = baos.toByteArray(); // you have the data in byte array
//        baos.close();
////   PDF box
//        PDDocument doc = new PDDocument();
//        PDPage page  = new PDPage();
//        doc.addPage(page);
//        PDPageContentStream contents = new PDPageContentStream(doc, page);
//
//        PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc,imageInByte,"");
//        contents.drawImage(pdImage,0,0);
//        contents.close();
//        doc.save("pdf_with_text.pdf");
//        doc.close();
//
//    }
//}
