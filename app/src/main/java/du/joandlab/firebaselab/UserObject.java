package du.joandlab.firebaselab;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jogus on 2016-10-27.
 */

class UserObject implements Parcelable {

    private String username;
    private String connection;
    private String email;
    private String registerdate;
    private int avatarId;

    private String recipientUid;

    /*Current user (or sender) info*/
    private String mCurrentUserName;
    private String mCurrentUserUid;
    private String mCurrentUserEmail;
    private String mCurrentUserCreatedAt;


    public UserObject() {

    }

    private UserObject(Parcel pIN) {

        username = pIN.readString();
        connection = pIN.readString();
        email = pIN.readString();
        registerdate = pIN.readString();
        avatarId = pIN.readInt();
        recipientUid = pIN.readString();
        mCurrentUserName = pIN.readString();
        mCurrentUserUid = pIN.readString();
        mCurrentUserEmail = pIN.readString();
        mCurrentUserCreatedAt = pIN.readString();
    }

    public static final Creator<UserObject> CREATOR = new Creator<UserObject>() {
        @Override
        public UserObject createFromParcel(Parcel in) {
            return new UserObject(in);
        }

        @Override
        public UserObject[] newArray(int size) {
            return new UserObject[size];
        }
    };

    String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
    }

    public String getRecipientUid() {
        return recipientUid;
    }

    public void setRecipientUid(String recipientUid) {
        this.recipientUid = recipientUid;
    }

    public String getmCurrentUserName() {
        return mCurrentUserName;
    }

    public void setmCurrentUserName(String mCurrentUserName) {
        this.mCurrentUserName = mCurrentUserName;
    }

    public String getmCurrentUserUid() {
        return mCurrentUserUid;
    }

    public void setmCurrentUserUid(String mCurrentUserUid) {
        this.mCurrentUserUid = mCurrentUserUid;
    }

    public String getmCurrentUserEmail() {
        return mCurrentUserEmail;
    }

    public void setmCurrentUserEmail(String mCurrentUserEmail) {
        this.mCurrentUserEmail = mCurrentUserEmail;
    }

    public String getmCurrentUserCreatedAt() {
        return mCurrentUserCreatedAt;
    }

    public void setmCurrentUserCreatedAt(String mCurrentUserCreatedAt) {
        this.mCurrentUserCreatedAt = mCurrentUserCreatedAt;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(connection);
        parcel.writeString(email);
        parcel.writeString(registerdate);
        parcel.writeInt(avatarId);
        parcel.writeString(recipientUid);
        parcel.writeString(mCurrentUserName);
        parcel.writeString(mCurrentUserUid);
        parcel.writeString(mCurrentUserEmail);
        parcel.writeString(mCurrentUserCreatedAt);
    }

    /*create chat endpoint for firebase*/
    public String getChatRef() {
        return createUniqueChatRef();
    }

    private String createUniqueChatRef() {
        String uniqueChatRef;

        uniqueChatRef = cleanEmailAddress(getmCurrentUserEmail()) + "-" + cleanEmailAddress(getEmail());

        return uniqueChatRef;
    }

    private String cleanEmailAddress(String email) {

        //replace dot with underscore since firebase does not allow dot as key
        return email.replace(".", "_");

    }
}