package Core;
import com.mysql.cj.exceptions.UnableToConnectException;
import java.sql.*;


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
 * @version 0.1.0
 *
 */
public final class DBContextMegaPD {


    private static DBContextMegaPD context = null;
    private static boolean isConnected = false;

    //ConnectionVariables
    private boolean hasHost = false;
    private boolean hasDB = false;
    private boolean hasAuth = false;

    private String connectionString;
    private String username;
    private String password;

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
     *  [host] as been defined
     *  [database] as been defined
     *  [authentication] as been defined
     *
     * @throws IllegalStateException if there's already a connection in progress
     * @throws UnableToConnectException if host/database/authentication was not set OR couldn't access the database
     */
    public void connect(){
        if(isConnected) throw new IllegalStateException("Can't change connection while there's a connection active");
        //--------------------------------------
        if(!hasAuth || !hasDB || !hasHost){
            throw new UnableToConnectException("Connection requires host/db and credentials");
        }

        try {
            System.out.println("Started connection ...");
            this.connection = DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
            throw new UnableToConnectException("Connection failed, please check DB access/status \n" + e);
        }

        System.out.println("Connected successfully");
        isConnected = true;

        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            throw new UnableToConnectException("Connection failed to create statement, please check DB access/status \n" + e);
        }

        context = this;
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

    public int registerServer(String name, String ip, int connPort) throws SQLException {

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
                return generatedKeys.getInt(1);
            }
        }

        return 0;
    }
}
