package Modules;

import Core.Context;
import Core.Modules.ModuleInterface.NotificationModule;
import MegaPD.Core.Exeptions.MegaPDRemoteException;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for managing/storing all the client notifications
 *
 * @author rmcsilva
 * @version 1.0
 */

public class NotificationManager extends Thread implements NotificationModule {
    private JFXButton btnNotifications;
    private List<String> notifications;
    private List<String> messages;

    /**
     * Setups the log for the client notifications
     * @param btnNotifications button that is responsible for showing the notifications and its status
     */
    public NotificationManager(JFXButton btnNotifications) {
        this.btnNotifications = btnNotifications;
        notifications = new ArrayList<>();
        messages = new ArrayList<>();
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

    public List<String> getMessages() {
        return messages;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void updateUsers(String s, int i) throws MegaPDRemoteException, IOException {
        this.addNotification(s);
        try{ Context.getUsersList().clear(); } catch (Exception ignored){}
        Context.getConnection().getUsersOnline().forEach((id, username)-> Context.getUsersList().add(username));
    }

    @Override
    public void updateFiles(String s, int i) throws MegaPDRemoteException, IOException {
        this.addNotification(s);
    }

    @Override
    public void globalMessage(String s, int i) throws MegaPDRemoteException, IOException {
        messages.add(s);
    }

    @Override
    public void run() {
        System.out.println("[Notification] started");
        while(true){
            try {
                Enum request = (Enum) Context.getUser().getNotificationIn().readObject();
                String message = (String) Context.getUser().getNotificationIn().readObject();
                if(request instanceof NotificationRequest){
                    NotificationRequest notificationRequest = (NotificationRequest) request;
                    switch (notificationRequest){
                        case updateUsers:
                            this.updateUsers(message, 0);
                            break;
                        case updateFiles:
                            this.updateFiles(message, 0);
                            break;
                    }
                }

            } catch (IOException e){
                Toolkit.getDefaultToolkit().beep();

                JFrame jf=new JFrame();
                jf.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(jf, "Conexao com o Servidor Perdida", "Connection Lost",JOptionPane.ERROR_MESSAGE);
                Platform.exit(); //somehow its not properly closing the FX
                System.exit(-2);
            }catch( ClassNotFoundException | MegaPDRemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
