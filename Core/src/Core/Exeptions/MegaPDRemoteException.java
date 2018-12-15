package MegaPD.Core.Exeptions;

public class MegaPDRemoteException extends Exception {
    public MegaPDRemoteException(){ }
    public MegaPDRemoteException(String message){
        super(message);
    }
}
