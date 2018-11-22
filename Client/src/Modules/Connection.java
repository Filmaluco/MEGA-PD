package Modules;

import Models.ServerData;
import PD.Core.Log;
import PD.Core.User;
import PD.Core.Module;

import java.io.*;
import java.net.Socket;


public class Connection implements Module.ConnectionModule {

    private ServerData server;


    public Connection(ServerData server){
        this.server = server;

    }


    //----------------------------------------------------------------------------------------------
    //      ConnectionModule (TCP Connection)
    //----------------------------------------------------------------------------------------------

    @Override
    public boolean login(User user){
        Socket connSocket;
        ObjectInputStream in;
        ObjectOutputStream out;
        try {
             connSocket = new Socket(server.getIP(), server.getPort());
             InputStream inn = connSocket.getInputStream();
             OutputStream oout = connSocket.getOutputStream();

             out = new ObjectOutputStream(oout);
             in = new ObjectInputStream(inn);

            out.writeObject(user);
            out.flush();

            connSocket.close();

        } catch (IOException e) {
            Log.w("[Connection] Problem establishing connection\"");
        }
        return false;
    }

    @Override
    public void logout(User user) {

    }


}
