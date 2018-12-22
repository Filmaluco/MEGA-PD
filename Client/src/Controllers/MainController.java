package Controllers;

import Modules.NotificationManager;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    JFXButton btnNotifications;

    private NotificationManager notificationManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notificationManager = new NotificationManager(btnNotifications);
        Thread t = new Thread(notificationManager);
        t.setDaemon(true);
        t.start();
    }

    public void showNotifications(ActionEvent eventAction) {
        JFXAlert alert = new JFXAlert((Stage) btnNotifications.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(true);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.getStylesheets().add(getClass().getResource("/Views/Layouts/main.css").toExternalForm());
        layout.setHeading(new Label("Notifications:"));

        if (notificationManager.hasNotifications()){
            JFXListView<String> notificationList = new JFXListView<>();
            notificationList.setMinHeight(300);
            notificationList.setMinWidth(600);
            for (String notification : notificationManager.getNotifications()) {
                notificationList.getItems().add(notification);
            }
            layout.setBody(notificationList);
            notificationManager.clearNotifications();
        }else{
            layout.setBody(new Label("No new notifications!"));
            //This is for test only
//            for (int i = 0; i < 100; i++) {
//                notificationManager.addNotification("Teste " + i);
//            }
        }

        JFXButton closeButton = new JFXButton("Confirm");
        closeButton.getStyleClass().add("dialog-accept");
        closeButton.setOnAction(event -> alert.hideWithAnimation());
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
    }
}
