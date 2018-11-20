package Models;

import Core.Log;
import Helpers.PasswordHasher;

import java.io.Serializable;
import java.net.*;
import java.util.List;

/**
 *
 *  User model, this class represents all the user information <br>
 *  <i>this model needs to be synced between client and server</i>
 *
 * @author FilipeA
 * @version 1.0
 */
public class User implements Serializable {

    public final static long serialVersionUID = 1L;

    //Variables
    //----------------------------------------------------------------------------------------------
    //Constant variables
    public final static String NOT_DEFINED = "notDefined";

    //Private Variables
    private int ID;
    private String username;
    private String hashedPassword;
    private boolean isAuthenticated = false;
    private String directory;
        //Network related variables
        private String IP;
            //Main server (connection module)
        private int serverID;
        private transient Socket connectionSocket;
            //Other servers (ping <-> broadcast module)
        private transient DatagramSocket pingSocket;
        private int pingPort;
            //Notifications module
        private transient Socket notificationSocket;
        private int notificationPort;
            //Chat module
        private transient Socket chatSocket;
        private int chatPort;
            //FileManager module
        private transient Socket fileManagerSocket;
        private int fileManagerPort;
            //FileTransfer module
        private transient List<Socket> fileTransfers;



    //Public Variables


    //----------------------------------------------------------------------------------------------
    //      CONSTRUCTOR'S
    //----------------------------------------------------------------------------------------------

    /**
     * Constructor with all modules + username and password <i>this constructor will consider the user has authenticated</i> <br>
     * This constructor is intended to be used on the client side as it gets the local IP address
     * @param username
     * @param hashedPassword
     * @param directory
     */
    public User(String username, String hashedPassword, String directory, int pingPort, int notificationPort, int chatPort, int fileManagerPort) {
        //Find machine real IP address
        try(final DatagramSocket socket = new DatagramSocket()){
            try {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            } catch (UnknownHostException e) {
                Log.w("Cant reach local IP [" + e + "]");
            }
            IP = socket.getLocalAddress().getHostAddress();
        } catch (SocketException e) {
            Log.w("Cant reach local IP [" + e + "]");
        }

        this.username = username;
        this.hashedPassword = hashedPassword;
        this.directory = directory;
        this.pingPort = pingPort;
        this.notificationPort = notificationPort;
        this.chatPort = chatPort;
        this.fileManagerPort = fileManagerPort;
        this.isAuthenticated = true;
    }

    /**
     * Constructor with all modules <i>this constructor will consider the user has <b>NOT</b> authenticated</i> <br>
     * This constructor is intended to be used on the client side as it gets the local IP address
     *
     */
    public User(String directory, int pingPort, int notificationPort, int chatPort, int fileManagerPort) {
        //Find machine real IP address
        try(final DatagramSocket socket = new DatagramSocket()){
            try {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            } catch (UnknownHostException e) {
                Log.w("Cant reach local IP [" + e + "]");
            }
            IP = socket.getLocalAddress().getHostAddress();
        } catch (SocketException e) {
            Log.w("Cant reach local IP [" + e + "]");
        }

        this.username = NOT_DEFINED;
        this.hashedPassword = NOT_DEFINED;
        this.directory = directory;
        this.pingPort = pingPort;
        this.notificationPort = notificationPort;
        this.chatPort = chatPort;
        this.fileManagerPort = fileManagerPort;
        this.isAuthenticated = true;
    }


    /**
     * Constructor with all modules + username and password <i>this constructor will consider the user has authenticated</i> <br>
     * This constructor is intended to be used on the server side
     * @param ID
     * @param username
     * @param hashedPassword
     * @param directory
     * @param connectionSocket
     * @param pingSocket
     * @param notificationSocket
     * @param chatSocket
     * @param fileManagerSocket
     */
    public User(int ID, String username, String hashedPassword, String directory, Socket connectionSocket, DatagramSocket pingSocket, Socket notificationSocket, Socket chatSocket, Socket fileManagerSocket) {
        this.ID = ID;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.directory = directory;
        this.connectionSocket = connectionSocket;
        this.pingSocket = pingSocket;
        this.notificationSocket = notificationSocket;
        this.chatSocket = chatSocket;
        this.fileManagerSocket = fileManagerSocket;
        this.isAuthenticated = true;
        this.IP = NOT_DEFINED;
    }

