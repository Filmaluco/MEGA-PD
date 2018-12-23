package Modules;

import Core.Context;
import Core.UserInfo;
import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferManager implements Runnable{

    ServerSocket transferServerSocket;

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
                outObj.writeObject(-1L);
            }catch (Exception e){
                //File does not exist
                outObj.writeObject(-1L);
            }

            //start a new thread and send the file

            Context.getNotificationManager().addNotification(fileName + " requested, upload began...");

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
        ObjectOutputStream outObj;
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
        }

        if(fileSize < 0){
            Context.getNotificationManager().addNotification("File does not exist please refresh");
        }

        /*
        try { Context.getFileManager().requestFile(fileName, userID); } catch (MegaPDRemoteException | IOException e) {
            e.printStackTrace();
        }
        */

        Context.getNotificationManager().addNotification("fileName" + " requested, download began...");


        //start new thread to write the file
        //notifies client
    }


    //------------------------------------------------------------------------------------------------------------------
    //  Thread to WRITE FILE
    //------------------------------------------------------------------------------------------------------------------
    private class FileReceiver implements Runnable{

        @Override
        public void run() {
            //creates file from data received
            //any exception and updates the server and deletes the files
            //notifies the client
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //  Thread to READ FILE
    //------------------------------------------------------------------------------------------------------------------
    private class FileSender implements Runnable{

        @Override
        public void run() {
            //sends data
            //updates server on the request
        }
    }
}
