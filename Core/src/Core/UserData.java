package Core;

import Core.Modules.EntityData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UserData extends EntityData {
    private String  username;
    private boolean isAuthenticated;
    private Socket  notificationSocket;
    private ObjectInputStream notificationInput;
    private ObjectOutputStream notificationOuput;
    private ServerSocket fileTransferServerSocket;

    public UserData(Boolean bool){
        super(bool);
    }

    public UserData(String username) {
        super(-1);
        this.username = username;
    }

    public UserData(){
        super(-1);
    }

    public int getID() {
        if(id.compareTo(UNDEFINED) == 0){
            return  -1;
        }
        try{
            return Integer.valueOf(id);
        }catch (Exception e){
            return -1;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setID(int id) {
        this.id = Integer.toString(id);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSocket(Socket s) throws IOException {
        super.setConnectionSocket(s, false);
    }

    public void setNotificationSocket(Socket s, boolean receiver) throws IOException {
        this.notificationSocket = s;
        if(receiver){
            notificationInput = new ObjectInputStream(s.getInputStream());
            notificationOuput = new ObjectOutputStream(s.getOutputStream());
        }else{
            notificationOuput = new ObjectOutputStream(s.getOutputStream());
            notificationInput = new ObjectInputStream(s.getInputStream());
        }

    }

    public void setFileTransferServerSocket(ServerSocket ss){
        this.fileTransferServerSocket = ss;
    }

    public ServerSocket getFileTransferServerSocket() {
        return fileTransferServerSocket;
    }

    public ObjectInputStream getNotificationIn() {
        return notificationInput;
    }

    public ObjectOutputStream getNotificationOut() {
        return notificationOuput;
    }


    public void authenticate(){this.isAuthenticated = true;};
    public boolean isAuthenticaded(){
        return isAuthenticated;
    }

    public void closeNotificationSocket() throws IOException {notificationSocket.close();}

}
