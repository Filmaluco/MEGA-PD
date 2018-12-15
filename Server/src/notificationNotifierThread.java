import Core.UserData;

import java.util.List;

public class notificationNotifierThread implements Runnable {

    private List<UserData> users;
    public notificationNotifierThread(List<UserData> users) {
        this.users = users;
    }

    @Override
    public void run() {

        while (Main.STATUS){

        }
    }
}
