package Task7.Model;

class Connections {
    private int connectedTo;
    private int numMessages;
    private int numLikes;
    private int numComments;

    public Connections(int connectedTo, int numMessages, int numLikes, int numComments) {
        this.connectedTo = connectedTo;
        this.numMessages = numMessages;
        this.numLikes = numLikes;
        this.numComments = numComments;
    }

    public int getConnectedTo() {
        return connectedTo;
    }

    public int getNumMessages() {
        return numMessages;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public int getNumComments() {
        return numComments;
    }
}