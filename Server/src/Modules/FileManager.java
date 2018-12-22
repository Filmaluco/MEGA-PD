package Modules;

import Core.DBContextMegaPD;
import Core.Log;
import Core.MegaPDFile;
import Core.MegaPDHistory;
import Core.Modules.EntityData;
import Core.Modules.MegaPDModule;
import Core.Modules.ModuleInterface.FileManagerModule;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.util.List;

public class FileManager extends MegaPDModule implements FileManagerModule {

    DBContextMegaPD dbContext = DBContextMegaPD.getDBContext();
    private int userID;

    public FileManager(Connection conn, int userID) {
        super(conn.getUserData());
        this.userID = userID;
    }

    @Override
    public void updateFiles(List<MegaPDFile> list) {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return;
    }

    @Override
    public void addFile(MegaPDFile megaPDFile) throws IOException{
        try {
            if(megaPDFile.getFileName().length() >= 30){throw new Exception("Data truncation: name too long"); } else
            dbContext.addFile(userID, megaPDFile.getFileName(), megaPDFile.getFileSize());
        } catch (Exception e) {
            this.newException(e.getMessage());
            //e.printStackTrace();
        }

        sendData();
        return;
    }

    @Override
    public void remove(MegaPDFile megaPDFile) throws IOException {
        try {
            dbContext.removeFile(userID, megaPDFile.getFileName());
        } catch (Exception e) {
            this.newException(e.getMessage());
            e.printStackTrace();
        }

        sendData();
        return;
    }

    @Override
    public void updateFile(String s, long l) {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return;
    }

    @Override
    public List<MegaPDFile> getUserFiles(int i) {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public int requestFile(String s, int i) {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void completeFileTransfer(int i) {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return;
    }

    @Override
    public List<MegaPDHistory> getFileHistory() {
        this.newException("Not yet implemented");
        try {
            sendData();
        } catch (IOException e) {
            Log.w("Failed to transmit data to the user");
            //e.printStackTrace();
        }
        return null;
    }
}
