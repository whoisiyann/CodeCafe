package codecafe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/start_screen.fxml"));
        Parent root = loader.load();

        // Create scene
        Scene scene = new Scene(root);

        // Set title and show stage
        primaryStage.setTitle("Code Coffee");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);

    }
}