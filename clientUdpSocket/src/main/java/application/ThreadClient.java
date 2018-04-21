package application;

import java.io.IOException;
import java.net.DatagramPacket;

public class ThreadClient extends Thread {
    byte[] receiveData = new byte[1400];

    @Override
    public void run() {
        while(true) {
            DatagramPacket receivedPacket = new DatagramPacket(receiveData,receiveData.length);
            try {
                Client.clientSocket.receive(receivedPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(receivedPacket.getData() != null) {
                String response = new String(receivedPacket.getData());
                System.out.println("Response: " + response);
                break;
            }
        }
    }
}