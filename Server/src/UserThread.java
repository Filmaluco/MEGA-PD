import Core.Log;
import Core.MegaPDFile;
import Core.Modules.ModuleInterface.ConnectionModule.ConnectionRequest;
import Core.Modules.ModuleInterface.FileManagerModule.FileManagerRequest;
import Core.UserData;
import MegaPD.Core.Exeptions.MegaPDRemoteException;
import Modules.Connection;
import Modules.FileManager;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

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
    FileManager fileManager;

    public UserThread(UserData userData, Notifier notifier){
        this.user = userData;
        this.notifier = notifier;
        this.connection = new Connection(userData);
        this.fileManager = new FileManager(userData);
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
                //FileManager module
                if(enumRequested instanceof FileManagerRequest){
                    handleFileManagerRequest((FileManagerRequest) enumRequested);
                    continue;
                }


                //Request not recognized
                user.getSocketInput().reset();
                connection.newException("Request <"+enumRequested.toString()+"> not found");
                connection.sendData();
                user.getSocketOutput().flush();

            }catch (IOException e){
                //The socket with the user malfunction as such the connection was lost
                Log.w("Connection to user ["
                        +  ( user.getUsername() == null ? user.getAddress() : user.getUsername())
                        +"] lost");
                //Check if he was authenticated
                if(user.isAuthenticaded()) {
                    try { connection.logout(); } catch (MegaPDRemoteException e1) { }
                }
                notifier.disconnect(user);
                break;
            } catch (ClassNotFoundException e) {
                //Failed to read the Request object
                Log.w("Failed to process request from user["+ user.getAddress() +"] lost");
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
                    notifier.updateUsers((user.getUsername() == null ? "<GuestUser>" : user.getUsername()) + "connected");
                    Log.i("Established connection with user[" + user.getAddress() +"]");
                    break;

                case login:
                    user.setID(connection.login((String)user.getSocketInput().readObject(), (String) user.getSocketInput().readObject()));
                    //Todo update user Info based on the ID given
                    notifier.updateUsers(user.getUsername() + "logged in");
                    Log.i("User[" + user.getUsername() + "] authenticated");
                    break;

                case logout:
                    connection.logout();
                    break;

                case getUser:
                    connection.getUser((Integer) user.getSocketInput().readObject());
                    break;

                case getUsersOnline:
                    connection.getUsersOnline();
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
            Log.w("Failed to process user["
                    +  ( user.getUsername() == null ? user.getAddress() : user.getUsername())
                    +"] request " + request.toString());
        }
    }

    private void handleFileManagerRequest(FileManagerRequest request) {
        try{
            switch (request) {
                case updateFiles:
                    fileManager.updateFiles((List<MegaPDFile>) user.getSocketInput().readObject());
                    break;
                case addFile:
                    fileManager.addFile((MegaPDFile) user.getSocketInput().readObject());
                    break;
                case remove:
                    fileManager.remove((MegaPDFile) user.getSocketInput().readObject());
                    break;
                case updateFile:
                    fileManager.updateFile((String) user.getSocketInput().readObject(), user.getSocketInput().readLong());
                    break;
                case getUserFiles:
                    fileManager.getUserFiles((Integer) user.getSocketInput().readObject());
                    break;
                case requestFile:
                    fileManager.requestFile((String) user.getSocketInput().readObject(), (Integer) user.getSocketInput().readObject());
                    break;
                case completeFileTransfer:
                    fileManager.completeFileTransfer((Integer) user.getSocketInput().readObject());
                    break;
                case getFileHistory:
                    fileManager.getFileHistory();
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
            Log.w("Failed to process user["
                    +  ( user.getUsername() == null ? user.getAddress() : user.getUsername())
                    +"] request " + request.toString() + "\n" + e);
            //e.printStackTrace();
        }
    }
}
