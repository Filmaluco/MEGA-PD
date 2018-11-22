package Controllers.View;

import Core.Context;
import Modules.Connection;
import PD.Core.PasswordHasher;
import PD.Core.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;
import sun.applet.Main;

import java.awt.*;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class LoginController implements Initializable {

    Context context;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnGuestLogin;

    @FXML
    private Label lbWarning;

    @Override
    @FXML public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {

            ObservableList<String> list = FXCollections.observableArrayList();

        });
    }

    @FXML
    private void userLogin(ActionEvent event) {

        if(context.getServer() == null){
            this.setWarning("No connection");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.length() == 0 || password.length() == 0){
            this.setWarning("Please insert a username/password");
            return;
        }

        password = PasswordHasher.generateSecurePassword(password);

        context.setUser(new User(
                username,
                password,
                "/",
                context.getPingSocket().getLocalPort(),
                context.getNotificationServerSocket().getLocalPort(),
                context.getChatServerSocket().getLocalPort(),
                context.getFileManagerServerSocket().getLocalPort()
        ));

        Connection conn = new Connection(context.getServer());


        if (conn.login(context.getUser())){
            closeStage();
            loadMain();
        }else {
            this.setWarning("Please check your credentials");
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
    private void guestLogin(ActionEvent event) {
        //TODO: Implement
    }

    public void setWarning(String warning){
        lbWarning.setText(warning);
    }

    public void setContext(Context context){ this.context = context;}
}
