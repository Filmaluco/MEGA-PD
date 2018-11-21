package Modules;

import PD.Core.FileHistory;
import PD.Core.MegaFile;
import PD.Core.Module;

import java.util.List;

public class FileTransfer implements  Runnable, Module.FileTransfer {
    @Override
    public List<MegaFile> getMyFiles() {
        return null;
    }

    @Override
    public void addFile(MegaFile megaFile) {

    }

    @Override
    public void removeFile(MegaFile megaFile) {

    }

    @Override
    public List<FileHistory> getFileHistory() {
        return null;
    }

    @Override
    public int requestFile(int i) {
        return 0;
    }

    @Override
    public MegaFile getFileInfo(int i) {
        return null;
    }

    @Override
    public void startFileTransfer(int i) {

    }

    @Override
    public void finishFileTransfer(int i) {

    }

    @Override
    public void run() {

    }
}
