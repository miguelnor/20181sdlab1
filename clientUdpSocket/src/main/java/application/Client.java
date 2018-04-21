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

    static String port;
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    static DatagramSocket clientSocket;
    static DatagramPacket sendPackage;

    public static void main(String args[]) throws Exception {
        Scanner scan = new Scanner(System.in);
        logger.setLevel(Level.FINE);
        port = ApplicationProperties.getInstance().loadProperties().getProperty("server.port");
        InetAddress IPAddress = InetAddress.getByName("localhost");

        logger.info("Server address: " + IPAddress + ":" + port);

        //--Montando requisição---
        Client.menu();
        String code = Client.chooseOption();
        System.out.print("Key: ");
        BigInteger key = scan.nextBigInteger();
        String message = readMessage(code) ;
        String request = code + "'" + key + "'" + message;
        System.out.print(request);
        logger.info("Request: " + request);

        byte[] sendData = request.getBytes();

        clientSocket = new DatagramSocket();
        sendPackage = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(port));

        clientSocket.send(sendPackage);
        logger.info("Enviado: " + sendData);
        logger.info("Tamanho: " + request.length());

//        for (int i = 0; i < 4; i++) {
//            new ThreadResults("Thread " + i).start();
//        }
    }

    public static void menu() {
        Menu[] menu = Menu.values();
        System.out.println("----Choose an code option----");
        for (Menu m : menu) {
            System.out.printf("%d) %s%n", m.ordinal() + 1, m.name());
        }
        System.out.print("Option: ");
    }

    public static String chooseOption() {
        int code = 0;
        Scanner scan = new Scanner(System.in);
        code = scan.nextInt();
        while (code < 1 || code > 4) {
            System.out.println("----Choose an valid option----");
            Client.menu();
            code = scan.nextInt();
        }
        return Integer.toString(code);
    }

    public static String readMessage(String code){
        Scanner scan = new Scanner(System.in);
        String M;
        if(code.equals("1") || code.equals("3")){
            System.out.print("Message: ");
            M = scan.nextLine();
            return M;
        }
        return "";
    }


    public static BigInteger chooseKey() {
        Scanner scan = new Scanner(System.in);
        BigInteger code = scan.nextBigInteger();
        return code;
    }
}