package Modules;

import Core.Context;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferManager implements Runnable{

    ServerSocket transferServerSocket;

    public FileTransferManager(ServerSocket transferServerSocket) {
        this.transferServerSocket = transferServerSocket;
    }

    @Override
    public void run() {

        //Receiver cycle

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


            //check if file exists
            //send file size! if -1 then file doest exist and its considered a fail
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

        //request the server
        //request the client (file name)
        //receive file size
        //validates file size
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
