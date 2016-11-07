package du.joandlab.firebaselab;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by jogus on 2016-10-31.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String PREFERENCES_FILE = "My_settings";

    private List<ChatObject> mChatList;
    private Context mContext;
    private static final int SENDER = 0;
    private static final int RECIPIENT = 1;

    private int mSenderAvatarId;
    private int mRecipientAvatarId;



    public ChatAdapter(List<ChatObject> listOfChats, Context context) {
        mChatList = listOfChats;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatList.get(position).getmRecipientOrSenderStatus() == SENDER) {
            Log.e("Adapter", " sender");
            return SENDER;
        } else {
            return RECIPIENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        mSenderAvatarId = Integer.valueOf(readSharedSetting(mContext, "currentUserAvatarId", "0"));
        mRecipientAvatarId = Integer.valueOf(readSharedSetting(mContext, "recipientAvatarId", "0"));
        Log.d(TAG, "onCreateViewHolder: " + mSenderAvatarId);
        Log.d(TAG, "onCreateViewHolder: " + mRecipientAvatarId);

        switch (viewType) {
            case SENDER:
                View viewSender = inflater.inflate(R.layout.chat_item_sent, viewGroup, false);
                viewHolder = new ChatHolderSender(viewSender);
                break;
            case RECIPIENT:
                View viewRecipient = inflater.inflate(R.layout.chat_item_rcv, viewGroup, false);
                viewHolder = new ChatHolderRecipient(viewRecipient);
                break;
            default:
                View viewSenderDefault = inflater.inflate(R.layout.chat_item_sent, viewGroup, false);
                viewHolder = new ChatHolderSender(viewSenderDefault);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case SENDER:
                ChatHolderSender chatHolderSender = (ChatHolderSender) viewHolder;
                configureSenderView(chatHolderSender, position);
                break;
            case RECIPIENT:
                ChatHolderRecipient chatHolderRecipient = (ChatHolderRecipient) viewHolder;
                configureRecipientView(chatHolderRecipient, position);
                break;
        }


    }

    private void configureSenderView(ChatHolderSender chatHolderSender, int position) {
        ChatObject senderFireMessage = mChatList.get(position);
        chatHolderSender.getmSenderMessageTextView().setText(senderFireMessage.getMessage());
        chatHolderSender.getmSenderTimeStamp().setText(senderFireMessage.getTimeStamp());
        int senderAvatarId = AvatarPicker.getDrawableAvatarId(mSenderAvatarId);
        Drawable avatarDrawable = ContextCompat.getDrawable(mContext, senderAvatarId);
        chatHolderSender.getmSenderAvatar().setImageDrawable(avatarDrawable);
    }

    private void configureRecipientView(ChatHolderRecipient chatHolderRecipient, int position) {
        ChatObject recipientFireMessage = mChatList.get(position);
        chatHolderRecipient.getmRecipientMessageTextView().setText(recipientFireMessage.getMessage());
        chatHolderRecipient.getmRecipientTimeStamp().setText(recipientFireMessage.getTimeStamp());
        int recipientAvatarId = AvatarPicker.getDrawableAvatarId(mRecipientAvatarId);
        Drawable avatarDrawable = ContextCompat.getDrawable(mContext, recipientAvatarId);
        chatHolderRecipient.getmRecipientAvatar().setImageDrawable(avatarDrawable);
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }


    public void refillAdapter(ChatObject newFireChatMessage) {

        /*add new message chat to list*/
        mChatList.add(newFireChatMessage);

        /*refresh view*/
        notifyItemInserted(getItemCount() - 1);
    }

    public void refillFirsTimeAdapter(List<ChatObject> newFireChatMessage) {

        /*add new message chat to list*/
        mChatList.clear();
        mChatList.addAll(newFireChatMessage);
        /*refresh view*/
        notifyItemInserted(getItemCount() - 1);
    }

    public void cleanUp() {
        mChatList.clear();
    }


    /*==============ViewHolder===========*/

    /*ViewHolder for Sender*/

    public class ChatHolderSender extends RecyclerView.ViewHolder {

        private TextView mSenderMessageTextView;
        private TextView mSenderTimeStamp;
        private CircleImageView mSenderAvatar;

        public ChatHolderSender(View itemView) {
            super(itemView);
            mSenderMessageTextView = (TextView) itemView.findViewById(R.id.message_text_view_sent);
            mSenderTimeStamp = (TextView) itemView.findViewById(R.id.timestamp_text_view_sent);
            mSenderAvatar = (CircleImageView) itemView.findViewById(R.id.senderAvatar);
        }

        public TextView getmSenderMessageTextView() {
            return mSenderMessageTextView;
        }

        public void setmSenderMessageTextView(TextView mSenderMessageTextView) {
            this.mSenderMessageTextView = mSenderMessageTextView;
        }

        public TextView getmSenderTimeStamp() {
            return mSenderTimeStamp;
        }

        public void setmSenderTimeStamp(TextView mSenderTimeStamp) {
            this.mSenderTimeStamp = mSenderTimeStamp;
        }

        public CircleImageView getmSenderAvatar() {
            return mSenderAvatar;
        }

        public void setmSenderAvatar(CircleImageView mSenderAvatar) {
            this.mSenderAvatar = mSenderAvatar;
        }
    }


    /*ViewHolder for Recipient*/
    public class ChatHolderRecipient extends RecyclerView.ViewHolder {

        private TextView mRecipientMessageTextView;
        private TextView mRecipientTimeStamp;
        private CircleImageView mRecipientAvatar;

        public ChatHolderRecipient(View itemView) {
            super(itemView);
            mRecipientMessageTextView = (TextView) itemView.findViewById(R.id.message_text_view_rcv);
            mRecipientTimeStamp = (TextView) itemView.findViewById(R.id.timestamp_text_view_rcv);
            mRecipientAvatar = (CircleImageView) itemView.findViewById(R.id.recipientAvatar);
        }

        public TextView getmRecipientMessageTextView() {
            return mRecipientMessageTextView;
        }

        public void setmRecipientMessageTextView(TextView mRecipientMessageTextView) {
            this.mRecipientMessageTextView = mRecipientMessageTextView;
        }

        public TextView getmRecipientTimeStamp() {
            return mRecipientTimeStamp;
        }

        public void setmRecipientTimeStamp(TextView mRecipientTimeStamp) {
            this.mRecipientTimeStamp = mRecipientTimeStamp;
        }

        public CircleImageView getmRecipientAvatar() {
            return mRecipientAvatar;
        }

        public void setmRecipientAvatar(CircleImageView mRecipientAvatar) {
            this.mRecipientAvatar = mRecipientAvatar;
        }
    }
}