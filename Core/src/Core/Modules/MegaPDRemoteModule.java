package Core.Modules;

import MegaPD.Core.Exeptions.MegaPDRemoteException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class MegaPDRemoteModule {

    private String id;
    private String remoteAdress;
    private int remotePort;

    protected Socket remoteSocket;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;

    public MegaPDRemoteModule(EntityData remoteData){
        id = remoteData.id;
        remoteSocket = remoteData.getSocket();
        out = remoteData.out;
        in = remoteData.in;
    }

    public <E extends Enum<E>> boolean request(E requestCode) {
        try {
            out.reset();
            out.writeObject(requestCode);
            out.writeObject(id);
            out.flush();
        } catch (IOException e) {
            //Todo throw connection exception
            return false;
        }
            return true;
    }

    public boolean sendData(Object ... data){
        try {
            for (Object toSend : data) {
                out.writeObject(toSend);
            }
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public Object receiveData() throws MegaPDRemoteException {
        try {
            Object o = in.readObject();
            if(o instanceof MegaPDRemoteException){
                throw (MegaPDRemoteException)o;
            }
            if(o instanceof Integer){
                if(((Integer) o).compareTo(-1) == 0){
                    return null;
                }
            }
            return o;
        } catch(Exception e){
            if(e instanceof MegaPDRemoteException){
                throw new MegaPDRemoteException(e.getMessage());
            }else {
                e.printStackTrace();
                System.out.println(e);
            }
        }
        return null;
    }

    public void close(){
        try {
            if(this.remoteSocket != null){
                this.in.close();
                this.out.close();
                this.remoteSocket.close();
            }
        } catch (IOException ignored) { }
    }

}
