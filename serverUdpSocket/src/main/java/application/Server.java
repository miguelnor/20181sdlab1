package application;

import application.configuration.ApplicationProperties;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.net.*;

public class Server {

    static String port;
    static String logFile;
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    static DatagramSocket serverSocket;
    static byte[] receivedData = new byte[1480];
    static LinkedList<Process> processQueue = new LinkedList<>();


    public static void main(String args[]) {
        port = ApplicationProperties.getInstance().loadProperties().getProperty("site.port");
        logFile = ApplicationProperties.getInstance().loadProperties().getProperty("log.file");
        logger.info("Porta: " + port);
        new ProcessThread(logFile).run();
        try {
            FileWriter fileWriter = new FileWriter(logFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
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
                processQueue.add(new Process(data,IPReceived,portReceived));
                bufferedWriter.write(data);
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
