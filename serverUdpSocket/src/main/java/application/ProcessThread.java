package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ProcessThread extends Thread {

    final String CREATE = "1",
            READ = "2",
            UPDATE = "3",
            DELETE = "4";

    private String logFile;
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Map<BigInteger, String> mapa = new HashMap<>();

    public ProcessThread(String logFile) {
        this.logFile = logFile;
        logger.info("logFile: " + this.logFile);
    }

    @Override
    public void run() {
        processLogFile();
        logger.info("Iniciando atendimento da fila...");
        while (true) {
            Process process;
            synchronized (Server.processQueue) {
                process = Server.processQueue.poll();
            }
            if (process != null) {
                logger.info("processando requisicao da fila...");
                String[] splited = process.getRequest().split("'");
                String CRUD = splited[0];
                BigInteger key = new BigInteger(splited[1]);
                String value = splited[2];
                if (!CRUD.equals(READ))
                    processCUD(CRUD, key, value);
                else {
                    try {
                        logger.info("processando READ " + key);
                        processReply(key.toString() + " : " + mapRead(key), process);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void processReply(String data, Process process) throws Exception {
        byte[] sendData = data.getBytes();

        DatagramSocket clientSocket = new DatagramSocket();
        DatagramPacket sendPackage = new DatagramPacket(sendData, sendData.length, process.getClient(), Integer.parseInt(Server.portClient));
        logger.info("enviando resposta para o cliente " + process.getClient() +":" + process.getPort() + " " + data);
        clientSocket.send(sendPackage);
    }

    private boolean processCUD(String crud, BigInteger key, String value) {
        logger.info("processando CUD " + crud + key + value);
        switch (crud) {
            case CREATE:
                return mapCreate(key, value);
            case UPDATE:
                return mapUpdate(key, value);
            case DELETE:
                return mapDelete(key);
            default:
                return false;
        }
    }

    private boolean mapDelete(BigInteger key) {
        mapa.remove(key);
        return true;
    }

    private boolean mapUpdate(BigInteger key, String value) {
        if (!mapa.containsKey(key))
            return false;
        mapa.replace(key, value);
        return true;
    }

    private String mapRead(BigInteger key) {
        return mapa.get(key);
    }

    private boolean mapCreate(BigInteger key, String value) {
        mapa.put(key, value);
        return true;
    }

    private void processLogFile() {
        logger.info("processando logFile na inicializacao");
        String command;
        try {
            FileReader fileReader = new FileReader(logFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((command = bufferedReader.readLine()) != null) {
                String[] splited = command.split("'");
                String CRUD = splited[0];
                BigInteger key = new BigInteger(splited[1]);
                String value = splited[2];
                if (!CRUD.equals(READ))
                    processCUD(CRUD, key, value);
            }
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            logFile + "'");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
