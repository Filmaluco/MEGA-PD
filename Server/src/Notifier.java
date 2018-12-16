import Core.Modules.ModuleInterface;
import Core.UserData;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

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
            this.updateUsers();
        } catch (MegaPDRemoteException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public String updateUsers() throws MegaPDRemoteException {
        // TODO: 12/16/2018 implement
        return null;
    }

    @Override
    public String updateFiles() throws MegaPDRemoteException {
        // TODO: 12/16/2018 implement
        return null;
    }

    public void serverOff() {
        // TODO: 12/16/2018 implement
        return;
    }
}
