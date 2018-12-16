import Core.Log;
import Core.UserData;
import Models.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *  Responsible for accepting all initial requests of new Users
 *
 * @author FilipeA
 * @version 1.0
 */
public class ConnectionListenerThread implements Runnable{

    private List<UserData> users;
    private List<Thread> userThreads;
    private Notifier notifier;
    private Server server;

    public ConnectionListenerThread(Server server, List<UserData> users, Notifier notificationNotifier) {
        this.users = users;
        this.notifier = notificationNotifier;
        userThreads = new ArrayList<>();
        this.server = server;
    }

    @Override
    public void run() {

        while (Main.STATUS){
            try {
                Socket s;
                s = server.getServerSocket().accept();

                UserData user = new UserData(false);
                user.setSocket(s);
                user.setAddress(s.getRemoteSocketAddress().toString());

                Thread userThread = new Thread(new UserThread(user, notifier));

                users.add(user);
                userThreads.add(userThread);

                userThread.start();
                Log.i("[" + user.getAddress() + "] connected");
            }catch (SocketTimeoutException ignored){
            } catch (IOException e) {
                Log.w("Connection Listener [Connection failed]");
                //e.printStackTrace();
            }
        }
    }
}
