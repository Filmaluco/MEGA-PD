import Core.DBContextMegaPD;
import Core.Log;
import Models.Server;
import Modules.Connection;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @version 0.1.2
 */
public class Main {

    public static void main(String[] args) throws IOException {

        //--------------------------------------------------------------------------------------------------------------
        // Variables
        //--------------------------------------------------------------------------------------------------------------
        //Server variables
        final double VERSION = 0.2;
        final String DESIGNATION = "SERVER";
        Server server;
        CommandInterpreter commandInterpreter;
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
            Log.s("\"Database Connection not created [" + e + "]");
            return;
        }
        Log.i("Database Connection created[Success]");
        //--------------------------------------------------------------------------------------------------------------
        // Starting Modules
        //--------------------------------------------------------------------------------------------------------------

        // Register the server  ----------------------------------------------------------------------------------------
        try {
            server = new Server(DESIGNATION+"["+VERSION+"]");
        } catch (Exception e) {
            Log.s("Couldn't register the server [" + e + "]");
            return;
        }
        Log.i("Server [Registered]");
        Log.i("Server["+server.getID()+"] info: \n Name: "+ DESIGNATION+VERSION +
                "\n Address: " + server.getIP() +
                "\n Port: " + server.getPort());

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
        Commands command;

        while (commandInterpreter.isAlive()){
            System.out.print(" >");
            command = commandInterpreter.read();
            switch (command){
                case EXIT:
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
            connectionModule.join(5000);
            connectionModule.stop();
        } catch (InterruptedException e) {
            Log.w(e.toString());
        }
        Log.i("ConnectionModule [Ended]");
        Log.i("Server [Ended]");
        try {
            DBContextMegaPD.getDBContext().disconnect();
        } catch (SQLException e) {
            Log.s("Couldn't properly disconnect the Server please contact the DB administrator");
        }
        Log.i("Database [Disconnected]");
    }
}
