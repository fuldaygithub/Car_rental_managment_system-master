package carRentalSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import carRentalSystem.model.User;

import java.sql.Connection;
import java.sql.DriverManager;

import static javafx.fxml.FXMLLoader.load;

public class Main extends Application {
    private Connection conn;
    public static User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = load(getClass().getResource("view/start.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
