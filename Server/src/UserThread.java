import Core.Log;
import Core.Modules.ModuleInterface.ConnectionModule.ConnectionRequest;
import Core.UserData;
import Modules.Connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

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
                Enum enumRequested = connection.getRequest();

                if(enumRequested instanceof ConnectionRequest){
                    handleConnectionRequest((ConnectionRequest) enumRequested);
                    continue;
                }

                user.getSocketInput().reset();
                connection.newException("Request <"+enumRequested.toString()+"> not found");
                connection.sendData();
                user.getSocketOutput().flush();

            }catch (IOException e){
                Log.w("Connection to user ["+ user.getUsername() +"] lost");
                notifier.disconnect(user);
                break;
            } catch (ClassNotFoundException e) {
                Log.w("Failed to process request from user["+ user.getUsername() +"] lost");
            }
        }
        notifier.serverOff();
    }

    private void handleConnectionRequest(ConnectionRequest request) {
        try{
            switch (request) {
                case connect:
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
