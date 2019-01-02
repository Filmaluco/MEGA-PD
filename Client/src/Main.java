import Core.Context;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        switch (args.length){
            case 0: break;
            case 1:
                if(args[0].toUpperCase().contains("TRUE"))
                Context.setDevMode();
            break;
            case 2:
                if(args[0].toUpperCase().contains("TRUE"))
                    Context.setDevMode();
                Context.setDefaultFolderName(args[1]);
            break;
            default:
                System.out.println("incorrect arguments: devMode deFaultFolderName");
        }


        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //--------------------------------------------------------------------------------------------------------------
        //Log.initLog();


        Parent root = FXMLLoader.load(getClass().getResource("/Views/Layouts/login.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("Views/Layouts/main.fxml"));

        Scene login = new Scene(root);

        primaryStage.setTitle("Mega PD");
        primaryStage.setScene(login);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
