package carRentalSystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import carRentalSystem.Scene_Changer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class startController extends Scene_Changer implements Initializable {
    @FXML
    private AnchorPane loginPane;

//    public static  MESSAGEBAR;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            changeScene("login",loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void register() throws IOException{

        changeScene("registerCustomer",loginPane);
    }



}
