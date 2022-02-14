package carRentalSystem.controller;

import carRentalSystem.Scene_Changer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import carRentalSystem.Database_Connect;

import javafx.stage.FileChooser;
import java.io.File;
import javafx.stage.Stage;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;

public class adminAddCarController extends Scene_Changer implements Initializable {
    @FXML
    private AnchorPane mainSceneRight;

    @FXML
    private TextField car_name, car_model, car_price;

    private Database_Connect connect;

    @FXML
    private Button ImgFileBtn;

    @FXML
    private ImageView imgg;


    public Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connect = new Database_Connect();




    }

    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    String image = null;
    public  void uploadImg() throws IOException{

        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {



            String imgPath = file.getPath();

            BufferedImage img = ImageIO.read(new File(imgPath));
             image = encodeToString( img , "jpg");
//            System.out.println(image);

        }

    }

    public void addCarBtn(){
        Double price = Double.parseDouble(car_price.getText());
        connect.addCartoDB(car_name.getText(), car_model.getText(),price, image );
        car_name.setText("");
        car_model.setText("");
        car_price.setText("");
        image = null;

    }





}
