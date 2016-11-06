package du.joandlab.firebaselab;

/**
 * Created by gson73 on 2016-10-28.
 */

public class ChatObject {

    private String message;
    private String recipient;
    private String sender;
    private String timeStamp;
    private int avatarId;

    private int mRecipientOrSenderStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getmRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }

    public void setmRecipientOrSenderStatus(int mRecipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = mRecipientOrSenderStatus;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }
}
