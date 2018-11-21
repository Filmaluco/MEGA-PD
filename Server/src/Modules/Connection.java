package Modules;

import Core.Log;
import Models.Server;
import Models.User;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Connection Module, this class is responsible for accepting request's from the clients
 *
 * @author FilipeA
 * @version 0.2.0
 */
public class Connection implements Runnable{
    //Variables
    //----------------------------------------------------------------------------------------------
    //Private Variables
        //Control variables
    private boolean status = true;
        //Server control variables
    private Server server;
        //User Control variables
    private List<User> users;
        //TCP socket variables
    private final int SERVER_TIMEOUT = 5000;
    private ServerSocket serverSocket;

    //Public Variables


    //----------------------------------------------------------------------------------------------
    //      CONSTRUCTOR'S
    //----------------------------------------------------------------------------------------------

    /**
     * Base Constructor
     */
    public Connection(Server server){
      this.server = server;
      serverSocket = server.getServerSocket();
      users = server.getUsers();
    }

    //----------------------------------------------------------------------------------------------
    //      GETTERS
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //      SETTERS
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //      Runnable
    //----------------------------------------------------------------------------------------------

    @Override
    public void run() {
        try {
            serverSocket.setSoTimeout(SERVER_TIMEOUT);

            while (status){

               Socket userConnectionSocket;
                try {
                    userConnectionSocket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    continue;
                } catch (IOException e){
                Log.w("[Connection] an attempted connection failed \n " + e);
                continue;
                }




            }
        } catch (SocketException e) {
            Log.i("ConnectionModule [Failed] \n couldn't set serverSocket timeout");
        }
    }

    //----------------------------------------------------------------------------------------------
    //      Methods
    //----------------------------------------------------------------------------------------------

    // Public Methods ------------------------------------------------------------------------------

    /**
     * Stops connection module from accepting more request's
     */
    public void close() {
        status = false;
    }

    // Private Methods -----------------------------------------------------------------------------

}
