package Models;

public class ServerInfo {

    private int id, port;
    boolean status;
    private String address, name;

    public ServerInfo(int id, int port, boolean status, String address, String name) {
        this.id = id;
        this.port = port;
        this.status = status;
        this.address = address;
        this.name = name;
    }

    public ServerInfo(int id, int port, String address, String name) {
        this.id = id;
        this.port = port;
        this.address = address;
        this.name = name;
        this.status = false;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public boolean isOnline() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "id=" + id +
                ", port=" + port +
                ", status=" + status +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
