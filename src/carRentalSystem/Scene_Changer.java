package carRentalSystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class Scene_Changer {

    public void changeScene(String path, AnchorPane mainPane) throws IOException {

        AnchorPane pane = new AnchorPane();
        pane = FXMLLoader.load(getClass().getResource("../view/"+path+".fxml"));
        mainPane.getChildren().setAll(pane);
    }
}
