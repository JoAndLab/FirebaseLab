package du.joandlab.firebaselab;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = ChatFragment.class.getSimpleName();

    private RecyclerView mChatRecyclerView;
    private TextView mChatMessage;
    private EditText editChatText;
    private Button buttonSend;
    private TextView mChatTimeStamp;
    private ChatAdapter mChatAdapter;


    /* Sender and Recipient status*/
    private static final int SENDER_STATUS = 0;
    private static final int RECIPIENT_STATUS = 1;

    /* Recipient uid */
    private String mRecipientUid;

    /* Sender uid */
    private String mSenderUid;

    /* unique Firebase ref for this chat */
    private DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference(Ref.CHILD_CHAT);
    private DatabaseReference messageRef;

    /* Listen to change in chat in firabase-remember to remove it */
    private ChildEventListener mChatListener;

    private UserObject userObject;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Set recipient uid
        mRecipientUid = userObject.getRecipientUid();

        // Set sender uid;
        mSenderUid = userObject.getmCurrentUserUid();


        // Reference to recyclerView and text view
        mChatRecyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler_view);
        editChatText = (EditText) view.findViewById(R.id.chat_user_message);

        // Set recyclerView and adapter
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mChatRecyclerView.setHasFixedSize(true);

        // Initialize adapter
        List<ChatObject> emptyMessageChat = new ArrayList<>();
        mChatAdapter = new ChatAdapter(emptyMessageChat);

        mChatRecyclerView.setAdapter(mChatAdapter);


        buttonSend = (Button) view.findViewById(R.id.sendUserMessage);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToChat();
            }
        });


        return view;
    }

    public void getUserData(UserObject userObject) {

        this.userObject = userObject;

    }

    private void sendMessageToChat() {

        String senderMessage = editChatText.getText().toString();
        senderMessage = senderMessage.trim();

        if (!senderMessage.isEmpty()) {

            // Log.e(TAG, "send message");

            // Send message to firebase
            Map<String, String> newMessage = new HashMap<>();
            newMessage.put("sender", mSenderUid); // Sender uid
            newMessage.put("recipient", mRecipientUid); // Recipient uid
            newMessage.put("message", senderMessage); // Message

            chatRef.push().setValue(newMessage);

            // Clear text
            editChatText.setText("");

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChatListener = chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    // Log.e(TAG, "A new chat was inserted");

                    ChatObject newMessage = dataSnapshot.getValue(ChatObject.class);
                    if (newMessage.getSender().equals(mSenderUid)) {
                        newMessage.setmRecipientOrSenderStatus(SENDER_STATUS);
                    } else {
                        newMessage.setmRecipientOrSenderStatus(RECIPIENT_STATUS);
                    }
                    mChatAdapter.refillAdapter(newMessage);
                    mChatRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e(TAG, " I am onStart");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "I am onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "I am onStop");

        // Remove listener
        if (mChatListener != null) {
            // Remove listener
            chatRef.removeEventListener(mChatListener);
        }
        // Clean chat message
        mChatAdapter.cleanUp();

    }

}
