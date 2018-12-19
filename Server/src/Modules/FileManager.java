package Modules;

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

    public FileManager(Connection conn) {
        super(conn.getUserData());
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
    public void addFile(MegaPDFile megaPDFile) {
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
    public void remove(MegaPDFile megaPDFile) {
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
