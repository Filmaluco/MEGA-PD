package Core;

import Helpers.ServerManager;
import Models.ServerData;
import Modules.*;
import PD.Core.Log;
import PD.Core.User;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class Context {

    //Variables
    //----------------------------------------------------------------------------------------------
    //Private Variables
    private ServerData server;
    private User user;
    private Chat chatContext;
    private FileTransfer fileTransferContext;
        //TCP connections
        ServerSocket notificationServerSocket, chatServerSocket, fileManagerServerSocket;
        //UDP connection
        DatagramSocket pingSocket;

    //----------------------------------------------------------------------------------------------
    //      CONSTRUCTOR
    //----------------------------------------------------------------------------------------------

    public Context() {
        try {
            notificationServerSocket = new ServerSocket(0);
            chatServerSocket = new ServerSocket(0);
            fileManagerServerSocket = new ServerSocket(0);
            pingSocket = new DatagramSocket(0);
        } catch (IOException e) {
            Log.exit("[Sockets] failed to create sockets error: " + e);
        }
        server = ServerManager.loadServer();
    }

    //----------------------------------------------------------------------------------------------
    //      GETTERS
    //----------------------------------------------------------------------------------------------

    public User getUser() {
        return user;
    }

    public Chat getChatContext() {
        return chatContext;
    }

    public FileTransfer getFileTransferContext() {
        return fileTransferContext;
    }

    public ServerData getServer() {
        return server;
    }

    public ServerSocket getNotificationServerSocket() {
        return notificationServerSocket;
    }

    public ServerSocket getChatServerSocket() {
        return chatServerSocket;
    }

    public ServerSocket getFileManagerServerSocket() {
        return fileManagerServerSocket;
    }

    public DatagramSocket getPingSocket() {
        return pingSocket;
    }

    //----------------------------------------------------------------------------------------------
    //      SETTERS
    //----------------------------------------------------------------------------------------------

    public void setUser(User user) {
        this.user = user;
    }

    public void setChatContext(Chat chatContext) {
        this.chatContext = chatContext;
    }

    public void setFileTransferContext(FileTransfer fileTransferContext) {
        this.fileTransferContext = fileTransferContext;
    }
}
