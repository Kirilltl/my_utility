/*
package org.example;
import org.apache.commons.cli.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    private static final int AVERAGE_DELAY = 1000;
    public  static int SERVER_PORT = 50001;
    private static int BUFFER_SIZE = 1024;
    public  static int CLIENT_PORT = 0;

    public static void main(String[] args) throws Exception  {
        byte[] receivingDataBuffer = new byte[BUFFER_SIZE];
        CommandLineParser parser = new DefaultParser();
        Options options = listOptions();
        try {
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("p"))      SERVER_PORT = Integer.parseInt(cmd.getOptionValue("p"));
            if(cmd.hasOption("size"))   BUFFER_SIZE = Math.min(Integer.parseInt(cmd.getOptionValue("size")), 1024);
        }catch (ParseException e) {
            message("Error: " + e);
        }
        startServer(receivingDataBuffer);
        System.out.println(
                "File received successfully." +
                        "File: " + receivingDataBuffer.length +
                        "\n{ " + new String(receivingDataBuffer) + " }"
        );
    }

    private static void startServer(byte[] receivingDataBuffer) {
        StringBuilder stringBuilder = new StringBuilder();
        String sendingDataBuffer;
        try {
            message("Server start...");
            DatagramSocket socket = new DatagramSocket(SERVER_PORT);
            int i = 0;
            while (true) {
               Thread.sleep(AVERAGE_DELAY);
               DatagramPacket packet = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
               message("Waiting for a client to connect...");
               socket.receive(packet);
               InetAddress inetAddress = packet.getAddress();
               CLIENT_PORT = packet.getPort();
               String receivedData  = new String(packet.getData());
               message("Sent from the client: " + receivedData );
               sendingDataBuffer = ("Got package " + i);
               DatagramPacket outputPacket = new DatagramPacket(
                       sendingDataBuffer.getBytes(), sendingDataBuffer.length(), inetAddress, CLIENT_PORT
               );
               socket.send(outputPacket);
               i++;
                if(packet.getLength() == 0) {
                    message("Got the last package");
                    break;
                }else {
                    stringBuilder.append(receivedData);
                }
           }
            socket.close();
        }catch (Exception e) {
            message("Error: " + e);
        }
    }

    private static Options listOptions() {
        Options options = new Options();
        options.addOption("p", "ServerPort", true, "Порт сервера");
        options.addOption("size", "SizePackage", true, "Размер пакета, НЕ БОЛЕЕ 1024");
        return options;
    }

    private static void message(String m) {
        System.out.println(m);
    }
}
 */