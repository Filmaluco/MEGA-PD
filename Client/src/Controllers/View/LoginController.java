package Controllers.View;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnGuestLogin;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: Update visual changes
    }

    @FXML
    public void userLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        //TODO: Implement real authentication
        if (username.equals("MegaPD") && password.equals("PD2018")){
            closeStage();
            loadMain();
        }
    }

    private void closeStage(){
        ((Stage)usernameField.getScene().getWindow()).close();
    }

    void loadMain(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Views/Layouts/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("MegaPD");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void guestLogin(ActionEvent event) {
        //TODO: Implement
    }
}
