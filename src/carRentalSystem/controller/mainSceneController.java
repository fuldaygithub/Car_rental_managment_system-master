package carRentalSystem.controller;

import carRentalSystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import carRentalSystem.Scene_Changer;
import carRentalSystem.Database_Connect;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class mainSceneController extends Scene_Changer implements Initializable {
    @FXML
    private AnchorPane mainSceneRight;

    @FXML
    private Label currentUserText;

    @FXML private javafx.scene.control.Button closeButton;

    @FXML
    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
        Main.currentUser = null;
    }



    private Database_Connect connect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            changeScene("customerAvailbleCars",mainSceneRight);

            currentUserText.setText(Main.currentUser.getName());
//            System.out.print(connect.currentUser.getEmail());
//            currentUserText.setText(connect.currentUser.getEmail());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bookedCar() throws IOException {
        changeScene("customerBookedCars",mainSceneRight);
    }

    @FXML
    public void availableCars() throws IOException {
        changeScene("customerAvailbleCars",mainSceneRight);
    }

    @FXML
    public void logout() throws IOException {
        changeScene("customerAvailbleCars",mainSceneRight);
    }



}
