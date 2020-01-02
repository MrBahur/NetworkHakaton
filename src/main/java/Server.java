import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Server {


    public static void main(String[] args) {
        Server s = new Server();
        System.out.println(s.tryDeHash("aaaaa","zzzzz","7f5bb03cf507c861269be561971108be8f37d832"));
        System.out.println(s.tryDeHash("aaaaa","zzzzz","a346f3083515cbc8ca18aae24f331dee2d23454b"));
        System.out.println(s.tryDeHash("aaaaa","zzzzz","9017347a610d1436c1aaf52764e6578e8fc1a083"));
    }

    private String hash(String toHash, String nameOfHashAlgorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(nameOfHashAlgorithm);
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
        int start = convertStringToInt(startRange);
        int end = convertStringToInt(endRange);
        int length = startRange.length();
        for (int i = start; i <= end; i++) {
            String currentString = convertIntToString(i, length);
            String hash = hash(currentString, "SHA-1");
            if (originalHash.equals(hash)) {
                return currentString;
            }
        }
        return null;
    }

    private int convertStringToInt(String toConvert) {
        char[] charArray = toConvert.toCharArray();
        int num = 0;
        for (char c : charArray) {
            if (c < 'a' || c > 'z') {
                throw new RuntimeException();
            }
            num *= 26;
            num += c - 'a';
        }
        return num;
    }

    private String convertIntToString(int toConvert, int length) {
        StringBuilder s = new StringBuilder(length);
        while (toConvert > 0) {
            int c = toConvert % 26;
            s.insert(0, (char) (c + 'a'));
            toConvert /= 26;
            length--;
        }
        while (length > 0) {
            s.insert(0, 'a');
            length--;
        }
        return s.toString();
    }

}
