import Core.DBContextMegaPD;
import Core.Log;
import Core.UserData;
import Helpers.CommandInterpreter;
import Helpers.Constants;
import Helpers.PasswordHasher;
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
                    notifier.serverOff();
                    STATUS = false;
                break;
                case ADD:
                    String name, username, password;
                    password = commandInterpreter.hasArguments() ? commandInterpreter.nextArgument() : null;
                    username = commandInterpreter.hasArguments() ? commandInterpreter.nextArgument() : null;
                    name = commandInterpreter.hasArguments() ? commandInterpreter.nextArgument() : null;
                    if(username == null && password == null){ System.out.println("Invalid params check help command") ;continue;}
                    System.out.println(  DBContextMegaPD.getDBContext().registerUser(name, username, PasswordHasher.generateSecurePassword(password)) ? "Registered new user <"+username+">" : "Failed to register new user");
                break;
                case LS:
                    System.out.println("Users online ----------------------");
                    System.out.println("\t [id] => name");
                    DBContextMegaPD.getDBContext().getServerUsers().forEach((id, user) -> System.out.println("\t["+id+"] => "+ user));
                    System.out.println("------------------------------------");
                break;
                case CLS:
                    for (int i = 0; i < 50; ++i) System.out.println();
                    System.out.flush();
                    break;
                case HELP:
                    System.out.println(" commandDesc> CommandName: arg1 arg2 ... argN");
                    System.out.println("\tadd's a new user to the DB> " + CommandInterpreter.Commands.ADD.toString().toLowerCase() + " : name username password");
                    System.out.println("\tlists all users connected to this server> " + CommandInterpreter.Commands.LS.toString().toLowerCase());
                    System.out.println("\tturns off the server> " + CommandInterpreter.Commands.SHUTDOWN.toString().toLowerCase());
                    System.out.println("\tclears the screen> " + CommandInterpreter.Commands.CLS.toString().toLowerCase());
                    break;
                default:
                    System.out.println("Invalid command, please check the <help> command");
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
