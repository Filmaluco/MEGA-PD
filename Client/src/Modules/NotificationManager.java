package Modules;

import com.jfoenix.controls.JFXButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for managing/storing all the client notifications
 *
 * @author rmcsilva
 * @version 1.0
 */

public class NotificationManager {
    private JFXButton btnNotifications;
    private List<String> notifications;

    /**
     * Setups the log for the client notifications
     * @param btnNotifications button that is responsible for showing the notifications and its status
     */
    public NotificationManager(JFXButton btnNotifications) {
        this.btnNotifications = btnNotifications;
        notifications = new ArrayList<>();
    }

    /**
     * Gets all of the client notifications
     * @return List of notifications
     */
    public List<String> getNotifications() { return notifications; }

    /**
     * Checks if the client has notifications
     * @return True if the client has notifications
     */
    public boolean hasNotifications(){ return !notifications.isEmpty(); }

    /**
     * Adds notifications to the log
     * @param notification the notification that is going to be added to the log
     */
    public void addNotification(String notification){
        if (!hasNotifications()){
            btnNotifications.getStyleClass().removeAll("jfx-button");
            btnNotifications.getStyleClass().add("notificationOn");
        }

        notifications.add(notification);
    }

    /**
     * Clears the client notifications
     */
    public void clearNotifications(){
        notifications.clear();
        btnNotifications.getStyleClass().removeAll("notificationOn");
        btnNotifications.getStyleClass().add("jfx-button");
    }
}
