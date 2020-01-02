import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Server {
    private Utils utils;

    public Server() {
        utils = new Utils();
    }

    public static void main(String[] args) {
        Server s = new Server();
        System.out.println(s.tryDeHash("aaaaa", "zzzzz", "7f5bb03cf507c861269be561971108be8f37d832"));
        System.out.println(s.tryDeHash("aaaaa", "zzzzz", "a346f3083515cbc8ca18aae24f331dee2d23454b"));
        System.out.println(s.tryDeHash("aaaaa", "zzzzz", "9017347a610d1436c1aaf52764e6578e8fc1a083"));
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
