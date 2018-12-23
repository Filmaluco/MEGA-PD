package Core;
import com.mysql.cj.exceptions.UnableToConnectException;
import java.sql.*;
import java.util.*;


/**
 * <p><strong>DBContextMegaPD</strong> is the class responsible for handling the connection and interaction with the DB</p>
 * <p>In order to use this class it first needs to be initialized. To initialize it you need to:</p>
 * <p><strong>new DBContextMegaPD()</strong>.<em>setHost(</em>host<em>)</em><br /><em>.setDB(</em>dbName<em>)</em><br /><em>.setAuth(</em>username<em>, </em>password<em>)</em><br /><em>.connect();</em></p>
 * <p>&nbsp;</p>
 * <p>after being initialized once, all it has to be done to access the connection is:</p>
 * <p><strong> DBContextMegaPD</strong> var = <strong>DBContextMegaPD</strong>.<em>getDBContext();</em></p>
 *
 *
 * @author FilipeA
 * @version 1.0.0
 *
 */
public final class DBContextMegaPD {


    //Static access to the DB
    private static DBContextMegaPD context = null;
    private static boolean isConnected = false;

    //ConnectionVariables
    private boolean hasHost = false;
    private boolean hasDB = false;
    private boolean hasAuth = false;

    private String connectionString;
    private String username;
    private String password;

    //ControlVariables
    private boolean isRegistered = false;
    private int serverID = 0;

    //mysql connector variables
    private Connection connection;
    private Statement stmt = null;


    //-----------------------------------------------------------------------------------------------------------------
    //                                      Methods for connection to the database
    //-----------------------------------------------------------------------------------------------------------------

    /**
     * Chooses the database url for the connection
     *
     * @param host url to connect
     * @throws IllegalStateException if there's already a connection active
     *
     */
    public DBContextMegaPD setHost(String host){
        if(isConnected) throw new IllegalStateException("Can't change host while there's a connection active");
        //--------------------------------------
        this.connectionString = "jdbc:mysql://";
        this.connectionString += host;
        this.connectionString += ":3306";
        this.hasHost = true;
        return this;
    }

    /**
     * Chooses the database for the connection
     *
     * @param dbname database name
     * @throws IllegalStateException if there's already a connection active
     *
     */
    public DBContextMegaPD setDB(String dbname){
        if(isConnected) throw new IllegalStateException("Can't change DB while there's a connection active");
        //--------------------------------------
        this.connectionString += "/" + dbname;
        this.connectionString += "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.hasDB = true;
        return this;
    }


    /**
     * Chooses the database authentication for the connection
     *
     * @param username auth username
     * @param password auth password
     * @throws IllegalStateException if there's already a connection active
     *
     */
    public DBContextMegaPD setAuth(String username, String password){
        if(isConnected) throw new IllegalStateException("Can't change user while there's a connection active");
        //--------------------------------------
        this.username = username;
        this.password = password;
        this.hasAuth = true;
        return this;
    }

    /**
     * Connect to the database as long as the
     *  <br>[host] as been defined
     *  <br>[database] as been defined
     *  <br>[authentication] as been defined
     *
     * @throws IllegalStateException if there's already a connection in progress
     * @throws UnableToConnectException if host/database/authentication was not set OR couldn't access the database
     */
    public void connect(){
        //--------------------------------------
        if(!hasAuth || !hasDB || !hasHost){
            throw new UnableToConnectException("Connection requires host/db and credentials");
        }
        try {
            this.connection = DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
            throw new UnableToConnectException("Connection failed, please check DB access/status \n" + e);
        }

        isConnected = true;

        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            throw new UnableToConnectException("Connection failed to create statement, please check DB access/status \n" + e);
        }

