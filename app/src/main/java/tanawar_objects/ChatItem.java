package tanawar_objects;

import java.util.Calendar;

public class ChatItem {
    private String message;
    private String senderEmail;
    private String recipientEmail;
    private Calendar time;
    private String senderName;
    private String recipientName;
    public ChatItem(String message, String senderEmail, String recipientEmail,
                    Calendar time) {
        this.message = message;
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.time = time;
    }

    public ChatItem(String message, String senderEmail, String recipientEmail, Calendar time,
                    String senderName, String recipientName) {
        this.message = message;
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.time = time;
        this.senderName = senderName;
        this.recipientName = recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
}
