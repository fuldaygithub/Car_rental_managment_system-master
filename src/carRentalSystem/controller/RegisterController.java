package carRentalSystem.controller;

//import com.jfoenix.controls.JFXPasswordField;
//import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import carRentalSystem.Scene_Changer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import carRentalSystem.Database_Connect;


public class RegisterController extends Scene_Changer implements Initializable {

    private Database_Connect connect;

//    @FXML
//    private Pane mainPane;
    @FXML
    private TextField nameF, emailF, surnameF, national_id;
    @FXML
    private PasswordField password, Confirm_password;

    @FXML
    private AnchorPane loginPane;

    public static final Pattern VALIDEMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALIDEMAIL.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connect = new Database_Connect();
    }
    @FXML
    public void backToLogin() throws IOException{

            changeScene("login",loginPane);
    }

    @FXML
    public void register_btn() throws IOException, NoSuchAlgorithmException {

        if(nameF.getText().equals("") || surnameF.getText().equals("") || national_id.getText().equals("") || emailF.getText().equals("") || password.getText().equals("")){
            connect.showAlert("Invalid details","Fill in All details correctly");
        }else if(validate(emailF.getText()) == false){
            connect.showAlert("BAD EMAIL","Enter valid email");
        }else if(national_id.getText().length() != 9){
            connect.showAlert("BAD NATIONAL ID","Enter valid national id");
        }else if(password.getText().equals(Confirm_password.getText())) {

            connect.register(nameF.getText(), emailF.getText(), surnameF.getText(), national_id.getText(), password.getText());
            changeScene("login", loginPane);
        }else{
            connect.showAlert("Bad Passwords","passwords must match");
        }
    }
}
