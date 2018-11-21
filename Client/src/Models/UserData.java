package Models;

import java.net.ServerSocket;

public class UserData {
    String directory;
    int pingPort;
    ServerSocket notificationServerSocket;
    ServerSocket chatServerSocket;
    ServerSocket fileManagerServerSocket;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getPingPort() {
        return pingPort;
    }

    public void setPingPort(int pingPort) {
        this.pingPort = pingPort;
    }

    public ServerSocket getNotificationSocket() {
        return notificationServerSocket;
    }

    public void setNotificationSocket(ServerSocket notificationServerSocket) {
        this.notificationServerSocket = notificationServerSocket;
    }

    public ServerSocket getChatSocket() {
        return chatServerSocket;
    }

    public void setChatSocket(ServerSocket chatServerSocket) {
        this.chatServerSocket = chatServerSocket;
    }

    public ServerSocket getFileManagerSocket() {
        return fileManagerServerSocket;
    }

    public void setFileManagerSocket(ServerSocket fileManagerServerSocket) {
        this.fileManagerServerSocket = fileManagerServerSocket;
    }
}
