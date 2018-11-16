import Core.Log;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Log.initLog();
        Log.exit("Oh hi Mark!");
        Log.i("Testing...");
    }
}
