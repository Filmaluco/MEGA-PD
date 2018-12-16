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

    public FileManager(EntityData data) {
        super(data);
    }

    @Override
    public void updateFiles(List<MegaPDFile> list) throws MegaPDRemoteException {
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
    public void addFile(MegaPDFile megaPDFile) throws MegaPDRemoteException {
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
    public void remove(MegaPDFile megaPDFile) throws MegaPDRemoteException {
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
    public void updateFile(String s, long l) throws MegaPDRemoteException {
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
    public List<MegaPDFile> getUserFiles(int i) throws MegaPDRemoteException {
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
    public int requestFile(String s, int i) throws MegaPDRemoteException {
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
    public void completeFileTransfer(int i) throws MegaPDRemoteException {
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
