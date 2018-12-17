package Modules;

import Core.Log;
import Core.Modules.EntityData;
import Core.Modules.MegaPDModule;
import Core.Modules.ModuleInterface;
import Core.UserInfo;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Connection extends MegaPDModule implements ModuleInterface.ConnectionModule {


    public Connection(EntityData data) {
        super(data);
    }

    @Override
    public Socket login(int i) throws MegaPDRemoteException, IOException {
        return null;
    }

    @Override
    public Socket login(String s, String s1, int notificationPort) throws MegaPDRemoteException {
        return null;
    }

    @Override
    public void logout() throws MegaPDRemoteException {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return;
    }

    @Override
    public Map<Integer, String> getUsersOnline() throws MegaPDRemoteException {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserInfo getUser(int i) throws MegaPDRemoteException {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return null;
    }
}
