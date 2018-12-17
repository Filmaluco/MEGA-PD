import Core.Log;
import Core.Modules.ModuleInterface;
import Core.UserData;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.util.List;

/**
 *
 *  Responsible for notifying all connected users about important changes
 *
 * @author FilipeA
 * @version 1.1
 */
public class Notifier implements Runnable, ModuleInterface.NotificationModule {

    private List<UserData> users;
    public Notifier(List<UserData> users) {
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
        //TODO: sync
        users.remove(user);
        try {
            this.updateUsers((user.getUsername() == null ? "<GuestUser>" : user.getUsername()) +" disconnected");
        } catch (MegaPDRemoteException e) {
            //e.printStackTrace();
        }
    }


    public void serverOff() {
        // TODO: 12/16/2018 implement
        return;
    }

    @Override
    public void updateUsers(String s) throws MegaPDRemoteException {
        for (UserData user: users) {
            try {
                user.notificationOuput.writeObject(NotificationRequest.updateUsers);
                user.notificationOuput.writeObject(s);
            } catch (IOException e) {
                Log.w("Failed to transmit notification to user " + (user.getUsername() == null ? user.getAddress() : user.getUsername()));
            }
        }
    }

    @Override
    public void updateFiles(String s) throws MegaPDRemoteException {
        for (UserData user: users) {
            try {
                user.notificationOuput.writeObject(NotificationRequest.updateFiles);
                user.notificationOuput.writeObject(s);
            } catch (IOException e) {
                Log.w("Failed to transmit notification to user " + (user.getUsername() == null ? user.getAddress() : user.getUsername()));
            }
        }
    }
}
