import Core.DBContextMegaPD;
import Helpers.CommandInterpreter;
import Models.Server;
import Modules.Connection;
import PD.Core.Log;
import java.io.IOException;

import java.sql.SQLException;

/**
 * @version 0.1.4
 */
public class Main {

    public static void main(String[] args){

        //--------------------------------------------------------------------------------------------------------------
        // Variables
        //--------------------------------------------------------------------------------------------------------------
        //Server variables
        final double VERSION = 0.2;
        final String DESIGNATION = "SERVER";
        Server server;
        CommandInterpreter commandInterpreter;
        final int SERVER_TIMEOUT = 9000;
        //--------------------------------------------------------------------------------------------------------------
        //Connection Module
        Thread connectionModule = null;
        Connection connectionData;


        //--------------------------------------------------------------------------------------------------------------
        // Starting DB and LOG
        //--------------------------------------------------------------------------------------------------------------
        Log.initLog();
        // Starting DB context -----------------------------------------------------------------------------------------
        try {
            new DBContextMegaPD().setHost(args[0])
                    .setDB(args[1])
                    .setAuth(args[2], args[3])
                    .connect();
        }catch (Exception e){
            Log.exit("\"Database Connection not created [" + e + "]");
            return;
        }
        Log.i("Database [Connected]");
        //--------------------------------------------------------------------------------------------------------------
        // Starting Modules
        //--------------------------------------------------------------------------------------------------------------

        // Register the server  ----------------------------------------------------------------------------------------
        try {
            server = new Server(DESIGNATION+"["+VERSION+"]");
        } catch (Exception e) {
            Log.exit("Couldn't register the server [" + e + "]");
            return;
        }
        Log.i("Server [Registered]");
        Log.i("\nServer["+server.getID()+"] info: \nName: "+ DESIGNATION+VERSION +
                "\nAddress: " + server.getIP() +
                "\nPort: " + server.getPort() + "\n");

        Log.i("Server [Started]");

        // Starting ConnectionModule -----------------------------------------------------------------------------------
        connectionData = new Connection(server);
        connectionModule = new Thread(connectionData);
        connectionModule.start();
        Log.i("ConnectionModule [Started]");


        //--------------------------------------------------------------------------------------------------------------
        // Main Loop
        //--------------------------------------------------------------------------------------------------------------
        commandInterpreter = new CommandInterpreter();
        CommandInterpreter.Commands command;

        while (commandInterpreter.isAlive()){
            System.out.print(" >");
            command = commandInterpreter.read();
            switch (command){
                case SHUTDOWN:
                    Log.i("Shutdown Request from command line");
                    System.out.println("... shutting down");
                    continue;
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        // Closing Modules
        //--------------------------------------------------------------------------------------------------------------
        try {
            connectionData.close();
            connectionModule.join(SERVER_TIMEOUT);
        } catch (InterruptedException e) {
            Log.w(e.toString());
        }
        Log.i("ConnectionModule [Ended]");
        DBContextMegaPD.getDBContext().disconnect();
        Log.i("Database [Disconnected]");
        Log.i("Server [Ended]");
    }
}
