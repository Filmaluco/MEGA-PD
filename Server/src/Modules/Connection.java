package Modules;

import Core.DBContextMegaPD;
import Core.Log;
import Core.Modules.MegaPDModule;
import Core.Modules.ModuleInterface;
import Core.UserData;
import Core.UserInfo;
import Helpers.PasswordHasher;
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
            id = dbContext.loginGuestUser(data.getAddress(), data.getPort());
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
    public int login(String s, String s1) throws MegaPDRemoteException {
        int userID = 0;
        String username;

        try {
            userID = dbContext.loginUser(s, PasswordHasher.generateSecurePassword(s1));
            dbContext.updateUserAddress(userID, data.getAddress());
            username = dbContext.getUser(userID).getUsername();
        } catch (Exception e) {
            this.newException(e.getMessage());
            try { sendData(); } catch (IOException e1) { e1.printStackTrace(); }
            e.printStackTrace();
            throw new MegaPDRemoteException(e.getMessage());
        }

        data.setID(userID);
        data.setUsername(username);

        //send correct data --------------------------------------------------------------------------------------------
        try {
            sendData(userID);
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            e.printStackTrace();
            throw new MegaPDRemoteException(e.getMessage());
        }
        return userID;
    }


    @Override
    public Socket registerNotificationPort(int i) throws IOException {
        dbContext.updateUserNotificationPort(data.getID(), i);
        sendData();
        return new Socket(this.remoteAdress, i);
    }

    @Override
    public void registerFileTransferPort(int i) throws IOException {
        dbContext.updateUserFileTransferPort(data.getID(), i);
        sendData();
    }

    @Override
    public void logout() throws MegaPDRemoteException {
        try {
            dbContext.logoutUser(data.getID());
            sendData();
            //All other relevant data is done on the notifier class
        } catch (IOException e) {
            //Can be ignored, since the connection is already lost at this point (depends on implementation on the other side)
        }
    }

    @Override
    public Map<Integer, String> getUsersOnline() throws MegaPDRemoteException {
        Map<Integer, String> usersOnline = dbContext.getServerAuthUsers();

        //Remove myself
        usersOnline.remove(this.data.getID());


        try {
            sendData(usersOnline);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usersOnline;
    }

    @Override
    public UserInfo getUser(int i) throws IOException {
        UserInfo userInfo = null;
        try {
            userInfo = dbContext.getUser(i);
        } catch (Exception e) {
            this.newException("Failed to retrieve user info: " + e.getClass().getName());
            e.printStackTrace();
        }

        sendData(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo getUser(String s) throws MegaPDRemoteException, IOException {
        UserInfo userInfo = null;
        try {
            userInfo = dbContext.getUser(s);
        } catch (Exception e) {
            this.newException("Failed to retrieve user info: " + e.getClass().getName());
            e.printStackTrace();
        }

        sendData(userInfo);
        return userInfo;
    }

    public UserData getUserData() {
        return data;
    }

}
