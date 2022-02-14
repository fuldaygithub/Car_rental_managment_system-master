package carRentalSystem.controller;


import carRentalSystem.Database_Connect;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import carRentalSystem.Scene_Changer;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;


public class SettingsController extends Scene_Changer implements Initializable {

    private Database_Connect connect;

    @FXML
    private AnchorPane loginPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        connect = new Database_Connect();
    }

    @FXML
    public void createTables() throws IOException, SQLException, NoSuchAlgorithmException {
        LoginController log =new LoginController();
        log.login_btn();

        connect.createTables();
        connect.showAlert("Table creation","created tables");
    }

    @FXML
    public void backToLogin() throws IOException{
        changeScene("login",loginPane);
    }
}
