
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
            clientSocket = new DatagramSocket(4567);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.getInput();
        try {
            client.sendDiscover();
            client.listenToOffers();
            client.sendAllRequests();
            client.listenToAcks();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.clientSocket.close();
        System.out.println("Done");
    }


    private void getInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to " + teamName + ". Please enter the hash:");
        input = sc.nextLine();
        System.out.println("Please enter the input string length:");
        length = sc.nextInt();
        System.out.println("please wait while we processing your request");
        System.out.println("");
    }

    private void sendAllRequests() {
        getRanges(length, servers.size());
        for (int i = 0; i < servers.size(); i++) {
            sendRequest(servers.get(i), ranges.get(i));
        }
    }

    private void sendRequest(InetAddress address, Pair<String, String> range)   {
        Message m = new Message(teamName, Type.REQUEST, input, length, range.getKey(), range.getValue());
        byte[] buffer = m.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 3117);
        try {
            clientSocket.send(packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendDiscover() throws IOException {
        Message m = new Message(teamName, Type.DISCOVER, input, length, getBoundary(length, 'a'), getBoundary(length, 'z'));
        InetAddress address = InetAddress.getByName("255.255.255.255");
        DatagramSocket socket = clientSocket;
        socket.setBroadcast(true);
        byte[] buffer = m.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 3117);
        socket.send(packet);
        socket.setBroadcast(false);

    }

    private void listenToOffers() throws IOException {
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

    private void listenToAcks() throws IOException {
        int nackCounter = 0;
        byte[] receiveData = new byte[1024];
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 60000) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            Message m = new Message(new String(receivePacket.getData()));
            if (m.getMessageType() == Type.ACK) {
                System.out.println("Your answer is: " + m.getStartRange());
                return;
            }
            else if (m.getMessageType() == Type.NACK) {
                nackCounter++;
                if (nackCounter == servers.size()) {
                    System.out.println("Sorry we could not find an answer for you");
                    return;
                }
            }
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
