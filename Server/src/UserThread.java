import Core.UserData;

public class UserThread implements Runnable{

    UserData user;
    Notifier notifier;

    public UserThread(UserData userData, Notifier notifier){
        this.user = userData;
        this.notifier = notifier;
    }


    @Override
    public void run() {

    }
}
