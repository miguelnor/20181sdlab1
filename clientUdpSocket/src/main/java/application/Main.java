package application;

import application.configuration.ApplicationProperties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

public class Main {

    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String args[]) throws Exception {

        logger.setLevel(Level.FINE);

        int port = Integer.parseInt(ApplicationProperties.getInstance().loadProperties().getProperty("server.port"));
        InetAddress IPAddress = InetAddress.getByName("localhost");

        logger.info("Server address: " + IPAddress + ":" + port);

        String teste = "0 55 miguel";

        byte[] sendData = new byte[1480];

        sendData = teste.getBytes();

        DatagramSocket clientSocket = new DatagramSocket();

        DatagramPacket sendPackage = new DatagramPacket(sendData, sendData.length, IPAddress, port);

        clientSocket.send(sendPackage);
        logger.info("Enviado: " + teste);
        logger.info("Tamanho: " + teste.length());

        for (int i = 0; i < 4; i++) {
            new ThreadResults("Thread " + i).start();
        }
    }
}
