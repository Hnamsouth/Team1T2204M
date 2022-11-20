package Rooms.Controller;

import DBcontroller.Data;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import entity.OrderFoodItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FoodController implements Initializable {

    public GridPane GrPane;
    String description="";
    private static int a=0;
    boolean check=false;
    public  static ArrayList<Button> MNPL=new ArrayList<Button>();
    public  static ObservableList<ArrayList<Button>> MinusAndPlus = FXCollections.observableArrayList();

    public  static ArrayList<HBox> arrHB=new ArrayList<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int s=0;
        System.out.println(Data.Combo_food.size());
        for(int i=0;i< (Data.Combo_food.size()%3==0?Data.Combo_food.size()/3:(Data.Combo_food.size()/3)+1);i++){
            for(int j = 0; j<(Math.min(Data.Combo_food.size() - s+1, 3)); j++){

                VBox vb= new VBox();
                Label nameCB = new Label();nameCB.setText(Data.Combo_food.get(s).getId());
                nameCB.setPrefHeight(34);nameCB.setPrefWidth(355);nameCB.setTextFill(Color.WHITE);
                nameCB.setFont(Font.font("",FontWeight.BOLD,23));
                vb.getChildren().add(nameCB);

                description="";
                Label nameDSCT = new Label();
                Data.Combo_food.get(s).getCombo_food_item().forEach(e->{
                    description +=(description.equals("")?"":" + ")+ e.getAmount() +" "+ e.getId_food();
                });
                nameDSCT.setText(description);
                nameDSCT.setPrefHeight(57);nameDSCT.setPrefWidth(355);nameDSCT.setTextFill(Color.valueOf("#d0d0d0"));nameDSCT.setFont(Font.font(19));
                nameDSCT.setWrapText(true);
                vb.getChildren().add(nameDSCT);


                HBox hb= new HBox();
                Label namePrice = new Label();
                namePrice.setText("Price");
                namePrice.setPrefHeight(30);namePrice.setPrefWidth(58);namePrice.setTextFill(Color.WHITE);
                namePrice.setFont(Font.font(18));
                hb.getChildren().add(namePrice);

                Text price = new Text(String.valueOf(Data.Combo_food.get(s).getPrice()));price.setFill(Color.valueOf("#f5f5f5"));
                price.setUnderline(true);price.setFont(Font.font("", FontWeight.BOLD,25));
                hb.getChildren().add(price);

                Button mn = new Button();
                mn.setPrefHeight(35);mn.setPrefWidth(35); mn.setStyle("-fx-background-color: #9C793C");
                 FontAwesomeIcon icon1= new FontAwesomeIcon();
                 icon1.setGlyphName("MINUS");icon1.setFill(Color.WHITE);icon1.setSize("1.5em");
                 mn.setGraphic(icon1);
                hb.getChildren().add(mn);


                Label amount = new Label();
                amount.setPrefHeight(42);amount.setPrefWidth(48);
                amount.getStyleClass().add("inp-text");amount.setTextFill(Color.WHITE);amount.setFont(Font.font(30));amount.setAlignment(Pos.CENTER);


                amount.setText("0");
                if(!Data.order_food_item.isEmpty()){
                    int finalS1 = s;
                    Data.order_food_item.forEach(e->{
                        if(e.getId_combo_food().equals(Data.Combo_food.get(finalS1).getId())){
                            amount.setText(String.valueOf(e.getAmount()));
                        }
                    });
                }

                hb.getChildren().add(amount);

                Button plus = new Button();
                plus.setPrefHeight(35);plus.setPrefWidth(35); plus.setStyle("-fx-background-color: #9C793C");
                FontAwesomeIcon icon2= new FontAwesomeIcon();
                icon2.setGlyphName("PLUS");icon2.setFill(Color.WHITE);icon2.setSize("1.5em");
                plus.setGraphic(icon2);
                hb.getChildren().add(plus);

                int finalS = s;
                mn.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
                            int am=Integer.parseInt(amount.getText());
                            if(!Data.order_food_item.isEmpty()) {
                                for(int od=0; od<Data.order_food_item.size(); od++){
                                    if(am>0){
                                        if((Data.order_food_item.get(od).getId_combo_food().equals(Data.Combo_food.get(finalS).getId()))){
                                            if(am==1){
                                                Data.order_food_item.remove(od);
                                                amount.setText(String.valueOf(am-1));
                                            }else{
                                                OrderFoodItem odf3=new OrderFoodItem(null,Data.Combo_food.get(finalS).getId(),description,null,am-1,Data.Combo_food.get(finalS).getPrice());
                                                Data.order_food_item.remove(od);
                                                Data.order_food_item.add(odf3);
                                                amount.setText(String.valueOf(am-1));
                                            }

                                        }
                                    }
                                }
                            }
                });

                plus.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
                    check=false;
                    int am=Integer.parseInt(amount.getText());
                    if(!Data.order_food_item.isEmpty()){
                        for(int od=0; od<Data.order_food_item.size(); od++){
                            if((Data.order_food_item.get(od).getId_combo_food().equals(Data.Combo_food.get(finalS).getId()))){
                                OrderFoodItem odf3=new OrderFoodItem(null,Data.Combo_food.get(finalS).getId(),description,null,am+1,Data.Combo_food.get(finalS).getPrice());
                                Data.order_food_item.remove(od);
                                Data.order_food_item.add(odf3);
                                amount.setText(String.valueOf(am+1));
                                check=true;
                            }
                        }
                        if(!check){
                            OrderFoodItem odf2=new OrderFoodItem(null,Data.Combo_food.get(finalS).getId(),description,null,1,Data.Combo_food.get(finalS).getPrice());
                            Data.order_food_item.add(odf2);
                            amount.setText("1");
                        }
                    }else{
                        OrderFoodItem odf2=new OrderFoodItem(null,Data.Combo_food.get(finalS).getId(),description,null,1,Data.Combo_food.get(finalS).getPrice());
                        Data.order_food_item.add(odf2);
                        amount.setText("1");
                    }
//                    System.out.println(Data.order_food_item.size());
                });

                HBox.setMargin(price,new Insets(0,15,0,0));

                MNPL.add(mn);MNPL.add(plus);

                MinusAndPlus.add(s,MNPL);
                hb.setAlignment(Pos.BOTTOM_LEFT);
                hb.setSpacing(8);
                vb.getChildren().add(hb);
                arrHB.add(hb);

                GrPane.add(vb,j,i,1,1);
                a++;
                s++;
            }
            GrPane.setPadding(new Insets(10,15,0,15));
        }


    }

}
