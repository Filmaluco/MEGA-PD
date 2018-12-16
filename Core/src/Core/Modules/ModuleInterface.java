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

        enum ConnectionRequest{connect, login, logout, getUsersOnline, getUser}

        public Socket connect(String ip, int serverPort) throws MegaPDRemoteException, IOException;
        public int login(String username, String password) throws MegaPDRemoteException;
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

        public String updateUsers() throws MegaPDRemoteException;
        public String updateFiles() throws MegaPDRemoteException;
    }


}
