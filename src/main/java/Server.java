import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Server {


    public static void main(String[] args) {
        Server s = new Server();
        System.out.println(s.hash("shade", "SHA-1"));
        System.out.println(s.hash("viper", "SHA-1"));
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
        
        return null;
    }

}
