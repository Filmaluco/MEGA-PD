import Core.DBContextMegaPD;
import Core.Log;
import Core.UserData;
import Helpers.CommandInterpreter;
import Helpers.Constants;
import Models.Server;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * @version 2.0.0
 */
public class Main {

    public static boolean STATUS;

    public static void main(String[] args) {

        //--------------------------------------------------------------------------------------------------------------
        // Variables
        //--------------------------------------------------------------------------------------------------------------
        final double VERSION = 0.2;
        final String DESIGNATION = "SERVER";
        Server server;
        CommandInterpreter commandInterpreter;

        HashMap<Integer, UserData> users = new HashMap<>();

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
            server = new Server(DESIGNATION+"["+VERSION+"]", 0, users);
        } catch (Exception e) {
            e.printStackTrace();
            Log.exit("Couldn't register the server [" + e + "]");
            return;
        }
        Log.i("Server [Registered]");
        Log.i("\nServer["+server.getID()+"] info: \nName: "+ DESIGNATION+VERSION +
                 "\nAddress: " + server.getIP() +
                 "\nPort: " + server.getPort() + "\n");

        STATUS = true;
        Log.i("Server [Started]");


        // Starting User Notifications ---------------------------------------------------------------------------------
        Notifier notifier = new Notifier(users);
        Thread notificationNotifier = new Thread(notifier);
        notificationNotifier.start();

        // Starting User Connections -----------------------------------------------------------------------------------
        Thread connectionListener = new Thread(new ConnectionListenerThread(server, users, notifier));
        connectionListener.start();

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
                    STATUS = false;
                break;
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        // Closing Modules
        //--------------------------------------------------------------------------------------------------------------
        // Closing User Connections ------------------------------------------------------------------------------------
        try {
            connectionListener.join(Constants.SERVER_TIMEOUT);
        } catch (InterruptedException e) {
            Log.exit("ConnectionListener [InterruptedException]");
            //e.printStackTrace();
        }

        // Closing User Notifications ----------------------------------------------------------------------------------a
        try {
            notificationNotifier.join(Constants.SERVER_TIMEOUT);
        } catch (InterruptedException e) {
            Log.exit("NotificationNotifier [InterruptedException]");
            //e.printStackTrace();
        }


        try {
            DBContextMegaPD.getDBContext().disconnect();
        } catch (SQLException e) {
            Log.exit("Database [Couldn't properly disconnect the Server please contact the DB administrator]");
            //e.printStackTrace();
        }
        Log.i("Database [Disconnected]");
        Log.i("Server [Ended]");

        //--------------------------------------------------------------------------------------------------------------
        if(connectionListener.isAlive()) connectionListener.stop();
        if(notificationNotifier.isAlive()) notificationNotifier.stop();
    }
}
