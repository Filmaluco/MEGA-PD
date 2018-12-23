package Modules.RemoteModules;

import Core.Modules.EntityData;
import Core.Modules.MegaPDRemoteModule;
import Core.Modules.ModuleInterface;
import Core.UserInfo;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

    public class Connection extends MegaPDRemoteModule implements ModuleInterface.ConnectionModule {


        public Connection(EntityData remoteData) {
            super(remoteData);
        }

        @Override
        public int login() throws MegaPDRemoteException, IOException {
            return (Integer) this.remoteMethod(ConnectionRequest.guestLogin);
        }

        @Override
        public int login(String s, String s1) throws MegaPDRemoteException, IOException {
            return (Integer) this.remoteMethod(ConnectionRequest.AuthLogin, s, s1);
        }

        @Override
        public Socket registerNotificationPort(int i) throws MegaPDRemoteException, IOException {
            ServerSocket serverS = new ServerSocket(i);
            this.remoteMethod(ConnectionRequest.registerNotification, serverS.getLocalPort());
            return serverS.accept();
        }

        @Override
        public void registerFileTransferPort(int i) throws MegaPDRemoteException, IOException {
            this.remoteMethod(ConnectionRequest.registerFileTransfer, i);
        }

        @Override
        public void logout() throws MegaPDRemoteException, IOException {
            this.remoteMethod(ConnectionRequest.logout);
        }

        @Override
        public Map<Integer, String> getUsersOnline() throws MegaPDRemoteException, IOException {
            return (Map<Integer, String>) this.remoteMethod(ConnectionRequest.getUsersOnline);
        }

        @Override
        public UserInfo getUser(int i) throws MegaPDRemoteException, IOException {
            return (UserInfo) this.remoteMethod(ConnectionRequest.getUserByID, i);
        }

        @Override
        public UserInfo getUser(String s) throws MegaPDRemoteException, IOException {
            return (UserInfo) this.remoteMethod(ConnectionRequest.getUserByUsername, s);
        }
    }
