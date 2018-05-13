package application;

import java.io.IOException;

public class ProcessServerThread extends Thread {

    private final int port;

    public ProcessServerThread(int port){
        this.port = port;
    }

    @Override
    public void run(){
        try {
            ProcessServer server = new ProcessServer(this.port);
            server.start();
            server.blockUntilShutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

