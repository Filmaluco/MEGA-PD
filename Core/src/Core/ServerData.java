package Core;


import Core.Modules.EntityData;

import java.io.IOException;
import java.net.Socket;

public class ServerData extends EntityData {

    public ServerData(String name, int serverPort) throws IOException {
        super(name);
        this.port = serverPort;
        this.setSocket(new Socket(address, serverPort), true);
    }
}
