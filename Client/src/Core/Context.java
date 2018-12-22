package Core;

import Modules.Connection;
import Modules.NotificationManager;

public final class Context {

    private static UserData user;
    private static ServerData serverData;
    private static Connection connection;
    private static NotificationManager notificationManager;

    public static ServerData getServer() {
        return serverData;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static UserData getUser() {
        return user;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static void setServer(ServerData serverData) {
        Context.serverData = serverData;
    }

    public static void setConnection(Connection connection) {
        Context.connection = connection;
    }

    public static void setUser(UserData user) {
        Context.user = user;
    }

    public static void setNotificationManager(NotificationManager notificationManager) {
        Context.notificationManager = notificationManager;
    }
}
