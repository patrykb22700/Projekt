package test3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Company Director 1.0");
        primaryStage.getIcons().add(new Image("/test3/icon.png"));
        primaryStage.setResizable(false);
        primaryStage.setMaxHeight(600.0);
        primaryStage.setMaxWidth(800.0);
        primaryStage.setX(650.0);
        primaryStage.setY(225.0);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("styleMain.fxml"));
        Pane layout = loader.load();
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("application.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        
        launch(args);

    }

}
