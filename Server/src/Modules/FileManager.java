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
    public List<MegaPDFile> getUserFiles(int i) throws IOException {
        List<MegaPDFile> fileList;
        try {
            fileList = dbContext.getUserFiles(i);
        } catch (Exception e) {
            this.newException(e.getMessage());
            e.printStackTrace();
            sendData();
            return null;
        }

        sendData(fileList);
        return fileList;
    }

    @Override
    public int requestFile(String fileName, int userID) throws IOException {
        int requestID = -1;
        try {
            requestID = dbContext.registerFileTransfer(this.userID, userID, fileName);
        }catch (Exception e){
            this.newException("Request denied");
            e.printStackTrace();
            sendData();
            return -1;
        }

        sendData(requestID);
        return requestID;
    }

    @Override
    public void completeFileTransfer(int requestID) throws IOException {
        try {
            dbContext.completeFileTransfer(requestID);
        }catch (Exception e){
            this.newException("Request denied");
            e.printStackTrace();
            sendData();
            return;
        }

        sendData();
        return;
    }

    @Override
    public List<MegaPDHistory> getFileHistory() throws IOException{
        List<MegaPDHistory> fileList;
        try {
            fileList = dbContext.getUserFilesHistory(userID);
        } catch (Exception e) {
            this.newException(e.getMessage());
            e.printStackTrace();
            sendData();
            return null;
        }

        sendData(fileList);
        return fileList;
    }
}
