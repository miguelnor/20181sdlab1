package application;

import java.io.*;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main {

    static Properties prop;

    public static void main(String args[]) throws Exception
    {
        loadProperties();
        String serverPort = prop.getProperty("serverPort");
        System.out.println(serverPort);

        DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(serverPort));
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            ByteArrayInputStream bis = new ByteArrayInputStream(receivePacket.getData());
            ObjectInput in = new ObjectInputStream(bis);
            HashMap<BigInteger,String> o = (HashMap<BigInteger, String>) in.readObject();


            in.close();
            String sentence = o.get(BigInteger.ONE);

                    System.out.println("RECEIVED: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }

    private static void loadProperties(){
        prop = new Properties();
        InputStream input;
        try {
            input = new FileInputStream("application.properties");
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
