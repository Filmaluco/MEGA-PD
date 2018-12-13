package Modules;

import com.jfoenix.controls.JFXButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationManager {
    private JFXButton btnNotifications;
    private List<String> notifications;

    public NotificationManager(JFXButton btnNotifications) {
        this.btnNotifications = btnNotifications;
        notifications = new ArrayList<>();
    }

    public List<String> getNotifications() { return notifications; }

    public boolean hasNotifications(){ return !notifications.isEmpty(); }

    public void addNotification(String notification){
        if (!hasNotifications()){
            btnNotifications.getStyleClass().removeAll("jfx-button");
            btnNotifications.getStyleClass().add("notificationOn");
        }

        notifications.add(notification);
    }

    public void clearNotifications(){
        notifications.clear();
        btnNotifications.getStyleClass().removeAll("notificationOn");
        btnNotifications.getStyleClass().add("jfx-button");
    }
}
