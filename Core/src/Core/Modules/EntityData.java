package Core.Modules;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class EntityData {

    public String UNDEFINED = "undefined";

    protected String id;
    protected int port;
    protected String address;
    private Socket socket;

    protected ObjectInputStream in;
    protected ObjectOutputStream out;

    public EntityData(String id) {
        this.address = getIP();
        this.port = 0;
        this.id = id;
    }

    public EntityData(Boolean bool){
        this.port = 0;
        this.id = "-1";
        if(bool){
            this.address = getIP();
        }
    }

    public EntityData(int id){
        this(Integer.valueOf(id).toString());
    }

    public EntityData() {
        this("undefined");
    }


    public String getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public Socket getConnectionSocket() {
        return socket;
    }

    public ObjectInputStream getConnectionIn() {
        return in;
    }

    public ObjectOutputStream getConnectionOut() {
        return out;
    }

    protected void setConnectionSocket(Socket socket, boolean receiver) throws IOException {
        this.socket = socket;
        if(receiver){
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        }else{
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        }
    }

    public void closeConnectionSocket() throws IOException {
        if(socket != null){
            in.close();
            out.close();
            socket.close();
        }
    }

    private String getIP(){
        //Find machine real IP address
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ip.getHostAddress().toString();
    }
}
