package Core;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int ID;
    private String address;
    private int connectionPort,
                notificationPort,
                fileTransferPort,
                pingPort;

    public UserInfo(int id, String address, int connectionPort, int notificationPort, int fileTransferPort, int pingPort) {
        this.ID = id;
        this.address = address;
        this.connectionPort = connectionPort;
        this.notificationPort = notificationPort;
        this.fileTransferPort = fileTransferPort;
        this.pingPort = pingPort;
    }

    public int getID() {
        return ID;
    }

    public String getAddress() {
        return address;
    }

    public int getNotificationPort() {
        return notificationPort;
    }

    public int getFileTransferPort() {
        return fileTransferPort;
    }

    public int getPingPort() {
        return pingPort;
    }

    public int getConnectionPort() { return connectionPort; }
}
