package ListFilm;

import DBcontroller.DBcontroller;
import Main.*;
import Rooms.Controller.Mctl;
import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import entity.Film;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;


import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import DBcontroller.Data;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import static Showtime.Controller.fromlistfilm;

public class Controller implements Initializable {
    public ListView<HashMap<Integer,String>> listv;
    public TextField tfields;
    public BorderPane root;
    DBcontroller db;

    //    @../../../img/bp2_official_poster_1_.jpg
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        get film and add to HBox
         db= new DBcontroller();
        try {   db.getAllFilm();} catch (Exception e) {  throw new RuntimeException(e);};
        showfilm(Data.list_film);
        handleListv();
        searfilm();
        System.out.println(Data.Order_item == null?"false":Data.Order_item.size());

    }

    private void searfilm() {
        tfields.textProperty().addListener((ob,ovl,nvl)->{
            ArrayList<Film> arr= Data.list_film.stream().filter(e->
                    e.getName().trim().toLowerCase().contains(nvl.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            showfilm(arr);
            listv.refresh();
        });
    }
    private void showfilm(ArrayList<Film>  lf) {
        if(lf != null){
            ObservableList<HashMap<Integer,String>> obl= FXCollections.observableArrayList();
            ArrayList<Image> arrimg = new ArrayList<Image>();
            lf.forEach(e->{
                HashMap<Integer,String> hm= new HashMap<>();
                hm.put(e.getId(),e.getName().toString());
                arrimg.add(e.getImg());
                obl.add(hm);
            });
            listv.setItems(obl);
            listv.setCellFactory(lv ->new ListCell<HashMap<Integer,String>>(){
                private ImageView img = new ImageView();
                @Override
                public void updateItem(HashMap<Integer,String> name ,boolean empty){
                    super.updateItem(name,empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    }else{
                        for(int i=0;i<obl.size();i++){
                            if(obl.get(i).equals(name)){
                                img.setImage(arrimg.get(i));
                                img.setFitHeight(100);
                                img.setFitWidth(100);
                                setText(obl.get(i).values().toString().replace("[","").replace("]",""));
                                setGraphic(img);
                            }
                        }
                    }
                }
            });
        }
//  reset bdpane Center
        root.setCenter(listv);
// turn off webcam
        if(webCam!=null ){
            if(webCam.isOpen() ){
                runcheck=false;
                webCam.close();
            }
            stopCamera = true;
        }

    }
    public void handleListv(){
        listv.setOnMouseClicked(e->{
            if(!listv.getSelectionModel().getSelectedItems().isEmpty()){

                ObservableList<HashMap<Integer,String>> obl=listv.getSelectionModel().getSelectedItems();

                Data.list_film.forEach(lf->{
                    if(lf.getId().equals(obl.get(0).keySet().hashCode())){
                        Data.film_selected=lf;
                        fromlistfilm=true;
                        try {
                            editV.showtime();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });
    }
    public void flimOfMonth(ActionEvent actionEvent) {
        try {   db.getAllFilmOfMonth();} catch (Exception e) {  throw new RuntimeException(e);};
        showfilm(Data.list_film);
        listv.refresh();
    }
    public void flimInDay(ActionEvent actionEvent) {
        try {   db.getAllFilm();} catch (Exception e) {  throw new RuntimeException(e);};
        showfilm(Data.list_film);
    }
    public void toFood(ActionEvent actionEvent) {
        Mctl.FoodSTS=true;
        try {
            db.getAllFood();
            db.getCombo_food_item();
            db.getCombo_food();
            editV.room();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if(webCam!=null ){
            if(webCam.isOpen() ){
                runcheck=false;
                webCam.close();
            }
            stopCamera = true;
        }

    }


//    check qr;
    private ImageView imgWebCamCapturedImage;
    private boolean stopCamera = false;
    private boolean runcheck = true;
    Button Print = new Button("Print Invoices");
    private Webcam webCam = null;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private BorderPane webCamPane;

    public void toCheckQR(ActionEvent actionEvent) {
        webCamPane = new BorderPane();
        webCamPane.setStyle("-fx-background-color: #ccc;");
        imgWebCamCapturedImage = new ImageView();
        webCamPane.setCenter(imgWebCamCapturedImage);
        root.setCenter(webCamPane);

        Print.setFont(Font.font(25));
        Print.setVisible(false);
        webCamPane.setBottom(Print);
        webCamPane.setAlignment(Print, Pos.CENTER);
        webCamPane.setMargin(Print,new Insets(0,0,25,0));

        Print.setOnAction(e->{
            try {
                editV.PrintInvoices();
                if(webCam!=null ){
                    if(webCam.isOpen() ){
                        runcheck=false;
                        webCam.close();
                    }
                    stopCamera = true;
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        initializeWebCam(0);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                setImageViewSize();
            }
        });
    }
    protected void setImageViewSize() {

        double height = webCamPane.getHeight();
        double width = webCamPane.getWidth();

        imgWebCamCapturedImage.setFitHeight(400);
        imgWebCamCapturedImage.setFitWidth(600);
        imgWebCamCapturedImage.prefHeight(400);
        imgWebCamCapturedImage.prefWidth(600);
        imgWebCamCapturedImage.setPreserveRatio(true);

    }
    protected void initializeWebCam(final int webCamIndex) {
        Task<Void> webCamTask = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                webCam = Webcam.getWebcams().get(webCamIndex);
                webCam.open();

                startWebCamStream();
                runcheck();
                return null;
            }
        };

        Thread webCamThread = new Thread(webCamTask);
        webCamThread.setDaemon(true);
        webCamThread.start();

    }
    public void runcheck() throws Exception {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webCam.isOpen()) {

                if ((image = webCam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {
                tfields.textProperty().set(result.getText());
                System.out.println(result.getText().substring(0,2));
                System.out.println(result.getText());
//                txt.setText(result.getText());

//                compare data in database and go to scene show order

//                and close webcam:  webCam.close();
                db.GetFilmEditDate(Integer.parseInt(result.getText().substring(0,2)));
                db.GetShowtimeEditSeat(Integer.parseInt(result.getText().substring(0,2)));
                db.OrderSeatSelected(Integer.parseInt(result.getText().substring(0,2)));

                runcheck=false;
            }
            System.out.println("a");
        } while (runcheck);
        if(Data.Order_item!=null){
            Print.setVisible(true);
            root.setBottom(Print);

        }

        return;
    }
    public void toPrint(ActionEvent actionEvent) throws IOException {
        editV.PrintInvoices();

    }
    protected void startWebCamStream() {
        stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                final AtomicReference<WritableImage> ref = new AtomicReference<>();
                BufferedImage img = null;

                while (!stopCamera) {
                    try {
                        if ((img = webCam.getImage()) != null) {

                            ref.set(SwingFXUtils.toFXImage(img, ref.get()));
                            img.flush();

                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    imageProperty.set(ref.get());
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);

    }

    public void OrderList(ActionEvent actionEvent) throws IOException {
        Parent orderl= FXMLLoader.load(Controller.class.getResource("Orderlist.fxml"));
        root.setCenter(orderl);

    }

    }


