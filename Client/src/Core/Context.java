package Core;

import Modules.Connection;

public final class Context {
    private static ServerData serverData;
    private static Connection connection;

    public static ServerData getServerData() {
        return serverData;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setServerData(ServerData serverData) {
        Context.serverData = serverData;
    }

    public static void setConnection(Connection connection) {
        Context.connection = connection;
    }
}
