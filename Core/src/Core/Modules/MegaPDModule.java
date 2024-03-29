package Core.Modules;

import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MegaPDModule {

    private String id;
    protected String remoteAdress;
    private int remotePort;

    private boolean hasException;
    private MegaPDRemoteException lastMegaPDRemoteException;

    protected Socket remoteSocket;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;

    public MegaPDModule(EntityData data){
        hasException = false;
        remoteSocket = data.getConnectionSocket();
        remoteAdress = data.address;
        out = data.out;
        in = data.in;
    }

    public String getId() {
        return id;
    }

    private boolean hasException() {
        return hasException;
    }

    public MegaPDRemoteException getException() {
        hasException = false;
        return lastMegaPDRemoteException;
    }

    public Enum getRequest() throws SocketTimeoutException, IOException, ClassNotFoundException {
        Enum enumRead = null;
        try {
            enumRead = (Enum) in.readObject();
            this.id = (String) in.readObject();
        }catch (SocketTimeoutException exception){ throw new SocketTimeoutException();}
        catch (IOException e) {
            newException("IOException");
            throw new IOException(e);
        } catch (ClassNotFoundException e) {
            newException("ClassNotFoundException");
            throw new ClassNotFoundException("",e);
        }
        return enumRead;
    }

    public void sendData() throws IOException {
        if(hasException){
            out.writeObject(getException());
        }else{
            out.writeObject(-200);
        }
    }

    public void sendData(Object data) throws IOException {
            if(hasException){
                out.writeObject(getException());
            }else{
                out.writeObject(data);
            }
    }



    public void newException(String message){
        hasException = true;
        lastMegaPDRemoteException = new MegaPDRemoteException("ServerSide: " + message);
    }
}
