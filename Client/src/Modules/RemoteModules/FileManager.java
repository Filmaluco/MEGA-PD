package Modules.RemoteModules;

import Core.MegaPDFile;
import Core.MegaPDHistory;
import Core.Modules.EntityData;
import Core.Modules.MegaPDRemoteModule;
import Core.Modules.ModuleInterface;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileManager extends MegaPDRemoteModule implements ModuleInterface.FileManagerModule {

    private Map<Integer, String> requestMap = null;

    public FileManager(EntityData remoteData) {
        super(remoteData);
    }


    @Override
    public void updateFiles(List<MegaPDFile> list) throws MegaPDRemoteException, IOException {
        this.remoteMethod(FileManagerRequest.updateFiles, list);
    }

    @Override
    public void addFile(MegaPDFile megaPDFile) throws MegaPDRemoteException, IOException {
        this.remoteMethod(FileManagerRequest.addFile, megaPDFile);
    }

    @Override
    public void remove(MegaPDFile megaPDFile) throws MegaPDRemoteException, IOException {
        this.remoteMethod(FileManagerRequest.remove, megaPDFile);
    }

    @Override
    public void updateFile(String s, long l) throws MegaPDRemoteException, IOException {
        this.remoteMethod(FileManagerRequest.updateFile, s, l);
    }

    @Override
    public List<MegaPDFile> getUserFiles(int i) throws MegaPDRemoteException, IOException {
        return (List<MegaPDFile>) this.remoteMethod(FileManagerRequest.getUserFiles, i);
    }

    @Override
    public int requestFile(String s, int i) throws MegaPDRemoteException, IOException {
        return (Integer) this.remoteMethod(FileManagerRequest.requestFile, s, i);
    }

    @Override
    public void completeFileTransfer(int i) throws MegaPDRemoteException, IOException {
        this.remoteMethod(FileManagerRequest.completeFileTransfer, i);
    }

    @Override
    public List<MegaPDHistory> getFileHistory() throws MegaPDRemoteException, IOException {
        return (List<MegaPDHistory>) this.remoteMethod(FileManagerRequest.getFileHistory);
    }

}
