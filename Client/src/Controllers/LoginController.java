package Controllers;

import Core.Context;
import Core.ServerData;
import Core.UserData;
import Helpers.ServerRESTRequest;
import Models.ServerInfo;
import Modules.Connection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    @FXML
    private Label lbWarning;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: Update visual changes
        //setWarning();
        ObservableList<String> list = FXCollections.observableArrayList();
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
        ServerInfo serverInfo = null;
        try {
            serverInfo = ServerRESTRequest.getFirst(true);
            Context.setUser(new UserData());
            Context.setConnection(new Connection(new ServerData(serverInfo.getAddress(), serverInfo.getPort())));
            Context.getConnection().login();
            Context.getUser().setNotificationSocket(Context.getConnection().registerNotificationPort(0), true);
            closeStage();
            loadMain();
        }catch (Exception e){
            setWarning(e.getMessage());
        }
    }

    @FXML
    private void userLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        ServerInfo serverInfo = null;
        try {
            serverInfo = ServerRESTRequest.getFirst(true);
            Context.setConnection(new Connection(new ServerData(serverInfo.getAddress(), serverInfo.getPort())));
            Context.getConnection().login(username, password);
            Context.getUser().setNotificationSocket(Context.getConnection().registerNotificationPort(0), true);
            closeStage();
            loadMain();
        }catch (Exception e){
            setWarning(e.getMessage());
        }
    }

    public void setWarning(String warning){
        lbWarning.setText(warning);
    }
}
