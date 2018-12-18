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


    public  <E extends Enum<E>> Object remoteMethod(E requestCode, Object ...data) throws MegaPDRemoteException, IOException{

        try {
            //Send requestCode (so server knows what method is being requested)
            out.reset();
            out.writeObject(requestCode);
            out.writeObject(id);
            out.flush();

            //Send data
            for (Object toSend : data) {
                out.writeObject(toSend);
            }
        }catch (IOException e) {
            throw new IOException("Failed to properly connect to the server \n" + e);
        }

        //Process the server response
        return receiveData();
    }

    private Object receiveData() throws MegaPDRemoteException, IOException {
        try {
            Object o = in.readObject();
            //Checks if received a RemoteException
            if(o instanceof MegaPDRemoteException){
                throw (MegaPDRemoteException)o;
            }
            //Checks if received -200 (this means everything went ok)
            if(o instanceof Integer){
                if(((Integer) o).compareTo(-200) == 0){
                    return null;
                }
            }
            return o;
        } catch(Exception e){
            if(e instanceof MegaPDRemoteException){
                //It was indeed a remote Exception we received
                throw new MegaPDRemoteException(e.getMessage());
            }else {
                //Any other socket related exception
                e.printStackTrace();
                throw new IOException(e);
            }
        }
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
