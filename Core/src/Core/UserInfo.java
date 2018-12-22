package Core;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int ID;
    private String address, username, name;
    private int connectionPort,
                notificationPort,
                fileTransferPort,
                pingPort;

    public UserInfo(int id, String name, String username, String address, int connectionPort, int notificationPort, int fileTransferPort, int pingPort) {
        this.ID = id;
        this.name = name;
        this.username = username;
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

    public String getName() { return name; }

    public String getUsername() { return username; }

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

    @Override
    public String toString() {
        return "UserInfo{" +
                "ID=" + ID +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", connectionPort=" + connectionPort +
                ", notificationPort=" + notificationPort +
                ", fileTransferPort=" + fileTransferPort +
                ", pingPort=" + pingPort +
                '}';
    }
}
