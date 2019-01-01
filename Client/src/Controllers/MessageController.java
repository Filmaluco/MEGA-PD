package Controllers;

import Core.Context;
import Helpers.ListViewSetupHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageController implements Initializable {

    private String selectedUser = "none";

    private String currentUser;

    @FXML
    private JFXButton btnSendGlobally;

    @FXML
    JFXTextField tfUserText;

    @FXML
    private JFXListView<String> lvUserMessages;

    public ObservableList<String> userMessages = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: init currentUser to the users name
        currentUser = "none";

        //TODO: Get First User Messages from server
        lvUserMessages.setItems(userMessages);
        //Setting up users list
        lvUserMessages.setCellFactory(param -> new ListViewSetupHelper());
    }

    @FXML
    public void sendMessageToUser(ActionEvent event) {
        String text = tfUserText.getText();
        if (!text.isEmpty()){
            //TODO: Send message to the current user and update server
            addUserMessage(currentUser, text);
            tfUserText.clear();
        }
    }

    @FXML
    public void sendMessageGlobally(ActionEvent event) {
        String text = tfUserText.getText();
        if (!text.isEmpty()){
            addUserMessage(currentUser, text);
            tfUserText.clear();
        }
    }

    public void addUserMessage(String user, String message){
        String text = user + ":\n";
        text += message;
        userMessages.add(text);
    }
}
