package Modules;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSenderManager {

    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 10;

    private ServerSocket senderSocket;
    private File senderDirectory;

    public FileSenderManager(int port, File directory) throws IOException {
        senderSocket = null;
        senderSocket = new ServerSocket(port);
        senderDirectory = directory;
    }

    private void sendFile(String filename, int requestID) {
        //TODO: validate transfer through the ID
        OutputStream out;
        Socket toReceiver;
        byte []fileChunk = new byte[MAX_SIZE];
        int nBytes;

        String requestedFileName, requestedCanonicalFilePath = null;
        FileInputStream requestedFileInputStream = null;

        try {
            while (true) {
                try {
                    toReceiver = senderSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                try {
                    toReceiver.setSoTimeout(TIMEOUT * 1000);
                    out = toReceiver.getOutputStream();
                    requestedFileName = filename;
                    requestedCanonicalFilePath = new File(senderDirectory + File.separator + requestedFileName).getCanonicalPath();
                    if (!requestedCanonicalFilePath.startsWith(senderDirectory.getCanonicalPath() + File.separator)) {
                        System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                        System.out.println("A directoria de base nao corresponde a " + senderDirectory.getCanonicalPath() + "!");
                        return;
                    }
                    requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath);
                    while ((nBytes = requestedFileInputStream.read(fileChunk)) > 0) {
                        out.write(fileChunk, 0, nBytes);
                        out.flush();
                    }
                } catch (FileNotFoundException e) {   //Subclasse de IOException
                    System.out.println("Ocorreu a excepcao {" + e + "} ao tentar abrir o ficheiro " + requestedCanonicalFilePath + "!");
                } catch (IOException e) {
                    System.out.println("Ocorreu a excepcao de E/S: \n\t" + e);
                }
                if (requestedFileInputStream != null) {
                    try {
                        requestedFileInputStream.close();
                    } catch (IOException ex) {
                    }
                    try{
                        toReceiver.close();
                    } catch (IOException e){
                    }
                }
            }
        }finally {
            try{
                senderSocket.close();
            } catch (IOException e) {}
        }

    }
}
