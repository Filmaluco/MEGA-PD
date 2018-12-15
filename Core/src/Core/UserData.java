package MegaPD.Core;

import MegaPD.Core.Modules.EntityData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserData extends EntityData {
    private String  username;
    private Socket  notificationSocket;
    public ObjectInputStream notificationInput;
    public ObjectOutputStream notificationOuput;

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

    public void setSocket(Socket s) throws IOException {
        super.setSocket(s, false);
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


    public boolean isAuthenticaded(){
        return this.getID() != -1;
    }

}