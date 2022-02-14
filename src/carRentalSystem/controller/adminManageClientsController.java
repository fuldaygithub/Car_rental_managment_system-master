package carRentalSystem.controller;

import carRentalSystem.Database_Connect;
import carRentalSystem.Scene_Changer;
import carRentalSystem.model.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import javafx.scene.control.ListView;
import carRentalSystem.model.RegisteredUser;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;

public class adminManageClientsController extends Scene_Changer implements Initializable {
    @FXML
    private AnchorPane mainSceneRight;

    @FXML
    private ListView<RegisteredUser> clientsListView;

    @FXML
    private Label Name,lfrom,luntil,lcost,OverdueLabel,daysLabel, t_costLabel;

    @FXML
    private Text omang,email,car_name,sdate,rdate,cost, OverdueL,pula_total_cost;

    @FXML
    private ImageView car_image;

    private Database_Connect connect;

    ResultSet borrwored_car = null;

    public String b_car_id;



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        connect = new Database_Connect();
        ObservableList<RegisteredUser> clients = connect.getClientsFromDB();

        clientsListView.setItems(clients);


        //select first client
        clientsListView.getSelectionModel().selectFirst();
        borrwored_car =  connect.getBorrowedCarForAdmin(clientsListView.getSelectionModel().getSelectedItem().getId());
        Name.setText(clientsListView.getSelectionModel().getSelectedItem().getName());
        omang.setText(clientsListView.getSelectionModel().getSelectedItem().getOmang());
        email.setText(clientsListView.getSelectionModel().getSelectedItem().getEmail());



        //    display details of car borrowed by client

