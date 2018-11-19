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
public class Connection implements Runnable {
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
        while (status){



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
