package application;

import java.net.InetAddress;

public class Process {
    private String request;
    private InetAddress client;
    private int port;
    private boolean grpc;

    public Process(String request, InetAddress client, int port, boolean grpc){
        this.client = client;
        this.port = port;
        this.request = request;
        this.grpc = grpc;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public InetAddress getClient() {
        return client;
    }

    public void setClient(InetAddress client) {
        this.client = client;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isGrpc() {
        return grpc;
    }

    public void setGrpc(boolean grpc) {
        this.grpc = grpc;
    }
}
