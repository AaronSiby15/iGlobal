package se2203b.iGlobal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * @author Abdelkader Ouda
 */
public class IGlobalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(IGlobalApplication.class.getResource("iGlobal-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("iGlobal");
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}