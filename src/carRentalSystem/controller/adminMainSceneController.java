package carRentalSystem.controller;

import carRentalSystem.Main;
import carRentalSystem.Scene_Changer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class adminMainSceneController extends Scene_Changer implements Initializable {
    @FXML
    private AnchorPane mainSceneRight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            changeScene("adminManageCars",mainSceneRight);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @FXML private javafx.scene.control.Button closeButton;

    @FXML
    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
        Main.currentUser = null;
    }

    @FXML
    public void addCar() throws IOException {
        changeScene("adminAddCar",mainSceneRight);
    }

    @FXML
    public void viewCarsBtn() throws IOException {
        changeScene("adminManageCars",mainSceneRight);
    }

    @FXML
    public void viewEmpBtn() throws IOException {
        changeScene("adminManageEmployees",mainSceneRight);
    }

    @FXML
    public void viewClientsBtn() throws IOException {
        changeScene("adminManageClients",mainSceneRight);
    }



}
