package Modules;

import Core.Context;
import Core.UserInfo;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileTransferManager implements Runnable{

    ServerSocket transferServerSocket;
    public static final int MAX_SIZE = 4000;

    public FileTransferManager() throws IOException {
        try {
            transferServerSocket = new ServerSocket(0);
            Context.getUser().setFileTransferServerSocket(transferServerSocket);
            Context.getConnection().registerFileTransferPort(transferServerSocket.getLocalPort());
        } catch (IOException | MegaPDRemoteException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    @Override
    public void run() {

        System.out.println("[FileTransfer] started");

        //TODO: better method besides DAEMON ...
        while (true){
            try {
            Socket requestSocket;


            requestSocket = transferServerSocket.accept();
            InputStream in = requestSocket.getInputStream();
            OutputStream out = requestSocket.getOutputStream();

            ObjectInputStream inObj = new ObjectInputStream(in);
            ObjectOutputStream outObj = new ObjectOutputStream(out);

            String fileName = (String) inObj.readObject();
            File file;
            try {
                file = Context.getFolderManager().getFile(fileName);
                outObj.writeObject(file.length());
            }catch (Exception e){
                //File does not exist
                outObj.writeObject(-1L);
                continue;
            }

            int requestID = (int) inObj.readObject();
            if(requestID == -1){continue;}

            Context.getNotificationManager().addNotification(fileName + " requested, upload began...");
            //start a new thread and send the file
           FileSender fSender = new FileSender(requestSocket, in, out, inObj, outObj, file, requestID);
            fSender.setDaemon(true);
            fSender.run();


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void getFile(String fileName, int userID){

        //Get user fileTransferPort and address
        //Connect to it

        UserInfo remoteUser = null;
        Socket remoteUserSocket = null;

        OutputStream out;
        InputStream in;
        ObjectOutputStream outObj = null;
        ObjectInputStream inObj;

        long fileSize = -1;

        try {
            remoteUser = Context.getConnection().getUser(userID);
            remoteUserSocket = new Socket(remoteUser.getAddress(), remoteUser.getFileTransferPort());

            out = remoteUserSocket.getOutputStream();
            in = remoteUserSocket.getInputStream();
            outObj = new ObjectOutputStream(out);
            inObj = new ObjectInputStream(in);

            outObj.writeObject(fileName);
            fileSize = (long) inObj.readObject();

        } catch (MegaPDRemoteException | IOException e) {
            Context.getNotificationManager().addNotification("Failed to establish connection with user");
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if(fileSize < 0){
            Context.getNotificationManager().addNotification("File does not exist please refresh");
        }


        int requestID = -1;
        try { requestID = Context.getFileManager().requestFile(fileName, userID); } catch (MegaPDRemoteException | IOException e) {
            e.printStackTrace();
        }

        try {
            outObj.writeObject(requestID);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if(requestID == -1){
            Context.getNotificationManager().addNotification(fileName + " request denied");
            return;
        }


        Context.getNotificationManager().addNotification("fileName" + " requested, download began...");

        FileReceiver fReceiver = new FileReceiver(remoteUserSocket, in, out, inObj, outObj, fileName, fileSize);
        fReceiver.setDaemon(true);
        fReceiver.run();
    }


    //------------------------------------------------------------------------------------------------------------------
    //  Thread to WRITE FILE
    //------------------------------------------------------------------------------------------------------------------
    private class FileReceiver extends Thread{

        Socket socket;
        InputStream in;
        OutputStream out;
        ObjectInputStream inObj;
        ObjectOutputStream outObj;
        String fileName;
        long fileSize;

        public FileReceiver(Socket socket, InputStream in, OutputStream out, ObjectInputStream inObj, ObjectOutputStream outObj,String fileName, long fileSize) {
            this.socket = socket;
            this.in = in;
            this.out = out;
            this.inObj = inObj;
            this.outObj = outObj;
            this.fileName = fileName;
            this.fileSize = fileSize;
        }


        @Override
        public void run() {
            try {

                byte []fileChunk = new byte[MAX_SIZE];
                int nBytes;
                int bytesRead = 0;
                FileOutputStream localFileOS;
                //Save the fileInTempFolder SO handles delete, and no JavaFolderNotification
                String tempPath = System.getProperty("java.io.tmpdir");
                //creates file from data received on temp
                localFileOS = new FileOutputStream(tempPath + "/" + fileName);

                //copies the file to folder
                while((nBytes = in.read(fileChunk)) > 0){
                    bytesRead += nBytes;
                    try {
                        //System.out.println("writing " + nBytes);
                        localFileOS.write(fileChunk, 0, nBytes);

                    } catch (IOException e) {
                        System.out.println("There was an error writing the file chunk! " + e);
                        socket.close();
                    }
                }

                if (fileSize != bytesRead){
                    File file = new File(tempPath + "/" + fileName);
                    file.delete();
                    System.out.println("File was corrupted, deleted { fileSize was ["+fileSize+"] and only read ["+bytesRead+"]}");
                    throw new FileNotFoundException("Failed to read all file");
                }
                File tempFile = new File(tempPath + "/" + fileName);
                File destFile = new File( Context.getFolderManager().getFilesFolderPath() + "/" + tempFile.toPath().getFileName());
                Files.copy(tempFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Context.getFolderManager().updateFile(destFile.toPath());

                //notifies the client
                Context.getNotificationManager().addNotification(fileName + "downloaded");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //  Thread to READ FILE
    //------------------------------------------------------------------------------------------------------------------
    private class FileSender extends Thread{

        Socket socket;
        InputStream in;
        OutputStream out;
        ObjectInputStream inObj;
        ObjectOutputStream outObj;
        File file;
        int requestID;


        public FileSender(Socket socket, InputStream in, OutputStream out, ObjectInputStream inObj, ObjectOutputStream outObj, File file, int requestID) {
            this.socket = socket;
            this.in = in;
            this.out = out;
            this.inObj = inObj;
            this.outObj = outObj;
            this.file = file;
            this.requestID = requestID;
        }

        @Override
        public void run() {

            try {
                FileInputStream requestedFileInputStream = new FileInputStream(file);
                byte []fileChunk = new byte[MAX_SIZE];
                int nBytes;

                //sends data
                while ((nBytes = requestedFileInputStream.read(fileChunk)) > 0) {
                    out.write(fileChunk, 0, nBytes);
                    out.flush();
                }

                requestedFileInputStream.close();
                socket.close();
                //updates server on the request
                Context.getFileManager().completeFileTransfer(requestID);
                //notifies download was a success
                Context.getNotificationManager().addNotification(file.getName() + "upload successfully");
            } catch (Exception e) {
                e.printStackTrace();
                Context.getNotificationManager().addNotification(file.getName() + "upload failed");
            }

        }
    }
}
