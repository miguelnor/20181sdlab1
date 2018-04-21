package application;

import application.configuration.ApplicationProperties;

import java.io.FileWriter;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.net.*;

public class Server {

    static String port;
    static String portClient;
    static String logFile;
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    static DatagramSocket serverSocket;
    static FileWriter fileWriter;
    static byte[] receivedData = new byte[1480];
    static LinkedList<Process> processQueue = new LinkedList<>();


    public static void main(String args[]) {
        port = ApplicationProperties.getInstance().loadProperties().getProperty("site.port");
        portClient =  ApplicationProperties.getInstance().loadProperties().getProperty("client.port");
        logFile = ApplicationProperties.getInstance().loadProperties().getProperty("log.file");
        logger.info("Porta: " + port);
        new ProcessThread(logFile).start();
        logger.info("Passou da thread");
        try {
            //BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            serverSocket = new DatagramSocket(Integer.parseInt(port));
            while(true) {
                logger.info("Esperando recebimento...");
                DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
                serverSocket.receive(receivedPacket);
                String data = new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength());
                logger.info("Recebido: " + data);
                logger.info("Tamanho: " + data.length());
                InetAddress IPReceived = receivedPacket.getAddress();
                int portReceived = receivedPacket.getPort();
                logger.info("De: " + IPReceived + ":" + portReceived);
                synchronized (processQueue) {
                    processQueue.add(new Process(data, IPReceived, portReceived));
                }
                logger.info("Requisicao inserida na fila...");
                if(data.charAt(0) != '2')
                    writeOnFile(logFile,data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void writeOnFile(String file, String data){
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(data+System.getProperty( "line.separator"));
            fileWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
