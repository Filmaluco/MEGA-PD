import Core.Log;
import Core.Modules.ModuleInterface.ConnectionModule.ConnectionRequest;
import Core.UserData;
import MegaPD.Core.Exeptions.MegaPDRemoteException;
import Modules.Connection;

import java.io.IOException;
import java.net.Socket;

/**
 *
 *  Each UserThread encapsulates all interactions between one specific client and this server.
 *
 * @author FilipeA
 * @version 1.1
 */
public class UserThread implements Runnable{

    UserData user;
    Notifier notifier;
    Connection connection;

    public UserThread(UserData userData, Notifier notifier){
        this.user = userData;
        this.notifier = notifier;
        this.connection = new Connection(userData);
    }


    @Override
    public void run() {
        while (Main.STATUS){
            try{
                //Receives the Request
                Enum enumRequested = connection.getRequest();

                //Connection module
                if(enumRequested instanceof ConnectionRequest){
                    handleConnectionRequest((ConnectionRequest) enumRequested);
                    continue;
                }

                //Request not recognized
                user.getSocketInput().reset();
                connection.newException("Request <"+enumRequested.toString()+"> not found");
                connection.sendData();
                user.getSocketOutput().flush();

            }catch (IOException e){
                //The socket with the user malfunction as such the connection was lost
                Log.w("Connection to user ["+ user.getUsername() +"] lost");
                //Check if he was authenticated
                if(user.isAuthenticaded()) {
                    try { connection.logout(); } catch (MegaPDRemoteException e1) { }
                }
                notifier.disconnect(user);
                break;
            } catch (ClassNotFoundException e) {
                //Failed to read the Request object
                Log.w("Failed to process request from user["+ user.getUsername() +"] lost");
            }
        }
        //Server is about to shutdown, notify all main clients
        notifier.serverOff();
    }

    /**
     * Handles all requests made to the Connection Module
     * @param request enum with the request made
     */
    private void handleConnectionRequest(ConnectionRequest request) {
        try{
            switch (request) {
                case connect:
                    //User requested a connect method
                    Socket notificationS = connection.connect((String)user.getSocketInput().readObject(), (Integer) user.getSocketInput().readObject());
                    user.setNotificationSocket(notificationS, false);
                    Log.i("Established connection with user[" + user.getUsername() +"");
                    break;

                case login:
                    break;

                case logout:
                    break;

                case getUser:
                    break;

                case getUsersOnline:
                    break;

                default:
                    //Request not recognized (this should not happen unless the versions of the Core library are not the same)
                    user.getSocketInput().reset();
                    connection.newException("Request <" + request.toString() + "> not found");
                    connection.sendData();
                    user.getSocketOutput().flush();
                    break;
            }
        }catch(Exception e){
            Log.w("Failed to process user["+user.getUsername()+"] request");
        }
    }
}
