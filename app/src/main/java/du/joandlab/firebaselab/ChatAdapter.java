package du.joandlab.firebaselab;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Created by gson73 on 2016-10-28.
 */

class ChatAdapter extends FirebaseRecyclerAdapter<ChatObject, ChatHolder> {

    private List<ChatObject> mListOfChat;
    private static final int SENDER = 0;
    private static final int RECIPIENT = 1;

    public ChatAdapter(Class<ChatObject> modelClass, int modelLayout, Class<ChatHolder> viewHolderClass, DatabaseReference ref, List<ChatObject> mListOfChat) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mListOfChat = mListOfChat;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListOfChat.get(position).getmRecipientOrSenderStatus() == SENDER) {
            Log.e("Adapter", " sender");
            return SENDER;
        } else {
            return RECIPIENT;
        }
    }

    @Override
    protected void populateViewHolder(ChatHolder viewHolder, ChatObject model, int position) {

    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SENDER:
                View viewSender = inflater.inflate(R.layout.chat_item_sent, parent, false);
                viewHolder = new ChatHolder(viewSender);
                break;
            case RECIPIENT:
                View viewRecipient = inflater.inflate(R.layout.chat_item_rcv, parent, false);
                viewHolder = new ChatHolder(viewRecipient);
                break;
            default:
                View viewSenderDefault = inflater.inflate(R.layout.chat_item_sent, parent, false);
                viewHolder = new ChatHolder(viewSenderDefault);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);

        switch (viewHolder.getItemViewType()) {
            case SENDER:
                configureSenderView(viewHolder, position);
                break;
            case RECIPIENT:
                configureRecipientView(viewHolder, position);
                break;
        }

    }

    private void configureSenderView(ChatHolder chatHolder, int position) {
        ChatObject senderFireMessage = mListOfChat.get(position);
        chatHolder.getmSenderMessageTextView().setText(senderFireMessage.getMessage());
    }

    private void configureRecipientView(ChatHolder chatHolder, int position) {
        ChatObject recipientFireMessage = mListOfChat.get(position);
        chatHolder.getmRecipientMessageTextView().setText(recipientFireMessage.getMessage());
    }

    @Override
    public int getItemCount() {
        return mListOfChat.size();
    }
}