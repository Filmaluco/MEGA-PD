package Modules;

import Core.Log;

import java.io.IOException;
import java.net.*;

/**
 * Connection Module, this class is responsible for accepting request's from the clients
 *
 * @author FilipeA
 * @version 0.1.0
 */
public class Connection implements Runnable {

    //Control variables
    private boolean status = true;

    //Connection variables
    private int port;
    private String ip = "UNDEFINED";

    //TCP socket variables
    ServerSocket serverS;


    public Connection() throws IOException {
        serverS = new ServerSocket(0);
        port = serverS.getLocalPort();

    }

    /**
     * Gets the machine main IP to remote host
     *
     * @return IP
     */
    public String getIp() {
        try(final DatagramSocket socket = new DatagramSocket()){
            try {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            } catch (UnknownHostException e) {
                Log.w("Cant reach local IP [" + e + "]");
            }
            ip = socket.getLocalAddress().getHostAddress();
        } catch (SocketException e) {
            Log.w("Cant reach local IP [" + e + "]");
        }

        return ip;
    }

    /**
     * Gets the machine TCP port to remote host
     *
     * @return port
     */
    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        while (status){



        }
    }

    /**
     * Stops connection module from accepting more request's
     */
    public void close() {
        status = false;
    }
}
