import java.math.BigInteger;

public class Utils {

    public BigInteger convertStringToInt(String toConvert) {
        char[] charArray = toConvert.toCharArray();
        BigInteger num = new BigInteger("0");
        for (char c : charArray) {
            if (c < 'a' || c > 'z') {
                throw new RuntimeException();
            }
            num = num.multiply(new BigInteger("26"));
            int x = c - 'a';
            num = num.add(new BigInteger(Integer.toString(x)));
        }
        return num;
    }

    public String convertIntToString(BigInteger toConvert, int length) {
        StringBuilder s = new StringBuilder(length);

        while (toConvert.compareTo(new BigInteger("0")) > 0) {
            BigInteger c = toConvert.mod(new BigInteger("26"));
            s.insert(0, (char) (c.intValue() + 'a'));
            toConvert = toConvert.divide(new BigInteger("26"));
            length--;
        }
        while (length > 0) {
            s.insert(0, 'a');
            length--;
        }
        return s.toString();
    }

}
