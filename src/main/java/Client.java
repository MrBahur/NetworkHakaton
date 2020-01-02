
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    Utils utils;
    String input;
    int length;
    String teamName;
    ArrayList<Pair<String, String>> ranges;


    public Client() {
        utils = new Utils();
        teamName = "MatanAndOmer";
        ranges = new ArrayList<>();
    }

    public static void main(String[] args) {
        Client client = new Client();
        //client.getInput();
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


    private void getRanges(int length, int numOfServers) {
        String start ="";
        String end = "";
        for (int i=0; i<length; i++) {
            start += 'a';
            end += 'z';
        }
        int startRange = utils.convertStringToInt(start);
        int endRange = utils.convertStringToInt(end);
        int rangeSize = endRange - startRange;
        int interval = (rangeSize/numOfServers);
        startRange = 0;
        endRange = startRange + interval;
        for (int i=0; i<numOfServers; i++) {
            String from = utils.convertIntToString(startRange, length);
            String until = utils.convertIntToString(endRange, length);
            Pair<String, String> current = new Pair<>(from, until);
            ranges.add(current);
            startRange = endRange + 1;
            endRange = startRange + interval;
        }
    }

}