        try {
            borrwored_car.beforeFirst();
            if(!borrwored_car.isBeforeFirst()){
                System.out.println("clienr has no borrwed car "+clientsListView.getSelectionModel().getSelectedItem().getId());
                car_name.setText("HAS NOT BORROWED ANY CAR NOW");
                car_image.setImage(null);

                sdate.setText("");
                rdate.setText("");
                cost.setText("");
                lfrom.setText("");
                luntil.setText("");
                OverdueLabel.setText("");
                daysLabel.setText("");
                t_costLabel.setText("");
                lcost.setText("");
                OverdueL.setText("");
                pula_total_cost.setText("");

                borrwored_car = null; //untested
            }else{
                System.out.println("provideing borrowed car details");
                borrwored_car.first();
                BufferedImage image = decodeToImage(borrwored_car.getString("image"));
               Image image2 = SwingFXUtils.toFXImage(image, null);
                car_image.setImage(image2);

                car_name.setText(borrwored_car.getString("car_name"));
                sdate.setText(borrwored_car.getString("book_from"));
                rdate.setText(borrwored_car.getString("book_until"));

                lfrom.setText("FROM:");
                luntil.setText("UNTIL:");
                lcost.setText("COST:");
                OverdueLabel.setText("Overdue by:");
                daysLabel.setText("Day(s)");
                t_costLabel.setText("Total cost:");

                //            calculate cost using number of days
                LocalDate dateBefore = borrwored_car.getDate("book_from").toLocalDate();
                LocalDate dateAfter = borrwored_car.getDate("book_until").toLocalDate();

                Period intervalPeriod = Period.between(dateBefore, dateAfter);
                int num_days = intervalPeriod.getDays();
                if(num_days == 0){
                    num_days = 1;
                }
                double cumulative_cost = borrwored_car.getDouble("price") * num_days;
                cost.setText("Rmb "+cumulative_cost+"0");

                //overdue costs
//
                LocalDate today = LocalDate.now();
                Period intervalPeriod3 = Period.between(dateAfter, today);
                int num_overdue_days = intervalPeriod3.getDays();

                if(num_overdue_days <= 0){
//                    total cost remains that same
                    OverdueL.setText("0");
                    pula_total_cost.setText("Rmb "+cumulative_cost+"0");
                }else {
                    OverdueL.setText(num_overdue_days+"");
                    double pluse_overdue_cost = cumulative_cost + (borrwored_car.getDouble("price") * num_overdue_days);
                    pula_total_cost.setText("Rmb "+pluse_overdue_cost+"0");

                }

                b_car_id = borrwored_car.getString("car_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }



    @FXML public void handleMouseClick(MouseEvent arg0) {
        Name.setText(clientsListView.getSelectionModel().getSelectedItem().getName());
        omang.setText(clientsListView.getSelectionModel().getSelectedItem().getOmang());
        email.setText(clientsListView.getSelectionModel().getSelectedItem().getEmail());



//    display details of car borrowed by client
        borrwored_car =  connect.getBorrowedCarForAdmin(clientsListView.getSelectionModel().getSelectedItem().getId());

        try {
            borrwored_car.beforeFirst();
            if(!borrwored_car.isBeforeFirst()){
                System.out.println("clienr has no borrwed car "+clientsListView.getSelectionModel().getSelectedItem().getId());
                car_name.setText("HAS NOT BORROWED ANY CAR NOW");
                car_image.setImage(null);

                sdate.setText("");
                rdate.setText("");
                cost.setText("");
                lfrom.setText("");
                luntil.setText("");
                OverdueLabel.setText("");
                daysLabel.setText("");
                t_costLabel.setText("");
                lcost.setText("");
                OverdueL.setText("");
                pula_total_cost.setText("");

                borrwored_car = null; //untested

            }else{

                System.out.println("provideing borrowed car details");
                borrwored_car.first();
                BufferedImage image = decodeToImage(borrwored_car.getString("image"));
                Image image2 = SwingFXUtils.toFXImage(image, null);
                car_image.setImage(image2);

                car_name.setText(borrwored_car.getString("car_name"));
                sdate.setText(borrwored_car.getString("book_from"));
                rdate.setText(borrwored_car.getString("book_until"));

                lfrom.setText("FROM:");
                luntil.setText("UNTIL:");
                lcost.setText("COST:");
                OverdueLabel.setText("Overdue by:");
                daysLabel.setText("Day(s)");
                t_costLabel.setText("Total cost:");

                //            calculate cost using number of days
                LocalDate dateBefore = borrwored_car.getDate("book_from").toLocalDate();
                LocalDate dateAfter = borrwored_car.getDate("book_until").toLocalDate();

                Period intervalPeriod = Period.between(dateBefore, dateAfter);
                int num_days = intervalPeriod.getDays();
                if(num_days == 0){
                    num_days = 1;
                }
                double cumulative_cost = borrwored_car.getDouble("price") * num_days;
                cost.setText("Rmb "+cumulative_cost+"0");

                //overdue costs
//
                LocalDate today = LocalDate.now();
                Period intervalPeriod3 = Period.between(dateAfter, today);
                int num_overdue_days = intervalPeriod3.getDays();

                if(num_overdue_days <= 0){
//                    total cost remains that same
                    OverdueL.setText("0");
                    pula_total_cost.setText("Rmb "+cumulative_cost+"0");
                }else {
                    OverdueL.setText(num_overdue_days+"");
                    double pluse_overdue_cost = cumulative_cost + (borrwored_car.getDouble("price") * num_overdue_days);
                    pula_total_cost.setText("Rmb "+pluse_overdue_cost+"0");

                }

                b_car_id = borrwored_car.getString("car_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void returnCarBtn(){
        if(borrwored_car != null) {
            connect.returnCar(b_car_id);

            System.out.println("car returned " + clientsListView.getSelectionModel().getSelectedItem().getId());
            car_name.setText("HAS NOT BORROWED ANY CAR NOW");
            car_image.setImage(null);

            sdate.setText("");
            rdate.setText("");
            cost.setText("");
            lfrom.setText("");
            luntil.setText("");
            OverdueLabel.setText("");
            daysLabel.setText("");
            t_costLabel.setText("");
            lcost.setText("");
            OverdueL.setText("");
            pula_total_cost.setText("");
            borrwored_car = null;
            connect.showAlert("CLEARED", "car returned");
        }
    }





}
