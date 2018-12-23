package Core;

import Modules.FolderManager;
import Modules.NotificationManager;
import Modules.RemoteModules.Connection;
import Modules.RemoteModules.FileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class Context {

    private static UserData user;
    private static ServerData serverData;
    private static Connection connection;
    private static FileManager fileManager;
    private static NotificationManager notificationManager;
    private static FolderManager folderManager;
    private static ObservableList<String> usersList = FXCollections.observableArrayList();

    public static UserData getUser() {
        return user;
    }
    public static ServerData getServer() {
        return serverData;
    }
    public static Connection getConnection() { return connection; }
    public static NotificationManager getNotificationManager() { return notificationManager; }
    public static FileManager getFileManager() { return fileManager; }
    public static FolderManager getFolderManager() { return folderManager; }
    public static ObservableList<String> getUsersList() { return usersList; }

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
    public static void setFolderManager(FolderManager folderManager) { Context.folderManager = folderManager; }
}
