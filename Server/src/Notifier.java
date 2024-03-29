import Core.Log;
import Core.Modules.ModuleInterface;
import Core.UserData;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 *
 *  Responsible for notifying all connected users about important changes
 *
 * @author FilipeA
 * @version 1.1
 */
public class Notifier implements Runnable, ModuleInterface.NotificationModule {

    private HashMap<Integer, UserData> users;
    public Notifier(HashMap<Integer, UserData> users) {
        this.users = users;
    }

    @Override
    public void run() {

        while (Main.STATUS){

        }
    }

    /**
     * User disconnected as such it needs to be removed from future connections
     * @param user to disconnect
     */
    public void disconnect(UserData user){
        synchronized (users) {
            try {
                users.remove(user.getID());
                user.closeConnectionSocket();
                user.closeNotificationSocket();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        try {
            this.updateUsers(((user.getUsername() == null ? "<GuestUser>" : user.getUsername()) + " disconnected"), user.getID());
        } catch (MegaPDRemoteException e) {
            e.printStackTrace();
        }
    }


    public void serverOff() {
        // NO NEED, new structure in place
        return;
    }

    @Override
    public void updateUsers(String s, int i) throws MegaPDRemoteException {
        synchronized (users) {
            users.forEach((id, user) -> {
                if (id == i) return; //equals a continue;
                try {
                    if (user.getNotificationOut() == null) {
                        Log.w("User " + (user.getUsername() == null ? user.getAddress() : user.getUsername()) + " is not listening to notifications");
                        return;
                    }
                    user.getNotificationOut().writeObject(NotificationRequest.updateUsers);
                    user.getNotificationOut().writeObject(s);
                } catch (IOException e) {
                    Log.w("Failed to transmit notification to user " + (user.getUsername() == null ? user.getAddress() : user.getUsername()));
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void updateFiles(String s, int i) {
        synchronized (users) {
            users.forEach((id, user) -> {
                if (id == i) return; //equals a continue;
                try {
                    user.getNotificationOut().writeObject(NotificationRequest.updateFiles);
                    user.getNotificationOut().writeObject(s);
                } catch (IOException e) {
                    Log.w("Failed to transmit notification to user " + (user.getUsername() == null ? user.getAddress() : user.getUsername()));
                }
            });
        }
    }

    @Override
    public void globalMessage(String s, int i) throws MegaPDRemoteException {
        synchronized (users) {
            users.forEach((id, user) -> {
                try {
                    if (user.getNotificationOut() == null) {
                        Log.w("User " + (user.getUsername() == null ? user.getAddress() : user.getUsername()) + " is not listening to notifications");
                        return;
                    }
                    //Log.i("Sent message <" + s + "> to user " +user.getUsername());
                    user.getNotificationOut().writeObject(NotificationRequest.globalMessage);
                    user.getNotificationOut().writeObject((users.get(i).getUsername() + ": " +s));
                } catch (IOException e) {
                    Log.w("Failed to transmit notification to user " + (user.getUsername() == null ? user.getAddress() : user.getUsername()));
                    e.printStackTrace();
                }
            });
        }
    }
}
