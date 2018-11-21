package PD.Core;

import java.util.List;

public class Module {

    public interface FileTransfer {

        public List<MegaFile> getMyFiles();
        public void addFile(MegaFile file);
        public void removeFile(MegaFile file);
        //------------------------------------------------------------------------------------------------------------------
        public List<FileHistory> getFileHistory();
        //------------------------------------------------------------------------------------------------------------------
        public int requestFile(int fileID);
        public MegaFile getFileInfo(int fileID);
        public void startFileTransfer(int transferID);
        public void finishFileTransfer(int transferID);
    }

    public interface ConnectionModule {
        public boolean login(User user);
        public void logout(User user);
    }

    public interface ChatModule {

        public int[] getOnlineUsers();
        public int[] getRecentUsers();
        public User getUsernameFromID(int id);
        public List<Message> getMessages(int userID);
        public int[] getUnreadMessages(int userID);
        public boolean sendMessage(Message toSend);
        public boolean broadCastMessage(Message toSend);
    }

    public interface NotificationModule {
        public void newMessage();
        public void updateUsers();
        public void transferCompleted();
    }

}
