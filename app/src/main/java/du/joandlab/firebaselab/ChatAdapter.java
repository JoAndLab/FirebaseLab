package du.joandlab.firebaselab;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jogus on 2016-10-31.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatObject> mChatList;
    private static final int SENDER = 0;
    private static final int RECIPIENT = 1;

    public ChatAdapter(List<ChatObject> listOfChats) {
        mChatList = listOfChats;
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
    }

    private void configureRecipientView(ChatHolderRecipient chatHolderRecipient, int position) {
        ChatObject recipientFireMessage = mChatList.get(position);
        chatHolderRecipient.getmRecipientMessageTextView().setText(recipientFireMessage.getMessage());
        chatHolderRecipient.getmRecipientTimeStamp().setText(recipientFireMessage.getTimeStamp());
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
    }


    /*ViewHolder for Recipient*/
    public class ChatHolderRecipient extends RecyclerView.ViewHolder {

        private TextView mRecipientMessageTextView;
        private TextView mRecipientTimeStamp;

        public ChatHolderRecipient(View itemView) {
            super(itemView);
            mRecipientMessageTextView = (TextView) itemView.findViewById(R.id.message_text_view_rcv);
            mRecipientTimeStamp = (TextView) itemView.findViewById(R.id.timestamp_text_view_rcv);
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
    }
}