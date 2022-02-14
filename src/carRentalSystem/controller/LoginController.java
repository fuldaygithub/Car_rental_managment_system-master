package carRentalSystem.controller;

import carRentalSystem.Database_Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import carRentalSystem.Scene_Changer;
import carRentalSystem.model.User;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController extends Scene_Changer implements Initializable {
    @FXML
    private AnchorPane loginPane;

    private Database_Connect connect;


    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private CheckBox admin_checkbox;

    public String currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        connect = new Database_Connect();
    }
    @FXML
    public void register() throws IOException{

            changeScene("registerCustomer",loginPane);

    }
    @FXML
    public void settings() throws IOException{

        changeScene("settings",loginPane);


    }
    @FXML
    public void createTables() throws IOException{

        connect.createTables();//method to creat tael and admin insert

        connect.showAlert("Table creation","created tables");
    }

    @FXML
    public void login_btn() throws IOException, SQLException, NoSuchAlgorithmException {
        User user;
        String hashed_pass = connect.cryptWithMD5(password.getText());
        if(!admin_checkbox.isSelected()){
            user = connect.loginCustomer(email.getText(), password.getText());
            if(user != null) {

                if(hashed_pass.equals(user.getPassword())) {
                    Parent root = FXMLLoader.load(getClass().getResource("../view/mainScene.fxml"));

                    Stage primaryStage = new Stage();
                    primaryStage.setTitle("Customer Home");
                    primaryStage.setScene(new Scene(root, 900, 400));
                    primaryStage.show();
                    password.setText("");
                }else{
                    connect.showAlert("BAD USER","INVALID CREDENTIALS");
                }
            }else{
                connect.showAlert("BAD USER","INVALID CREDENTIALS");
            }
        }else if(admin_checkbox.isSelected()){

            user = connect.loginAdmin(email.getText(), password.getText());
            if(user != null) {
                if(hashed_pass.equals(user.getPassword())) {
                    Parent root = FXMLLoader.load(getClass().getResource("../view/adminMainScene.fxml"));

                    Stage primaryStage = new Stage();
                    primaryStage.setTitle("Admin Home");
                    primaryStage.setScene(new Scene(root, 900, 400));
                    primaryStage.show();
                    password.setText("");
                }else{
                    connect.showAlert("BAD USER","INVALID CREDENTIALS");
                }
            }else{
                connect.showAlert("BAD USER","INVALID CREDENTIALS");
            }
        }





    }





}
