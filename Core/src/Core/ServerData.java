package MegaPD.Core;

import MegaPD.Core.Modules.EntityData;

import java.io.IOException;
import java.net.Socket;

public class ServerData extends EntityData {

    public ServerData(String serverAddress, int serverPort) throws IOException {
        super();
        this.address = serverAddress;
        this.port = serverPort;
        this.setSocket(new Socket(serverAddress, serverPort), true);
    }
}
