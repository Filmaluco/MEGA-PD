import Core.UserData;

import java.util.ArrayList;
import java.util.List;

public class ConnectionListenerThread implements Runnable{

    private List<UserData> users;
    private List<UserThread> userThreads;

    public ConnectionListenerThread(List<UserData> users) {
        this.users = users;
        userThreads = new ArrayList<>();
    }

    @Override
    public void run() {

        while (Main.STATUS){

        }
    }
}