        context = this;
    }

    /**
     * Puts the servers status Offline and disconnects from the remote database
     */
    public void disconnect() throws SQLException {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to disconnect");
        this.connect();

        //Delete all files from users connected to this server
        String sql = "DELETE FROM Files WHERE UserID IN( SELECT UserID FROM Server_Users Where ServerID = "+this.serverID+")";
        stmt.executeUpdate(sql);

        //Remove current server from active
        sql = "UPDATE `Servers` SET `Status` = '0' WHERE `Servers`.`ID` = "+ this.serverID +";";
        stmt.executeUpdate(sql);

        isConnected = false;
        //Close connection
        //JConnector is losing the connection after N time, the system is doing an autoReconnect there's no need to close because he loses the connection every N time
        if (stmt != null) stmt.close();
        if (connection != null) connection.close();
    }

    /**
     * Static method for access from anywhere, as long as there's a connection already established
     *
     * @return Context for the connection already in progress
     * @throws UnableToConnectException if there's no connection in progress
     */
    public static DBContextMegaPD getDBContext() {
        if(!isConnected) throw new UnableToConnectException("Connection was lost \n");
        //--------------------------------------
        return context;
    }

    //-----------------------------------------------------------------------------------------------------------------
    //                                      Methods to access the database
    //-----------------------------------------------------------------------------------------------------------------

    /**
     * This method register the base Server info in the database
     *
     * @param name identification name to register in the DB
     * @param ip identification IP of the server
     * @param connPort TCP port to accept connections
     * @return serverID
     * @throws Exception
     *  <br> if it can't register the Server properly (1st)
     *  <br> <b>or</b> if it was already registered
     */
    public int registerServer(String name, String ip, int connPort) throws Exception {
        if(isRegistered) throw  new Exception();

        //No need to prepare statement, if someone has access to this method it already has all the power he wants
        String sql = "INSERT INTO `Servers` (`ID`, `Name`, `IP`, `Port`, `Status`) " +
                "VALUES"+"(NULL, '"+name+"', '"+ip+"', '"+connPort+"', '1');";
        try {
            stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            System.out.println("Couldn't register the server");
            e.printStackTrace();
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                isRegistered = true;
                this.serverID = generatedKeys.getInt(1);
                generatedKeys.close();
                return this.serverID;
            }
        }

        isRegistered = false;
        return 0;
    }

    public boolean registerUser(String name, String username, String generateSecurePassword) {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();

        String sql = "INSERT INTO `filmaluc_PD`.`User` (`ID`, `IP_Address`, `ConnectionTCP_Port`, `NotificationTCP_Port`, `FileTransferTCP_Port`, `Ping_UDP_Port`, `Name`, `Username`, `Password`, `Blocked`) " +
                "VALUES (NULL, 'notDefinedYet', '0', NULL, NULL, NULL, '"+name+"', '"+username+"', '"+generateSecurePassword+"', '0');";

        try {
           int worked = stmt.executeUpdate(sql);
           if(worked!=1) throw new SQLException();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @param ip
     * @param connectionPort
     * @return
     */
    public int loginGuestUser(String ip, int connectionPort) {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();
        int guestID = -1;

        try {
            //Sql statement to create a new user
            String sql = "INSERT INTO `filmaluc_PD`.`User` " +
                    "(`ID`, `IP_Address`, `ConnectionTCP_Port`, `NotificationTCP_Port`, `FileTransferTCP_Port`, `Ping_UDP_Port`, `Name`, `Username`, `Password`, `Blocked`) " +
                    "VALUES (NULL, ?, ?, NULL, NULL, NULL, ?, NULL, NULL, 0);";

            //Create the GUEST USER
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, ip);
            preparedStatement.setInt(2, connectionPort);
            preparedStatement.setString(3, "GestUser");
            preparedStatement.executeUpdate();

            //Retrieve GUEST USER ID
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if(rs.next()){
                guestID = rs.getInt(1);
            }

            //Sql statement to link new user to the current server
            sql = "INSERT INTO `filmaluc_PD`.`Server_Users` (`ServerID`, `UserID`, `Errors`, `JoinedDate`, `Status`) VALUES (?, ?, '0', CURRENT_TIMESTAMP, '1');";
            //Link GuestUser to this server
            preparedStatement = connection
                    .prepareStatement(sql);
            preparedStatement.setInt(1, this.serverID);
            preparedStatement.setInt(2, guestID);
            preparedStatement.executeUpdate();

            //Return the guestID
            return guestID;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToConnectException("Failed to register new user");
        }
    }

    /**
     *
     * @param username
     * @param hashedPassword
     * @return
     * @throws Exception
     */
    public int loginUser(String username, String hashedPassword) throws Exception {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();

        String sql;
        PreparedStatement preparedStatement;
        int userID = -1;

        try {
        //Sql statement to check if user Exists ------------------------------------------------------------------------
        sql = "SELECT * FROM `User` WHERE `Username` = ? LIMIT 1 ";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.execute();

        //Retrieve USER ID
        ResultSet rs = preparedStatement.getResultSet();

        if(rs.next()){
            userID = rs.getInt(1);
        }

        if(userID == -1) throw new Exception("User does not exists");

        //Sql statement to check if password matches -------------------------------------------------------------------
        sql = "SELECT * FROM `User` WHERE `Username` = ? AND `Password` = ? LIMIT 1";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, hashedPassword);
        preparedStatement.execute();

        if(!(preparedStatement.getResultSet().next())) throw new Exception("Wrong Password");


        //Sql statement to link new user to the current server ---------------------------------------------------------
        sql = "INSERT INTO `filmaluc_PD`.`Server_Users` (`ServerID`, `UserID`, `Errors`, `JoinedDate`, `Status`) VALUES (?, ?, '0', CURRENT_TIMESTAMP, '1');";
        //Link GuestUser to this server
        preparedStatement = connection
                .prepareStatement(sql);
        preparedStatement.setInt(1, this.serverID);
        preparedStatement.setInt(2, userID);
        preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("DB Query failed");
        }

        return userID;
    }

    /**
     *
     * @param userID
     */
    public void logoutUser(int userID){
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();
        try {
            // First make sure he is no longer connected to the server
        String sql = "UPDATE `Server_Users` SET `Status` = 0 WHERE `ServerID` = ? AND `UserID` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, this.serverID);
        preparedStatement.setInt(2, userID);
        preparedStatement.executeUpdate();

            //Then make sure to reset all his files by deleting this session files

            sql = "DELETE FROM `Files` WHERE `UserID` = ?";
            preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, userID);
        preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToConnectException("Failed to properly logoutUser");
        }
    }

    /**
     *
     * @param userID
     * @return
     * @throws Exception
     */
    public UserInfo getUser(int userID) throws Exception {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();

        UserInfo userInfo = null;

        String name, username, address;
        int id, connectionPort, notificationPort, fileTransferPort, pingPort;

        String sql;
        PreparedStatement preparedStatement;

        try {
            //Sql statement to check if user Exists ------------------------------------------------------------------------
            sql = "SELECT * FROM `User` WHERE `ID` = ? LIMIT 1";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            preparedStatement.execute();

            //Retrieve USER ID
            ResultSet rs = preparedStatement.getResultSet();

            if (rs.next()) {
                id = rs.getInt(1);
                address = rs.getString(2);
                connectionPort = rs.getInt(3);
                notificationPort = rs.getInt(4);
                fileTransferPort = rs.getInt(5);
                pingPort = rs.getInt(6);
                name = rs.getString(7);
                username = rs.getString(8);

                userInfo = new UserInfo(id, name, username, address, connectionPort, notificationPort, fileTransferPort, pingPort);
            }

            if(userInfo==null) throw new Exception("ID incorrect");

        }catch (SQLException e){
            e.printStackTrace();
            throw new UnableToConnectException("Failed to obtain user info");
        }

        return userInfo;
    }

    public UserInfo getUser(String uName) throws Exception {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();

        UserInfo userInfo = null;

        String name, username, address;
        int id, connectionPort, notificationPort, fileTransferPort, pingPort;

        String sql;
        PreparedStatement preparedStatement;

        try {
            //Sql statement to check if user Exists ------------------------------------------------------------------------
            sql = "SELECT * FROM `User` WHERE `Username` = ? LIMIT 1";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uName);
            preparedStatement.execute();

            //Retrieve USER ID
            ResultSet rs = preparedStatement.getResultSet();

            if (rs.next()) {
                id = rs.getInt(1);
                address = rs.getString(2);
                connectionPort = rs.getInt(3);
                notificationPort = rs.getInt(4);
                fileTransferPort = rs.getInt(5);
                pingPort = rs.getInt(6);
                name = rs.getString(7);
                username = rs.getString(8);

                userInfo = new UserInfo(id, name, username, address, connectionPort, notificationPort, fileTransferPort, pingPort);
            }

            if(userInfo==null) throw new Exception("ID incorrect");

        }catch (SQLException e){
            e.printStackTrace();
            throw new UnableToConnectException("Failed to obtain user info");
        }

        return userInfo;
    }

    /**
     *
     * @param user
     * @param port
     */
    public void updateUserNotificationPort(int user, int port) {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();
        try {
            String sql = "UPDATE `filmaluc_PD`.`User` SET `NotificationTCP_Port` = ? WHERE `user`.`ID` = ?; ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, port);
            preparedStatement.setInt(2, user);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToConnectException("Failed to register new user");
        }
    }

    //EU SEI A REDUNDANCIA!!!!! SIGAM EM FRENTE VOCES NAO VIRAM NADA! (sorry time is short...)
    /**
     *
     * @param user
     * @param port
     */
    public void updateUserFileTransferPort(int user, int port) {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();
        try {
            String sql = "UPDATE `filmaluc_PD`.`User` SET `FileTransferTCP_Port` = ? WHERE `user`.`ID` = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, port);
            preparedStatement.setInt(2, user);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToConnectException("Failed to register new user");
        }
    }

    /**
     *
     * @param user
     * @param address
     */
    public void updateUserAddress(int user, String address) {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();
        try {
            String sql = "UPDATE `filmaluc_PD`.`User` SET `User`.`IP_Address` = ? WHERE `user`.`ID` = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, address);
            preparedStatement.setInt(2, user);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToConnectException("Failed to register new user");
        }
    }

    /**
     *
     * @return
     */
    public Map<Integer, String> getServerUsers(){
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();

        Map<Integer, String> usersOnline = new HashMap<>();
        String sql;
        PreparedStatement preparedStatement;
        try {
            sql = "SELECT * FROM `Server_Users` WHERE `ServerID` = ? AND `Status` = 1;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, serverID);
            preparedStatement.execute();

            ResultSet rs = preparedStatement.getResultSet();
            int id;

            while (rs.next()) {
                id = rs.getInt(2);
                usersOnline.put(id, this.getUser(id).getName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToConnectException("Failed to get server users");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersOnline;
    }

    /**
     *
     * @return
     */
    public Map<Integer, String> getServerAuthUsers(){
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();

        Map<Integer, String> usersOnline = new HashMap<>();
        String sql;
        PreparedStatement preparedStatement;
        try {
            sql = "SELECT * FROM `User` WHERE ID IN (SELECT UserID FROM Server_Users WHERE `ServerID` = ? AND `Status` = 1) AND `PASSWORD` IS NOT NULL ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, serverID);
            preparedStatement.execute();

            ResultSet rs = preparedStatement.getResultSet();
            int id;
            String username;

            while (rs.next()) {
                id = rs.getInt(1);
                username = rs.getString(8);
                usersOnline.put(id, username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToConnectException("Failed to get server users");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersOnline;
    }

    public void addFile(int userID, String fileName, long fileSize){
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();
        try {
            String sql = "INSERT INTO `filmaluc_PD`.`Files` (`ID`, `UserID`, `Name`, `Directory`, `Size`) VALUES (NULL, ?, ?, '~/MegaPDFiles', ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, fileName);
            preparedStatement.setLong(3, fileSize);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to register file (confirm fileSize and Name lenght)");
        }
    }

    public void removeFile(int userID, String fileName){
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();
        try {
            String sql = "DELETE FROM `Files` WHERE `UserID` = ? AND `Name` = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, fileName);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to remove file");
        }
    }

    public List<MegaPDFile> getUserFiles(int userID) {
        if(!isConnected && isRegistered) throw new IllegalStateException("There's no connection to DB");
        this.connect();

        List<MegaPDFile> fileList = new ArrayList<>();
        String sql;

        try{
            sql = "SELECT `ID`, `UserID`, `Name`, `Directory`, `Size` FROM `Files` WHERE `UserID` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userID);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()){
                fileList.add(new MegaPDFile(result.getInt(1), result.getInt(2), result.getString(4), result.getString(3), "", result.getLong(5)));
            }

        }catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException("Failed retrieve user files (check if the user is still connected)");
        }

        return fileList;
    }
}
