import Core.Log;
import Core.Modules.ModuleInterface.ConnectionModule.ConnectionRequest;
import Core.UserData;
import MegaPD.Core.Exeptions.MegaPDRemoteException;
import Models.Server;
import Modules.Connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 *
 *  Responsible for accepting all initial requests of new Users
 *
 * @author FilipeA
 * @version 1.0
 */
public class ConnectionListenerThread implements Runnable{

    private HashMap<Integer, UserData> users;
    private HashMap<Integer, Thread> userThreads;
    private Notifier notifier;
    private Server server;

    public ConnectionListenerThread(Server server, HashMap<Integer, UserData> users, Notifier notificationNotifier) {
        this.users = users;
        this.notifier = notificationNotifier;
        userThreads = new HashMap<>();
        this.server = server;
    }

    @Override
    public void run() {

        while (Main.STATUS){
            try {
                Socket s;
                s = server.getServerSocket().accept();

                //New user connection ----------------------------------------------------------------------------------
                //Create base user data
                UserData user = new UserData(false);
                user.setSocket(s);
                user.setAddress(s.getInetAddress().toString().substring(1));

                //Creates the initial connection
                Connection userConnection = new Connection(user);
                int userID;
                try {
                    //Receives the Request for Guest Login or Auth login
                    ConnectionRequest request = (ConnectionRequest) user.getConnectionIn().readObject();
                    user.getConnectionIn().readObject(); //will also receive a Integer just discard ...

                    switch (request) {
                        case guestLogin:
                            userID = userConnection.login();
                            notifier.updateUsers(user.getUsername() +" connected", userID);
                            Log.i("Established connection with [" + user.getAddress() + "], designated as <"+user.getUsername()+">");
                        break;
                        case AuthLogin:
                            try {
                                userID = userConnection.login((String) user.getConnectionIn().readObject(), (String) user.getConnectionIn().readObject());
                                notifier.updateUsers(user.getUsername() +" connected", userID);
                                Log.i("Established connection with [" + user.getAddress() + "], designated as <"+user.getUsername()+">");
                            }catch (MegaPDRemoteException e){
                                Log.i("Failed attempt to connect login from ["+user.getAddress()+"]");
                                user.getConnectionOut().writeObject(new MegaPDRemoteException("Failed to Login \n" + e.getClass().getName()));
                                continue; }
                        break;
                        default:
                            throw new ClassNotFoundException("Bad request");
                    }
                }catch (ClassNotFoundException e) {
                    Log.w("Connection Listener [Connection failed - Bad Request]");
                    user.getConnectionOut().writeObject(new MegaPDRemoteException("Failed to connect to remote host \n" + e.getClass().getName()));
                    //e.printStackTrace();
                    continue;
                    //e.printStackTrace();
                }catch (Exception e){
                    Log.w("Connection with "+ user.getAddress()+ " failed");
                    user.getConnectionOut().writeObject(new MegaPDRemoteException("Failed to connect to remote host \n" + e.getClass().getName()));
                    e.printStackTrace();
                    continue;
                }

                //Creates user Thread
                Thread userThread = new Thread(new UserThread(userConnection, notifier));
                //Adds user to user list
                users.put(userID,user);
                userThreads.put(userID, userThread);

                //Starts user Thread
                userThread.start();
                //Log.i("[" + user.getAddress() + "] connected");
            }catch (SocketTimeoutException ignored){
            } catch (IOException e) {
                Log.w("Connection Listener [Connection failed - Socket]");
                //e.printStackTrace();
            }
        }

    }
}
