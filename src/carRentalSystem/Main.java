package carRentalSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import carRentalSystem.model.User;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main extends Application {
    private Connection conn;
    public static User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_db?autoReconnect=true","root", "");
               System.out.println("conncet");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource("view/start.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
