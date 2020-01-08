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
        teamName = "WeAreTheRealTrolls______________";
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
            Message received;
            try {
                received = new Message(sentence);
            } catch (Exception e) {
                handleFakeTrolls(receivePacket.getAddress(), receivePacket.getPort());
                continue;
            }
            InetAddress address = receivePacket.getAddress();
            int port = receivePacket.getPort();
            if (received.getMessageType() == Type.DISCOVER) {
                sendOffer(address, port, received);
            }
            else if (received.getMessageType() == Type.REQUEST) {
                Thread t = new Thread(() -> sendAnswer(address, port, received));
                t.start();
            }
            else {
                System.out.println(received.toString());
            }
        }
    }

    private void handleFakeTrolls(InetAddress address, int port) {
        byte[] buffer = "We are the real Trolls, fix your shitty message it's broken".getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            serverSocket.send(packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Message sent: " + new String(packet.getData()));
    }

    private void sendAnswer(InetAddress address, int port, Message received) {
        String answer = tryDeHash(received.getStartRange(), received.getEndRange(), received.getHashToCrack());
        if (answer != null) {
            Message toSend = new Message(teamName, Type.ACK, received.getHashToCrack(), received.getLength(), answer, received.getEndRange());
            send(toSend, address, port);
        }
        else {
            Message toSend = new Message(teamName, Type.NACK, received.getHashToCrack(), received.getLength(), received.getStartRange(), received.getEndRange());
            send(toSend, address, port);
        }
    }

    private void sendOffer(InetAddress address, int port, Message received) {
        Message toSend = new Message(teamName, Type.OFFER, received.getHashToCrack(), received.getLength(), received.getStartRange(), received.getEndRange());
        send(toSend, address, port);
    }

    private void send(Message m, InetAddress address, int port) {
        byte[] buffer = m.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            Thread.sleep(1000);
            serverSocket.send(packet);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("message sent: " + m.toString());
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
        BigInteger start = utils.convertStringToInt(startRange);
        BigInteger end = utils.convertStringToInt(endRange);
        int length = startRange.length();
        for (BigInteger i = start; i.compareTo(end) <= 0; i = i.add(new BigInteger("1"))) {
            String currentString = utils.convertIntToString(i, length);
            String hash = hash(currentString);
            if (originalHash.equals(hash)) {
                return currentString;
            }
        }
        return null;
    }

}
