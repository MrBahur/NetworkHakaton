import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.io.*;
import java.net.*;

public class Server {
    private Utils utils;
    private String teamName;
    private DatagramSocket serverSocket;

    public Server() throws SocketException {
        utils = new Utils();
        teamName = "MatanAndOmer                    ";
        serverSocket = new DatagramSocket(3117);
    }

    public static void main(String[] args) throws IOException {
        Server s = new Server();
        s.listen();

    }

    private void listen() throws IOException {
        byte[] receiveData = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            System.out.println("message received: " + sentence);
            //move to different thread
            Message recieved = new Message(sentence);

            InetAddress address = receivePacket.getAddress();
            int port = receivePacket.getPort();
            sendOffer(address,port,recieved);

        }
    }

    private void sendOffer(InetAddress address, int port, Message received) throws IOException {
        Message toSend = new Message(teamName, Type.OFFER, received.getHashToCrack(), received.getLength(), received.getStartRange(), received.getEndRange());
        byte[] buffer = toSend.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        serverSocket.send(packet);
        System.out.println("message sent: " + toSend.toString());

    }

    private String hash(String toHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(toHash.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String tryDeHash(String startRange, String endRange, String originalHash) {
        int start = utils.convertStringToInt(startRange);
        int end = utils.convertStringToInt(endRange);
        int length = startRange.length();
        for (int i = start; i <= end; i++) {
            String currentString = utils.convertIntToString(i, length);
            String hash = hash(currentString);
            if (originalHash.equals(hash)) {
                return currentString;
            }
        }
        return null;
    }


}
