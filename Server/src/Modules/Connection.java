package Modules;

import Core.DBContextMegaPD;
import Core.Log;
import Core.Modules.MegaPDModule;
import Core.Modules.ModuleInterface;
import Core.UserData;
import Core.UserInfo;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Connection extends MegaPDModule implements ModuleInterface.ConnectionModule {

    DBContextMegaPD dbContext;
    UserData data;

    public Connection(UserData data) {
        super(data);
        this.data = data;
        dbContext = DBContextMegaPD.getDBContext();
    }


    @Override
    public int login() throws MegaPDRemoteException, IOException {
        int id;
        try {
            id = dbContext.registerGuestUser(data.getAddress(), data.getPort());
        }catch (Exception e){
            String errorMessage = "Failed registerGuest user ["+data.getAddress() +"]";
            e.printStackTrace();
            throw new MegaPDRemoteException(errorMessage);
        }
        data.setUsername("GuestUser"+id);
        data.setID(id);
        sendData(id);
        return id;
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
        sendData();
        return new Socket(this.remoteAdress, i);
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
    public void logout() throws MegaPDRemoteException, IOException {
        try {
            dbContext.disconnectUser(data.getID());
            sendData();
        } catch (IOException e) {
            //Can be ignored, since the connection is already lost at this point (depends on implementation on the other side)
        }
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

    public UserData getUserData() {
        return data;
    }
}
