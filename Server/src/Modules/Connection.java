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
    public int login() throws MegaPDRemoteException, IOException {
        sendData();
        return 0;
    }

    @Override
    public int login(String s, String s1) throws MegaPDRemoteException, IOException {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Socket registerNotificationPort(int i) throws MegaPDRemoteException, IOException {
        return new Socket(this.remoteAdress.substring(1), i);
    }

    @Override
    public void registerFileTransferPort(int i) throws MegaPDRemoteException, IOException {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
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
