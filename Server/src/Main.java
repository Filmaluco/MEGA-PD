import Core.DBContextMegaPD;
import Core.Log;
import Core.UserData;
import Helpers.CommandInterpreter;
import Models.Server;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @version 1.0.0
 */
public class Main {

    public static void main(String[] args) throws IOException {

        //--------------------------------------------------------------------------------------------------------------
        // Variables
        //--------------------------------------------------------------------------------------------------------------
        final double VERSION = 0.2;
        final String DESIGNATION = "SERVER";
        Server server;
        CommandInterpreter commandInterpreter;
        final int SERVER_TIMEOUT = 9000;

        List<UserData> users = new ArrayList<>();

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
            server = new Server(DESIGNATION+"["+VERSION+"]", 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.exit("Couldn't register the server [" + e + "]");
            return;
        }
        Log.i("Server [Registered]");
        Log.i("\nServer["+server.getID()+"] info: \nName: "+ DESIGNATION+VERSION +
                "\nAddress: " + server.getIP() +
                "\nPort: " + server.getPort() + "\n");

        Log.i("Server [Started]");

        // Starting User Connections -----------------------------------------------------------------------------------


        // Starting User Notifications ---------------------------------------------------------------------------------


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
        // Closing User Connections ------------------------------------------------------------------------------------


        // Closing User Notifications ----------------------------------------------------------------------------------a
        try {
            DBContextMegaPD.getDBContext().disconnect();
        } catch (SQLException e) {
            Log.exit("Database [Couldn't properly disconnect the Server please contact the DB administrator]");
            //e.printStackTrace();
        }
        Log.i("Database [Disconnected]");
        Log.i("Server [Ended]");
    }
}
