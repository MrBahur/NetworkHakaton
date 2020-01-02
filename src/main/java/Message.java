public class Message {
    private String teamName;
    private Type messageType;
    private String hashToCrack;
    private int length;
    private String startRange;
    private String endRange;

    public Message(String teamName, Type messageType, String hashToCrack, int length, String startRange, String endRange) {
        this.teamName = teamName;
        this.messageType = messageType;
        this.hashToCrack = hashToCrack;
        this.length = length;
        this.startRange = startRange;
        this.endRange = endRange;
    }

    @Override
    public String toString() {
        return teamName + messageType.ordinal() + hashToCrack + length + startRange + endRange;
    }
}
