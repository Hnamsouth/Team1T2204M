package CheckOut;

import DBcontroller.DBcontroller;
import DBcontroller.Data;
import Main.editV;
import Rooms.Controller.Mctl;
import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import entity.Film;
import entity.Showtime;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import entity.*;
import javafx.stage.Window;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class Controller implements Initializable {
    public BorderPane BdPane;
    public Label FilmName   ,Date   ,StartEndTime  ,RoomName;
    public HBox ListSeat= new HBox(),ListCombo= new HBox();;
    public ImageView image;
    public ToggleGroup payment;
    public Label totalBF,discount,totalAT;
    public TextField voucherTxt;
    public RadioButton RBcash,RBatmCard,RBmasterCard,RBmomo,RBzalo;
    public ImageView imgATM,imgMasterCard,imgCash,imgMomo,imgZalo;
    public Label descriptionVC;
    public Button btnAddVC;
    DBcontroller db;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!Mctl.onlyFood){
            Film f= Data.film_selected;
            Showtime st= Data.showtime_time_selected;
            db= new DBcontroller();

            FilmName.setText(f.getName());
            Date.setText(st.getDate().toString());
            StartEndTime.setText(st.getTime().toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))+" ~ "+
                    st.getTime().toLocalTime().plusMinutes(f.getDuration()).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
            RoomName.setText(st.getId_room());
            image.setImage(f.getImg());

            Data.Order_item.forEach(e->{
                Text txt= new Text(e.getName_seat()+", ");
                txt.setFont(Font.font("",FontWeight.BOLD,22));
                ListSeat.getChildren().add(txt);
            });
        }
        totalBF.setText(String.valueOf((Mctl.totalseat+Mctl.totalCombo)));
        totalAT.setText(String.valueOf((Mctl.totalseat+Mctl.totalCombo)));

        Data.order_food_item.forEach(e->{
            Text txt= new Text(e.getId_combo_food()+" x"+e.getAmount()+", ");
            txt.setFont(Font.font("",18));
            ListCombo.getChildren().add(txt);
        });
        if(Data.EditSTS){
            try {
                db.checkToken(Data.order_ticket.getId_token()); //
                if(Data.token!=null){
                    checkVC();
                    voucherTxt.setText(Data.order_ticket.getId_token());
                    voucherTxt.setDisable(true);
                    btnAddVC.setText("Clear");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void submit(ActionEvent actionEvent) throws IOException {
        if(RBcash.isSelected()){
//            Main.editV.PrintInvoices();
            Main.editV.PDFcreate();
        }else if(RBatmCard.isSelected()){

        }else if(RBmasterCard.isSelected()){

        }else if(RBmomo.isSelected()){

        }else{
//            RBzalo
        }
    }

    public void addVoucher(ActionEvent actionEvent) throws Exception {
        if(btnAddVC.getText().matches("Clear")){
            voucherTxt.setDisable(false);
            voucherTxt.setText("");
            btnAddVC.setText("Apply");
        }else{
            db.checkToken(voucherTxt.getText());
            if(Data.token!=null){
                checkVC();
            }else{
                alert("not found");
            }
        }

    }

    public void checkVC() throws SQLException {
        Double ttBF=Mctl.totalseat+Mctl.totalCombo;
        token tk=Data.token;
        if(tk.getEnd_date().toLocalDate().compareTo(LocalDate.now())<0 || tk.getUsage_status()){
            alert("the token has expired or been used");
        }else{
            db.getVoucher(tk.getId_voucher());
            voucher vc= Data.voucher;
//            if(vc.getPercent()==null && vc.getCard()==null){
//                Alert al = new Alert(Alert.AlertType.WARNING);
//                al.setContentText("the token not found");
//                al.show();
//            }else{
//                System.out.println(vc.getPercent()+"\t"+ vc.getCard());
                if(vc.getPercent()!=0){
                    descriptionVC.setText(vc.getPercent()+"% off the total order");
                    discount.setText(String.valueOf(( vc.getPercent()*ttBF/100 )));
                    totalAT.setText(String.valueOf( ( ttBF-( vc.getPercent()*ttBF/100 ) ) ));
                }else{
                    descriptionVC.setText(vc.getCard()+"K off the total order");
                    discount.setText(String.valueOf(vc.getCard()));
                    totalAT.setText(String.valueOf( ( ttBF-vc.getCard() ) ));
                }
                voucherTxt.setText(tk.getId());
                voucherTxt.setDisable(true);
                btnAddVC.setText("Clear");
//            }
        }

    }

    private ImageView imgWebCamCapturedImage;
    private boolean stopCamera = false;
    private boolean runcheck = true;
    Button Print = new Button("Apply");
    private Webcam webCam = null;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private BorderPane webCamPane;

    Dialog<ButtonType> dialog=new Dialog<>();

    public void addVoucherQR(ActionEvent actionEvent) throws Exception {
        webCamPane = new BorderPane();
        webCamPane.setStyle("-fx-background-color: #232323;");
        imgWebCamCapturedImage = new ImageView();
        webCamPane.setCenter(imgWebCamCapturedImage);
        webCamPane.setStyle("-fx-background-color: #232323");

        dialog.getDialogPane().setPrefHeight(450);
        dialog.getDialogPane().setPrefWidth(620);
        dialog.getDialogPane().setGraphic(webCamPane);
;
        Print.setFont(Font.font(25));
        Print.setVisible(false);

        webCamPane.setBottom(Print);
        webCamPane.setAlignment(Print, Pos.CENTER);
        webCamPane.setMargin(Print,new Insets(25,0,25,0));
        runcheck=true;

        Print.setOnAction(e->{
            try {
                checkVC();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            if(webCam!=null ){
                if(webCam.isOpen()){
                    stopCamera = true;
                    webCam.close();
                    runcheck=false;
                    Data.setValueEmpty();
                }
            }
            dialog.close();


        });

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            dialog.close();
            stopCamera = true;
            runcheck=false;
            Data.setValueEmpty();
        });

        initializeWebCam(0);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setImageViewSize();
            }
        });

        dialog.show();
    }

    public void runcheck() throws Exception {
        runcheck=true;
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
                }
            }
            if (result != null) {
                System.out.println(result);

                db.checkToken(result.toString());
                if(Data.token!=null){
                    Print.setVisible(true);
                }
            }
            System.out.println("a");
        } while (runcheck);
    }
    protected void setImageViewSize() {
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

    public void alert(String ctn){
        Alert al= new Alert(Alert.AlertType.WARNING);
        al.setContentText(ctn);
        al.show();
    }

    public void toListFilm(ActionEvent actionEvent) throws IOException {
        Data.setValueEmpty();
        editV.ListFlim();
    }
    public void back(ActionEvent actionEvent) throws IOException {
        editV.room();
    }


}
