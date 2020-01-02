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

    public static void main(String[] args) {
        Server s = new Server();
        try {
            s.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        String broadcastMessage = "Hello";
        InetAddress address = InetAddress.getByName("255.255.255.255");
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,3711);
        socket.send(packet);
        socket.close();

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
