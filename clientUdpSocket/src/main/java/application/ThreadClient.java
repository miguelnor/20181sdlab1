package application;

import java.io.IOException;
import java.net.DatagramPacket;

public class ThreadClient extends Thread {
    byte[] receiveData = new byte[1400];

    @Override
    public void run() {
        System.out.println("Escutando");
        while(true) {
            DatagramPacket receivedPacket = new DatagramPacket(receiveData,receiveData.length);
            try {
                System.out.println("Escutando");
                Client.clientSocket.receive(receivedPacket);
                System.out.println("Escutando");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(receivedPacket.getData() != null) {
                String response = new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength());
                System.out.println("Response: " + response);
                break;
            }
        }
    }
}