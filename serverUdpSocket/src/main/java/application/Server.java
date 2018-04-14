package application;

import application.configuration.ApplicationProperties;

import java.util.logging.Logger;
import java.net.*;

public class Server {

    static String port;
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    static DatagramSocket serverSocket;
    static byte[] receivedData = new byte[1480];
    static byte[] sendData = new byte[1480];


    public static void main(String args[]) {
        port = ApplicationProperties.getInstance().loadProperties().getProperty("site.port");
        logger.info("Porta: " + port);
        try {
            serverSocket = new DatagramSocket(Integer.parseInt(port));
            while(true) {
                DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
                serverSocket.receive(receivedPacket);
                String data = new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength());
                logger.info("Recebido: " + data);
                logger.info("Tamanho: " + data.length());
                InetAddress IPReceived = receivedPacket.getAddress();
                int portReceived = receivedPacket.getPort();
                logger.info("De: " + IPReceived + ":" + portReceived);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
