package carRentalSystem.controller;

import carRentalSystem.Database_Connect;
import carRentalSystem.Scene_Changer;
import carRentalSystem.model.Car;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.time.Period;


public class CustomerBookedCarController extends Scene_Changer implements Initializable {

    @FXML
    private AnchorPane mainSceneRight;

    @FXML
    private ImageView carImage;



    private Database_Connect connect;

    @FXML
    private Text car_name,total_cost_value, take_car_date, return_car_date, OverdueL, pula_total_cost;

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






    @Override
    public void initialize(URL location, ResourceBundle resources) {

        connect = new Database_Connect();

        ResultSet rs =  connect.getBookedCarsFromDB();

        try{
            rs.beforeFirst();
            if(rs.isBeforeFirst()) {

                rs.first();
                //            calculate cost using number of days
                LocalDate dateBefore = rs.getDate("book_from").toLocalDate();
                LocalDate dateAfter = rs.getDate("book_until").toLocalDate();

                Period intervalPeriod = Period.between(dateBefore, dateAfter);
                int num_days = intervalPeriod.getDays();
                if(num_days == 0){
                    num_days = 1;
                }
                double cumulative_cost = rs.getDouble("price") * num_days;

                //overdue costs
//                LocalDate today = LocalDate.of(2018, 6, 3);
                LocalDate today = LocalDate.now();
                Period intervalPeriod3 = Period.between(dateAfter, today);
                int num_overdue_days = intervalPeriod3.getDays();

                if(num_overdue_days <= 0){
//                    total cost remains that same
                    OverdueL.setText("0");
                    pula_total_cost.setText("Rmb "+cumulative_cost+"0");
                }else {
                    OverdueL.setText(num_overdue_days+"");
                    double pluse_overdue_cost = cumulative_cost + (rs.getDouble("price") * num_overdue_days);
                    pula_total_cost.setText("Rmb "+pluse_overdue_cost+"0");

                }




//            update display values
                car_name.setText(rs.getString("car_name"));
                total_cost_value.setText("Rmb " + cumulative_cost + "0");
                take_car_date.setText(rs.getString("book_from"));
                return_car_date.setText(rs.getString("book_until"));


                BufferedImage image = decodeToImage(rs.getString("image"));
                Image image2 = SwingFXUtils.toFXImage(image, null);
                carImage.setImage(image2);


            }else{
                car_name.setText("NO BOOKED CAR");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
