package application;

import application.configuration.ApplicationProperties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

public class Client {

    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void menu() {
        Menu[] menu = Menu.values();
        System.out.println("----Choose an code option----");
        for(Menu m: menu) {
            System.out.printf("%d) %s%n", m.ordinal() + 1, m);
        }
    }

    public static String chooseOption() {
        int code = 0;
        Scanner scan = new Scanner(System.in);
        while (code < 1 || code > 4) {
            Client.menu();
            code = scan.nextInt();
        }
        return Integer.toString(code);
    }

    public static BigInteger chooseKey() {
        Scanner scan = new Scanner(System.in);
        BigInteger code = scan.nextBigInteger();
        return code;
    }

    public static String verifyKeyOfCode(String code) {
        if (code.equals(2) || code.equals(4)) {
            return " ";
        } else {
            String key = Client.chooseKey().toString();
            return key;
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner scan = new Scanner(System.in);
        logger.setLevel(Level.FINE);
        int port = Integer.parseInt(ApplicationProperties.getInstance().loadProperties().getProperty("server.port"));
        InetAddress IPAddress = InetAddress.getByName("localhost");

        logger.info("Server address: " + IPAddress + ":" + port);

        //--Montando requisição---
        Client.menu();
        String code = Client.chooseOption();
        String key = Client.verifyKeyOfCode(code);
        String message = scan.nextLine();
        String request = code+"'"+key+"'"+message;


        logger.info("Request: " + request);

        byte[] sendData = request.getBytes();

        DatagramSocket clientSocket = new DatagramSocket();
        DatagramPacket sendPackage = new DatagramPacket(sendData, sendData.length, IPAddress, port);

        clientSocket.send(sendPackage);
        logger.info("Enviado: " + sendData);
        logger.info("Tamanho: " + request.length());

//        for (int i = 0; i < 4; i++) {
//            new ThreadResults("Thread " + i).start();
//        }
    }

}
