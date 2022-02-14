package carRentalSystem.controller;

import carRentalSystem.Database_Connect;
import carRentalSystem.Scene_Changer;
import carRentalSystem.model.Car;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ListView;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import carRentalSystem.model.Car;


public class adminManageCarController extends Scene_Changer implements Initializable {
    @FXML
    private AnchorPane mainSceneRight;

    @FXML
     private ListView<Car> adminCarsListView;

    @FXML
    private Button mainSceneBookCarBtn;

    private Database_Connect connect;

    @FXML
    private Text carName,Price_value,Stauts_value;

    @FXML
    private TextField change_Price;

    @FXML
    private Button bookCar_bnt;

    private double price;
    private String car_Name , image;

    @FXML
    private ImageView carImage;

    public ObservableList<Car> cars;

    private int currentlySelectedCar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connect = new Database_Connect();

        cars = connect.getCarsFromDB();
        adminCarsListView.setItems(cars);

        adminCarsListView.getSelectionModel().selectFirst();
        carName.setText(adminCarsListView.getSelectionModel().getSelectedItem().getName());
        Price_value.setText("Rmb " + adminCarsListView.getSelectionModel().getSelectedItem().getPrice() + " per day");
        Stauts_value.setText(adminCarsListView.getSelectionModel().getSelectedItem().getStatus());
        currentlySelectedCar = adminCarsListView.getSelectionModel().getSelectedItem().getCar_id();
       image = adminCarsListView.getSelectionModel().getSelectedItem().getImage();

        BufferedImage image = decodeToImage(adminCarsListView.getSelectionModel().getSelectedItem().getImage());

        Image image2 = SwingFXUtils.toFXImage(image, null);

        carImage.setImage(image2);

        price = adminCarsListView.getSelectionModel().getSelectedItem().getPrice();
        car_Name = adminCarsListView.getSelectionModel().getSelectedItem().getName();
        //end of selecting first item

    }
//    }
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

    @FXML
    public void deleteBTN() throws IOException {
        connect.deleteCarFromDB(currentlySelectedCar);
        connect = new Database_Connect();
        cars = connect.getCarsFromDB();
        adminCarsListView.setItems(cars);
        System.out.println("delete car ");
    }

    public void  changePrice(){

        Double priceC = Double.parseDouble(change_Price.getText());
        connect.changePrice(priceC ,currentlySelectedCar);
        connect.showAlert("Price Change","CHANGED PRICE");
        Price_value.setText("Rmb "+ change_Price.getText() + " per day");
        change_Price.setText("");
//        changeScene("adminManageCars",mainSceneRight);
        cars = connect.getCarsFromDB();
        adminCarsListView.setItems(cars);

    }


    public void HandlMouseClick(MouseEvent mouseEvent) {
        carName.setText(adminCarsListView.getSelectionModel().getSelectedItem().getName());
        Price_value.setText("Rmb "+adminCarsListView.getSelectionModel().getSelectedItem().getPrice() + " per day");
        Stauts_value.setText(adminCarsListView.getSelectionModel().getSelectedItem().getStatus());
        currentlySelectedCar = adminCarsListView.getSelectionModel().getSelectedItem().getCar_id();
        image = adminCarsListView.getSelectionModel().getSelectedItem().getImage();

        BufferedImage image = decodeToImage(adminCarsListView.getSelectionModel().getSelectedItem().getImage());

        Image image2 = SwingFXUtils.toFXImage(image, null);

        carImage.setImage(image2);

        price = adminCarsListView.getSelectionModel().getSelectedItem().getPrice();
        car_Name = adminCarsListView.getSelectionModel().getSelectedItem().getName();

    }
}
