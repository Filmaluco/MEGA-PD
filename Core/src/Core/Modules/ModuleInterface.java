package Core.Modules;

import Core.MegaPDFile;
import Core.MegaPDHistory;
import Core.UserInfo;
import MegaPD.Core.Exeptions.MegaPDRemoteException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ModuleInterface {

    public interface ConnectionModule {

        enum ConnectionRequest{guestLogin, login, logout, getUsersOnline, getUser}

        public Socket login(int notificationPort) throws MegaPDRemoteException, IOException;
        public Socket login(String username, String password, int notificationPort) throws MegaPDRemoteException, IOException;
        public void logout() throws MegaPDRemoteException;
        public Map<Integer, String> getUsersOnline() throws MegaPDRemoteException;
        public UserInfo getUser(int userId)throws MegaPDRemoteException;
    }

    public interface FileManagerModule{
        enum FileManagerRequest {updateFiles, addFile, remove, updateFile, getUserFiles, requestFile, completeFileTransfer, getFileHistory}

        public void updateFiles(List<MegaPDFile> fileList) throws MegaPDRemoteException;
        public void addFile(MegaPDFile file) throws MegaPDRemoteException;
        public void remove(MegaPDFile file) throws MegaPDRemoteException;
        public void updateFile(String fileName, long fileSize) throws MegaPDRemoteException;
        public List<MegaPDFile> getUserFiles(int userId) throws MegaPDRemoteException;
        public int requestFile(String fileName, int userId) throws MegaPDRemoteException;
        public void completeFileTransfer(int requestID) throws MegaPDRemoteException;
        public List<MegaPDHistory> getFileHistory();
    }

    public interface NotificationModule{
        enum NotificationRequest {updateUsers, updateFiles}

        public void updateUsers(String notificationMessage) throws MegaPDRemoteException;
        public void updateFiles(String notificationMessage) throws MegaPDRemoteException;
    }


}