    /**
     * Constructor with all modules <i>this constructor will consider the user has <b>NOT</b> authenticated</i>
     * This constructor is intended to be used on the server side
     * @param ID
     * @param directory
     * @param connectionSocket
     * @param pingSocket
     * @param notificationSocket
     * @param chatSocket
     * @param fileManagerSocket
     */
    public User(int ID, String directory, Socket connectionSocket, DatagramSocket pingSocket, Socket notificationSocket, Socket chatSocket, Socket fileManagerSocket) {
        this.ID = ID;
        this.username = NOT_DEFINED;
        this.hashedPassword = NOT_DEFINED;
        this.directory = directory;
        this.connectionSocket = connectionSocket;
        this.pingSocket = pingSocket;
        this.notificationSocket = notificationSocket;
        this.chatSocket = chatSocket;
        this.fileManagerSocket = fileManagerSocket;
        this.isAuthenticated = false;
        this.IP = NOT_DEFINED;
    }




    //----------------------------------------------------------------------------------------------
    //      GETTERS
    //----------------------------------------------------------------------------------------------

    /**
     * Returns the user ID
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the ID of the main server
     * @return server id
     */
    public int getServerID() {
        return serverID;
    }

    /**
     * Returns the user current IP address
     * @return user ip
     */
    public String getIP() {
        return IP;
    }

    /**
     * Returns the username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the hashed version of user
     * @return hashed password
     */
    public String getPassword() {
        return hashedPassword;
    }

    /**
     * Checks if the is is authenticated
     * @return true if authenticated
     */
    public boolean isAuthenticated(){
        return this.isAuthenticated;
    }

    /**
     * Returns the user current directory name
     * @return directory name
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Returns the socket responsible for the connection between the main server and the user
     * @return connectionSocket
     */
    public Socket getConnectionSocket() {
        return connectionSocket;
    }

    /**
     * Returns the UDP socket responsible for the ping system
     * @return pingSocket
     */
    public DatagramSocket getPingSocket() {
        return pingSocket;
    }

    /**
     * Returns the <b>USER</b> local port for the ping module
     * @return UDP ping module port
     */
    public int getPingPort() {
        return pingPort;
    }

    /**
     * Returns the TCP socket responsible for the notification system
     * @return notificationSocket
     */
    public Socket getNotificationSocket() {
        return notificationSocket;
    }

    /**
     * Returns the <b>USER</b> local port for the notification module
     * @return TCP notification module port
     */
    public int getNotificationPort() {
        return notificationPort;
    }

    /**
     * Returns the TCP socket responsible for the chat system
     * @return chatSocket
     */
    public Socket getChatSocket() {
        return chatSocket;
    }

    /**
     * Returns the <b>USER</b> local port for the chat module
     * @return TCP chat module port
     */
    public int getChatPort() {
        return chatPort;
    }

    /**
     * Returns the TCP socket responsible for the file management system
     * @return fileManagerSocket
     */
    public Socket getFileManagerSocket() {
        return fileManagerSocket;
    }

    /**
     * Returns the <b>USER</b> local port for the notification module
     * @return TCP notification module port
     */
    public int getFileManagerPort() {
        return fileManagerPort;
    }


    //----------------------------------------------------------------------------------------------
    //      SETTERS
    //----------------------------------------------------------------------------------------------

    /**
     * sets the user ConnectionSocket (TCP) <br>
     * this socket is used to keep the connection between the user and the mainServer
     * @param connectionSocket socket to associate to the user
     */
    public void setConnectionSocket(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    /**
     * sets the user PingsSocket (UDP) <br>
     * this socket is used to verify if the user is still connected
     * @param pingSocket datagramSocket to associate to the user
     */
    public void setPingSocket(DatagramSocket pingSocket) {
        this.pingSocket = pingSocket;
    }



    /**
     * Sets the user IP
     * @param IP address
     */
    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * associates the user with a hashed password
     * @see Helpers.PasswordHasher
     * @param password to associate with the user
     */
    public void setPassword(String password){
        this.hashedPassword = PasswordHasher.generateSecurePassword(password);
    }

    //----------------------------------------------------------------------------------------------
    //      Methods
    //----------------------------------------------------------------------------------------------

    // Public Methods ------------------------------------------------------------------------------

    /**
     * Locks future access to the user
     */
    public void lock(){
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    /**
     * Unlocks access to the user
     */
    public void unlock(){
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    // Private Methods -----------------------------------------------------------------------------
}
