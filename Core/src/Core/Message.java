package Core;

import java.util.Date;

public class Message {
    int ID;
    int fromID;
    int toID;
    boolean status;
    Date date;
    String message;

    /**
     * Sets message has seen
     * @param ID
     * @param fromID
     * @param toID
     * @param date
     * @param message
     */
    public Message(int ID, int fromID, int toID, Date date, String message) {
        this.ID = ID;
        this.fromID = fromID;
        this.toID = toID;
        this.status = true;
        this.date = date;
        this.message = message;
    }

    /**
     * Control over the status of the message [Seen or notSeen]
     * @param ID
     * @param fromID
     * @param toID
     * @param seen
     * @param date
     * @param message
     */
    public Message(int ID, int fromID, int toID, Date date, String message, boolean seen) {
        this.ID = ID;
        this.fromID = fromID;
        this.toID = toID;
        this.status = status;
        this.date = date;
        this.message = message;
    }

    public int getID() {
        return ID;
    }

    public int getFromID() {
        return fromID;
    }

    public int getToID() {
        return toID;
    }

    public boolean Seen() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
