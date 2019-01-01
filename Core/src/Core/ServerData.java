package Core;


import Core.Modules.EntityData;

import java.io.IOException;
import java.net.Socket;

public class ServerData extends EntityData {

    public ServerData(String name, String address, int serverPort) throws IOException {
        super(name);
        this.port = serverPort;
        this.address = address;
        this.setConnectionSocket(new Socket(address, serverPort), true);
    }
}
