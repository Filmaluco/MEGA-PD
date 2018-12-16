package Modules;

import Core.Log;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class FileReceiverManager {

    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 10;
    public static int requestID = 1;
    public static final String tempPath = System.getProperty("java.io.tmpdir");

    private String fileName;
    private long fileSize;
    private String senderIP;
    private int senderPort;
    private Map<Integer, String> requestMap = null;

    public FileReceiverManager(String file, long size, String IP, int port){
        fileName = file;
        fileSize = size;
        senderIP = IP;
        senderPort = port;
        requestMap = new HashMap<Integer, String>();
    }


    /**
     * This method sends a request of a file from another client to the server, returns a request ID if successful
     * @param fileName the name of the requested file
     * @param clientIP the client's IP in which the file is in
     * @return a valid request ID
     */
    public int requestFile(String fileName, int clientIP){
        //TODO: Request file from server
        if() {
            requestMap.put(requestID++, fileName);
            return requestID;
        }
        else
            return 0;
    }

    public void getFile(int rID, String destPath) throws IOException {
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
            socketToSender = new Socket(senderIP, senderPort);
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
            System.out.println("Socket is closed or not connected!! " + e);
        }

        pout = new PrintWriter(socketToSender.getOutputStream(), true);
        pout.flush();

        System.out.println("gonna print");

        while((nBytes = in.read(fileChunk)) > 0){
            System.out.println("reading");
            bytesRead += nBytes;
            try {
                System.out.println("writing " + nBytes);
                localFileOS.write(fileChunk, 0, nBytes);

            } catch (IOException e) {
                System.out.println("There was an error writing the file chunk! " + e);
            }
        }

        System.out.println("stopped reading");

        System.out.println(bytesRead);
        System.out.println(fileSize);

        if (fileSize != bytesRead){
            File file = new File(tempPath + "/" + filename);
            file.delete();
        }
        else{
            System.out.println("here");
            moveFromTemp(filename, destPath);
            requestMap.remove(rID);
        }


    }

    /**
     * This method moves the the transferred file from the temp folder to the user folder.
     * @param f Named of the transferred file
     * @param destPath Folder into which the file will be placed
     */
    private void moveFromTemp(String f, String destPath) {
        File file = new File(tempPath + "/" + f);

        if (file.renameTo(new File(destPath + "/" + fileName))){
            file.delete();
        }
    }

}
