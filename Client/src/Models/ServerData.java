package Models;

/**
 *
 * Server data, it represents the data the client needs to connect to a server
 *
 * @author Vascoide
 * @version 0.1
 */
public class ServerData {

    //Variables
    //----------------------------------------------------------------------------------------------
    //Private Variables

    private int ID;
    private String name;
    private String IP;
    private int port;
    private boolean status;

    //----------------------------------------------------------------------------------------------
    //      CONSTRUCTORS
    //----------------------------------------------------------------------------------------------

    /**
     * Creates the object to save the server data needed.
     * @param ID Server ID.
     * @param name Server Name.
     * @param IP Server IP.
     * @param port Server port.
     * @param status Server status, '1' if active and '0' if otherwise.
     */
    public ServerData(int ID, String name, String IP, int port, boolean status) {
        this.ID = ID;
        this.name = name;
        this.IP = IP;
        this.port = port;
        this.status = status;
    }

    //----------------------------------------------------------------------------------------------
    //      GETTERS
    //----------------------------------------------------------------------------------------------

    /**
     *  Gets the server's ID.
     *
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Gets the server's name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the server's IP
     *
     * @return IP
     */
    public String getIP() {
        return IP;
    }

    /**
     *
     * Gets the server's port.
     *
     * @return port
     */

    public int getPort() {
        return port;
    }

    /**
     * Gets the server's status.
     *
     * @return status
     */

    public boolean getStatus() {
        return status;
    }


}
