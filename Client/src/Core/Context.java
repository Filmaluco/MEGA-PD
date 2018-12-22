package Core;

import Modules.RemoteModules.Connection;
import Modules.RemoteModules.FileManager;
import Modules.NotificationManager;

public final class Context {

    private static UserData user;
    private static ServerData serverData;
    private static Connection connection;
    private static FileManager fileManager;
    private static NotificationManager notificationManager;

    public static UserData getUser() {
        return user;
    }
    public static ServerData getServer() {
        return serverData;
    }
    public static Connection getConnection() {
        return connection;
    }
    public static NotificationManager getNotificationManager() { return notificationManager; }
    public static FileManager getFileManager() { return fileManager; }

    public static void setUser(UserData user) {
        Context.user = user;
    }
    public static void setServer(ServerData serverData) {
        Context.serverData = serverData;
    }
    public static void setConnection(Connection connection) {
        Context.connection = connection;
    }
    public static void setNotificationManager(NotificationManager notificationManager) { Context.notificationManager = notificationManager; }
    public static void setFileManager(FileManager fileManager) { Context.fileManager = fileManager; }
}
