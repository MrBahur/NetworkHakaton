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

    public String getTeamName() {
        return teamName;
    }

    public Type getMessageType() {
        return messageType;
    }

    public String getHashToCrack() {
        return hashToCrack;
    }

    public int getLength() {
        return length;
    }

    public String getStartRange() {
        return startRange;
    }

    public String getEndRange() {
        return endRange;
    }

    public Message(String message) {
        this.teamName = message.substring(0, 32);
        this.messageType = Type.values()[Integer.parseInt(message.substring(32, 33))];
        this.hashToCrack = message.substring(33, 74);
        this.length = Integer.parseInt(message.substring(74, 75));
        this.startRange = message.substring(75, 76+length);
        this.endRange = message.substring(76+length, 77+2*length);
    }

    @Override
    public String toString() {
        return teamName + messageType.ordinal() + hashToCrack + length + startRange + endRange;
    }
}
