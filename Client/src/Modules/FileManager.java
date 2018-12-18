package Modules;

import Core.Log;
import Core.MegaPDFile;
import Core.MegaPDHistory;
import Core.Modules.EntityData;
import Core.Modules.MegaPDRemoteModule;
import Core.Modules.ModuleInterface;
import Core.UserInfo;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager extends MegaPDRemoteModule implements ModuleInterface.FileManagerModule {

    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 10;
    public static final String tempPath = System.getProperty("java.io.tmpdir");

    private Map<Integer, String> requestMap = null;

    public FileManager(EntityData remoteData) {
        super(remoteData);
        requestMap = new HashMap<>();
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

        //int ret = new Integer((Integer) this.remoteMethod(FileManagerRequest.requestFile, s, i));

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

    public void getFile(int rID, String destPath, UserInfo info, MegaPDFile transferFile) throws IOException {
        byte []fileChunk = new byte[MAX_SIZE];
        int nBytes;
        int bytesRead = 0;
        FileOutputStream localFileOS = null;
        PrintWriter pout;
        String filename = null;

        for (int i: requestMap.keySet()) {
            if(i == rID)
                if(requestMap.containsKey(i)) {
                    filename = requestMap.get(i);
                    break;
                }
                else
                    Log.w("That request was already processed!");
        }

        if (filename == null)
            return;
        
        try {
            localFileOS = new FileOutputStream(tempPath + "/" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Socket socketToSender = null;

        try {
            socketToSender = new Socket(info.getAddress(), info.getFileTransferPort());
        } catch (IOException e) {
            System.out.println("Error creating socket!! " + e);
        }
        try {
            socketToSender.setSoTimeout(TIMEOUT * 1000);
        } catch (SocketException e) {
            System.out.println("TCP Error!! " + e);
        }

        InputStream in = null;

        try {
            in = socketToSender.getInputStream();
        } catch (IOException e) {
            Log.w("Socket is closed or not connected!! " + e);
        }

        try {
            pout = new PrintWriter(socketToSender.getOutputStream(), true);
            pout.flush();
        } catch (IOException e) {
            Log.w("Unable to output the data from the socket!! " + e);
            //e.printStackTrace();
        }


        while((nBytes = in.read(fileChunk)) > 0){
            bytesRead += nBytes;
            try {
                localFileOS.write(fileChunk, 0, nBytes);

            } catch (IOException e) {
                Log.w("There was an error writing the file chunk! " + e);
            }
        }

        System.out.println(bytesRead);
        System.out.println(transferFile.getFileSize());

        if (transferFile.getFileSize() != bytesRead){
            File file = new File(tempPath + "/" + filename);
            file.delete();
        }
        else{
            moveFromTemp(filename, destPath);
            requestMap.remove(rID);
            try {
                completeFileTransfer(rID);
            } catch (MegaPDRemoteException e) {
                Log.w("Could not inform server of the completed transfer! " + e);
            }
        }

        socketToSender.close();
    }

    /**
     * This method moves the the transferred file from the temp folder to the user folder.
     * @param f Named of the transferred file
     * @param destPath Folder into which the file will be placed
     */
    private void moveFromTemp(String f, String destPath) {
        File file = new File(tempPath + "/" + f);

        if (file.renameTo(new File(destPath + "/" + f))){
            file.delete();
        }
    }

}
