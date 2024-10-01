package org.example;
import org.apache.commons.cli.*;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {
    private static Path FILE_PATH;
    private static int COUNT_PACKAGE = 10;
    private static String HOST = "vk.com";
    private static int BUFFER_SIZE = 1024;
    private static int SERVER_PORT = 443;

    public static void main(String[] args) throws Exception {
        Options options = listOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("size")) BUFFER_SIZE = Math.min(Integer.parseInt(cmd.getOptionValue("size")), 1024);
            if (cmd.hasOption("f")) FILE_PATH = Paths.get(cmd.getOptionValue("f"));
            if (cmd.hasOption("c")) COUNT_PACKAGE = Integer.parseInt(cmd.getOptionValue("c"));
            if (cmd.hasOption("p")) SERVER_PORT = Integer.parseInt(cmd.getOptionValue("p"));
            if (cmd.hasOption("h")) HOST = cmd.getOptionValue("h");
            if (cmd.hasOption("f"))
                sendPackageFromFileToServer();
        } catch (ParseException e) {
            message("Error: " + e);
        }
        sendPackageToServer();
    }

    public static void sendPackageFromFileToServer() {
        long start_time, end_time, time;
        int count = 0;
        try (
                FileInputStream fileInputStream = new FileInputStream(FILE_PATH.toString());
                DatagramSocket socket = new DatagramSocket();
        ) {
            InetAddress address = InetAddress.getByName(HOST);
            socket.setSoTimeout(3000);
            while (count < COUNT_PACKAGE) {
                byte[] sendData = new byte[BUFFER_SIZE];
                while (fileInputStream.read(sendData) != -1) {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, SERVER_PORT);
                    socket.send(sendPacket);
                    byte[] receiveData = new byte[BUFFER_SIZE];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    try {
                        start_time = System.currentTimeMillis();
                        socket.receive(receivePacket);
                        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        end_time = System.currentTimeMillis();
                        time = end_time - start_time;
                        System.out.println("Response from " + HOST + ", port: " + SERVER_PORT + ", response: " + response + ", time = " + time + " ms");
                    } catch (SocketTimeoutException e) {
                        System.out.println("Time out. No response received.");
                    }
                    count++;
                }
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPackageToServer() {
        long start_time, start_time2, end_time, end_time2, time, time2;
        int count = 0;
        try {
            InetAddress address = InetAddress.getByName(HOST);
            DatagramSocket socket = new DatagramSocket();
            long x = 3000;
            socket.setSoTimeout(3020);
            while (count < COUNT_PACKAGE) {
                byte[] sendData = "Ping".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, SERVER_PORT);
                socket.send(sendPacket);
                byte[] receiveData = new byte[BUFFER_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                start_time2 = System.currentTimeMillis();
                try {
                    start_time = System.currentTimeMillis();
                    socket.receive(receivePacket);
                    end_time = System.currentTimeMillis();
                    time = end_time - start_time;
                    System.out.println("Response from " + HOST + ", port: " + SERVER_PORT + ", time = " + time + " ms");
                } catch (SocketTimeoutException e) {
                    end_time2 = System.currentTimeMillis();
                    time2 = end_time2 - start_time2 - x;
                    System.out.println("Response from " + HOST + ", port: " + SERVER_PORT + ", time = " + time2 + " ms");
                }
                count++;
            }
            System.out.println(COUNT_PACKAGE + " packages sent, " + COUNT_PACKAGE + " packages received");
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Options listOptions() {
        Options options = new Options();
        options.addOption("size", "SizePackage", true, "Размер пакета, НЕ БОЛЕЕ 1024");
        options.addOption("p", "ServerPort", true, "Порт сервера");
        options.addOption("h", "ServerHost", true, "Хост сервера");
        options.addOption("f", "FilePath", true, "Путь до файла");
        options.addOption("c", "count", true, "Количество пакетов");
        return options;
    }

    private static void message(String m) {
        System.out.println(m);
    }
}

    /*
    private static final int AVERAGE_DELAY = 1000;
    private static String HOST = "localhost";
    private static int SERVER_PORT = 50001;
    private static int BUFFER_SIZE = 1024;

    public static void sendEmptyPackageToServer() {
        message("send 10 package to Server. Size package = " + BUFFER_SIZE + " byte");
        try (DatagramSocket clientSocket = new DatagramSocket();) {
            InetAddress IPAddress = InetAddress.getByName(HOST);
            byte[] sendingDataBuffer = new byte[BUFFER_SIZE];
            byte[] receivingDataBuffer = new byte[BUFFER_SIZE];
            sendingDataBuffer = "Hello from UDP client".getBytes();
            int i = 0;
            while (i < COUNT_PACKAGE) {
                Thread.sleep(AVERAGE_DELAY);
                if(i == 9) sendingDataBuffer = new byte[0];
                DatagramPacket sendingPacket = new DatagramPacket(
                        sendingDataBuffer, sendingDataBuffer.length, IPAddress, SERVER_PORT
                );
                clientSocket.send(sendingPacket);
                DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
                clientSocket.receive(receivingPacket);
                i++;
                message("Sent from the server: " + new String(receivingPacket.getData()));
            }
        } catch (Exception e) {
            message("Error: " + e);
        }
    }

    public static void sendFileToServer() {
        //FILE_PATH = Path.of("C:\\test.txt");
        message("Send data  from a file: " + FILE_PATH);
        try (
                FileInputStream fileInputStream = new FileInputStream(FILE_PATH.toString());
                DatagramSocket clientSocket = new DatagramSocket();
        ){
            InetAddress IPAddress = InetAddress.getByName(HOST);
            byte[] sendingDataBuffer = new byte[BUFFER_SIZE];
            byte[] receivingDataBuffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ( (bytesRead = fileInputStream.read(sendingDataBuffer)) != -1 ) {
                Thread.sleep(AVERAGE_DELAY);
                DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer, bytesRead, IPAddress, SERVER_PORT);
                clientSocket.send(sendingPacket);
                DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
                clientSocket.receive(receivingPacket);
                String receivedData = new String(receivingPacket.getData());
                message("Sent from the server: " + receivedData);
            }
            DatagramPacket lastPacket = new DatagramPacket(new byte[0], 0, IPAddress, SERVER_PORT);
            clientSocket.send(lastPacket);
            message("END sent file");
        }  catch (IOException | InterruptedException e) {
            message(e.getMessage());
        }
    }*/
