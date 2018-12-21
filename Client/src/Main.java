import Helpers.ServerRESTRequest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Views/Layouts/login.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("Views/Layouts/main.fxml"));

        ServerRESTRequest.getFirst(true);

        Scene login = new Scene(root);

        primaryStage.setTitle("Mega PD");
        primaryStage.setScene(login);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
