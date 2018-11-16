import Core.DBContextMegaPD;
import Core.Log;
import Modules.Connection;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException {

        //--------------------------------------------------------------------------------------------------------------
        // Variables
        //--------------------------------------------------------------------------------------------------------------
        final double VERSION = 0.1;
        final String DESIGNATION = "SERVER";
        int serverID;
        //--------------------------------------------------------------------------------------------------------------
        //Connection Module
        Thread connectionModule = null;
        Connection connectionData = new Connection();


        //--------------------------------------------------------------------------------------------------------------
        // Main Code
        //--------------------------------------------------------------------------------------------------------------
        Log.initLog();
        Log.i("Server [Started]");

        // Starting DB context -----------------------------------------------------------------------------------------
        try {
            new DBContextMegaPD().setHost(args[0])
                                .setDB(args[1])
                                .setAuth(args[2], args[3])
                                .connect();
        }catch (Exception e){
          Log.s("Couldn't create DB connection [" + e + "]");
          return;
        }

        // Starting ConnectionModule -----------------------------------------------------------------------------------
        Log.i("ConnectionModule [Started]");
        connectionModule = new Thread(connectionData);
        connectionModule.start();


        // Register the server  ----------------------------------------------------------------------------------------
        try {
            serverID = DBContextMegaPD.getDBContext().registerServer(DESIGNATION+VERSION, connectionData.getIp(), connectionData.getPort());
        } catch (Exception e) {
            Log.s("Couldn't register the server [" + e + "]");
            return;
        }
        Log.i("Server [Registered]");
        Log.i("Server["+serverID+"] info: \n Name: "+ DESIGNATION+VERSION +
                                "\n Address: " + connectionData.getIp() +
                                "\n Port: " + connectionData.getPort());


        try {
            DBContextMegaPD.getDBContext().disconnect();
        } catch (SQLException e) {
            Log.s("Couldn't properly disconnect the Server please contact the DB administrator");
        }

        //--------------------------------------------------------------------------------------------------------------
        // Closing threads
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
    }
}
