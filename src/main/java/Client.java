
import javafx.util.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private Utils utils;
    private String input;
    private int length;
    private String teamName;
    private ArrayList<Pair<String, String>> ranges;
    private ArrayList<InetAddress> servers;
    private DatagramSocket clientSocket;


    public Client() {
        utils = new Utils();
        teamName = "MatanAndOmer____________________";
        ranges = new ArrayList<>();
        servers = new ArrayList<>();
        try {
            clientSocket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.getInput();
        try {
            client.sendDiscover();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.getRanges(6, 2);
        System.out.println("Done");

    }

    private void getInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to " + teamName + ". Please enter the hash:");
        input = sc.nextLine();
        System.out.println("Please enter the input string length:");
        length = sc.nextInt();
        System.out.println("please wait while we processing your request");
    }

    private void sendRequest() {

    }

    private void sendDiscover() throws IOException {
        Message m = new Message(teamName, Type.DISCOVER, input, length, getBoundary(length, 'a'), getBoundary(length, 'z'));
        InetAddress address = InetAddress.getByName("255.255.255.255");
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        byte[] buffer = m.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 3117);
        socket.send(packet);
        listen();
        socket.close();
    }

    private void listen() throws IOException {
        byte[] receiveData = new byte[1024];
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1000) {
            System.out.println(75);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            System.out.println(77);
            clientSocket.receive(receivePacket);
            System.out.println(79);
            servers.add(receivePacket.getAddress());
            String sentence = new String(receivePacket.getData());
            Message m = new Message(sentence);
            System.out.println(m.toString());
        }
    }

    private void getRanges(int length, int numOfServers) {
        String start = getBoundary(length, 'a');
        String end = getBoundary(length, 'z');
        BigInteger startRange = utils.convertStringToInt(start);
        BigInteger endRange = utils.convertStringToInt(end);
        BigInteger rangeSize = endRange.subtract(startRange);
        BigInteger interval = (rangeSize.divide(new BigInteger(Integer.toString(numOfServers))));
        startRange = new BigInteger("0");
        endRange = startRange.add(interval);
        for (int i = 0; i < numOfServers; i++) {
            String from = utils.convertIntToString(startRange, length);
            String until = utils.convertIntToString(endRange, length);
            Pair<String, String> current = new Pair<>(from, until);
            ranges.add(current);
            startRange = endRange.add(new BigInteger("1"));
            endRange = startRange.add(interval);
        }
    }

    private String getBoundary(int length, char c) {
        StringBuilder toReturn = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            toReturn.append(c);
        }
        return toReturn.toString();
    }

}
