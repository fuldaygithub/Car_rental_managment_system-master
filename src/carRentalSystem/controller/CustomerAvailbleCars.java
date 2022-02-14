package carRentalSystem.controller;

import carRentalSystem.Database_Connect;
import carRentalSystem.Scene_Changer;
import carRentalSystem.model.Car;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;


public class CustomerAvailbleCars extends Scene_Changer implements Initializable {

    @FXML
    private AnchorPane mainSceneRight;

    @FXML
    private ListView<Car> carListView;

    @FXML
    private Text carName,Price_value,Stauts_value;

    @FXML
    private Button bookCar_bnt;


    @FXML
    private DatePicker book_from_date,book_until_date;

    @FXML
    private ImageView carImage;

    private double price;
    private String car_Name , image;

    private int currentlySelectedCar;

    private Database_Connect connect;




//    ListView list = new ListView(cars);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        connect = new Database_Connect();
        ObservableList<Car> cars = connect.getCarsFromDB();
        carListView.setItems(cars);



        //select first item
        carListView.getSelectionModel().selectFirst();
        carName.setText(carListView.getSelectionModel().getSelectedItem().getName());
        Price_value.setText("Rmb "+carListView.getSelectionModel().getSelectedItem().getPrice() + " per day");
        Stauts_value.setText(carListView.getSelectionModel().getSelectedItem().getStatus());
        currentlySelectedCar = carListView.getSelectionModel().getSelectedItem().getCar_id();
        image = carListView.getSelectionModel().getSelectedItem().getImage();

        BufferedImage image = decodeToImage(carListView.getSelectionModel().getSelectedItem().getImage());

       Image image2 = SwingFXUtils.toFXImage(image, null);

        carImage.setImage(image2);

        price = carListView.getSelectionModel().getSelectedItem().getPrice();
        car_Name = carListView.getSelectionModel().getSelectedItem().getName();
        //end of selecting first item

//check if client already has booked car or if the car has been booked by other client
        try {
            System.out.println("already booked = "+connect.alreadyBooked());
            System.out.println("status= "+ carListView.getSelectionModel().getSelectedItem().getStatus());

            if(connect.alreadyBooked() || carListView.getSelectionModel().getSelectedItem().getStatus().equals("Booked")){
                bookCar_bnt.setDisable(true);
            }

        } catch (Exception e) {
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

        try {
            System.out.println("status= "+ carListView.getSelectionModel().getSelectedItem().getStatus());

            if(connect.alreadyBooked() || carListView.getSelectionModel().getSelectedItem().getStatus().equals("Booked")){
                bookCar_bnt.setDisable(true);
            }else{
                bookCar_bnt.setDisable(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

       carName.setText(carListView.getSelectionModel().getSelectedItem().getName());
       Price_value.setText("Rmb "+carListView.getSelectionModel().getSelectedItem().getPrice() + " per day");
        Stauts_value.setText(carListView.getSelectionModel().getSelectedItem().getStatus());
        currentlySelectedCar = carListView.getSelectionModel().getSelectedItem().getCar_id();
        image = carListView.getSelectionModel().getSelectedItem().getImage();

        BufferedImage image = decodeToImage(carListView.getSelectionModel().getSelectedItem().getImage());

        Image image2 = SwingFXUtils.toFXImage(image, null);

        carImage.setImage(image2);

        price = carListView.getSelectionModel().getSelectedItem().getPrice();
        car_Name = carListView.getSelectionModel().getSelectedItem().getName();

    }

    public void book_btn(){

        LocalDate today = LocalDate.now();
        LocalDate start_date = book_from_date.getValue();
        LocalDate return_date = book_until_date.getValue();

        if(start_date == null || return_date == null){
            connect.showAlert("BAD DATE","Please select dates");
           return;
        }
        Period intervalPeriod = Period.between(today, start_date);
        int sDate = intervalPeriod.getDays();

        Period intervalPeriod2 = Period.between(start_date, return_date);
        int rDate = intervalPeriod2.getDays();


            if(sDate < 0){

                connect.showAlert("BAD DATE","You can not choose date that PASSED");
                System.out.println("you can not choose date that passed");
            }else if(rDate < 0){

                connect.showAlert("BAD DATE","you can not choose date that is less than booked date");
                System.out.println("you can not choose date that is less than booked date");
            }else{
                connect.bookCar(currentlySelectedCar , car_Name ,price , start_date, return_date ,image);
//                disable button -will edit to check if user already booked thne disable
                connect.showAlert("CAR BOOKED","Successfully Booked car");
                bookCar_bnt.setDisable(true);
            }

    }


}
