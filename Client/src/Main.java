import Controllers.View.LoginController;
import Core.Context;
import PD.Core.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {


        //--------------------------------------------------------------------------------------------------------------
        Log.initLog();
        Context context = new Context();


        //--------------------------------------------------------------------------------------------------------------
        // Starts Visual
        //--------------------------------------------------------------------------------------------------------------
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/Layouts/login.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        LoginController controller = fxmlLoader.<LoginController>getController();

        if(context.getServer() == null){
            controller.setWarning("Please check your internet activity");
        }

        controller.setContext(context);

        Scene login = new Scene(root);
        primaryStage.setTitle("Mega PD");
        primaryStage.setScene(login);
        primaryStage.setResizable(false);

        primaryStage.show();

    }
}
