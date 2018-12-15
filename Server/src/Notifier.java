import Core.UserData;

import java.util.List;

public class Notifier implements Runnable {

    private List<UserData> users;
    public Notifier(List<UserData> users) {
        this.users = users;
    }

    @Override
    public void run() {

        while (Main.STATUS){

        }
    }
}
