import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.io.*;
import java.net.*;

public class Server {
    private Utils utils;

    public Server() {
        utils = new Utils();
    }

    public static void main(String[] args) throws IOException {
        Server s = new Server();
        s.listen();

    }

    private void listen() throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(3117);
        byte[] receiveData = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            System.out.println(sentence);
        }
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
