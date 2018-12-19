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

        enum ConnectionRequest{guestLogin, AuthLogin, registerNotification, registerFileTransfer, logout, getUsersOnline, getUser}

        int login() throws MegaPDRemoteException, IOException;
        int login(String username, String password) throws MegaPDRemoteException, IOException;
        Socket registerNotificationPort(int notificationPort) throws MegaPDRemoteException, IOException;
        void registerFileTransferPort(int fileTransferPort) throws MegaPDRemoteException, IOException;
        void logout() throws MegaPDRemoteException, IOException;
        Map<Integer, String> getUsersOnline() throws MegaPDRemoteException, IOException;
        UserInfo getUser(int userId)throws MegaPDRemoteException, IOException;
    }

    public interface FileManagerModule{
        enum FileManagerRequest {updateFiles, addFile, remove, updateFile, getUserFiles, requestFile, completeFileTransfer, getFileHistory}

        void updateFiles(List<MegaPDFile> fileList) throws MegaPDRemoteException, IOException;
        void addFile(MegaPDFile file) throws MegaPDRemoteException, IOException;
        void remove(MegaPDFile file) throws MegaPDRemoteException, IOException;
        void updateFile(String fileName, long fileSize) throws MegaPDRemoteException, IOException;
        List<MegaPDFile> getUserFiles(int userId) throws MegaPDRemoteException, IOException;
        int requestFile(String fileName, int userId) throws MegaPDRemoteException, IOException;
        void completeFileTransfer(int requestID) throws MegaPDRemoteException, IOException;
        List<MegaPDHistory> getFileHistory() throws MegaPDRemoteException, IOException;
    }

    public interface NotificationModule{
        enum NotificationRequest {updateUsers, updateFiles}

        void updateUsers(String notificationMessage, int ignoreUser) throws MegaPDRemoteException, IOException;
        void updateFiles(String notificationMessage, int ignoreUser) throws MegaPDRemoteException, IOException;

    }


}
