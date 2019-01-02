package Controllers;

import Core.Context;
import Helpers.ListViewSetupHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageController implements Initializable {

    private String selectedUser = "none";

    private String currentUser;

    @FXML
    private JFXButton btnSendGlobally;

    @FXML
    JFXTextField tfUserText;

    private ObservableList<String> userMessages;

    @FXML
    private JFXListView<String> lvUserMessages;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: init currentUser to the users name
        currentUser = "none";

        //TODO: Get First User Messages from server
        userMessages =  FXCollections.observableArrayList();
        Context.setMessages(userMessages);

        userMessages.add("Started Global Chat __________________");
        lvUserMessages.setItems(userMessages);
        //Setting up users list
        lvUserMessages.setCellFactory(param -> new ListViewSetupHelper());
    }


    @FXML
    public void sendMessageGlobally(ActionEvent event) {
        String text = tfUserText.getText();
        if (!text.isEmpty()){
            try {
                Context.getConnection().sendMessage(text);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            tfUserText.clear();
        }
    }

    @FXML
    public void sendMessageToUser(ActionEvent event) {
        String text = tfUserText.getText();
        if (!text.isEmpty()){
            //TODO: Send message to the current user and update server
            tfUserText.clear();
        }
    }


}
