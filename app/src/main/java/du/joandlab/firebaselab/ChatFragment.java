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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = ChatFragment.class.getSimpleName();

    private RecyclerView mChatRecyclerView;
    private EditText editChatText;
    private Button buttonSend;
    private ChatAdapter mChatAdapter;


    /* Sender and Recipient status*/
    private static final int SENDER_STATUS = 0;
    private static final int RECIPIENT_STATUS = 1;

    /* Recipient uid */
    private String mRecipientUid;

    /* Sender uid */
    private String mSenderUid;

    private String mTimeStamp;

    /* unique Firebase ref for this chat */
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

        // Set chat layout to recycleview - messages start at bottom
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(manager);

        // Initialize adapter
        List<ChatObject> emptyMessageChat = new ArrayList<>();
        mChatAdapter = new ChatAdapter(emptyMessageChat, getContext());

        mChatRecyclerView.setAdapter(mChatAdapter);


        messageRef = rootRef.child(Ref.CHILD_CHAT).child(getChatRef());


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
        SimpleDateFormat time = new SimpleDateFormat("EEEE HH:mm", Locale.getDefault());
        mTimeStamp = time.format(new Date());
        senderMessage = senderMessage.trim();

        if (!senderMessage.isEmpty()) {

            // Log.e(TAG, "send message");

            // Send message to firebase
            Map<String, String> newMessage = new HashMap<>();
            newMessage.put("sender", mSenderUid); // Sender uid
            newMessage.put("recipient", mRecipientUid); // Recipient uid
            newMessage.put("message", senderMessage); // Message
            newMessage.put("timeStamp", mTimeStamp); // TimeStamp

            messageRef.push().setValue(newMessage);

            // Clear text
            editChatText.setText("");

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChatListener = messageRef.addChildEventListener(new ChildEventListener() {
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

    /*create chat endpoint for firebase*/
    public String getChatRef() {
        return createUniqueChatRef();
    }

    private String createUniqueChatRef() {
        String uniqueChatRef = "";

        String str1 = mRecipientUid.replaceAll("\\D+", "");
        String str2 = mSenderUid.replaceAll("\\D+", "");

        long id1 = Long.parseLong(str1);
        long id2 = Long.parseLong(str2);

        if (id2 > id1) {
            uniqueChatRef = cleanEmailAddress(userObject.getmCurrentUserEmail()) + "-" + cleanEmailAddress(userObject.getEmail());
        } else {

            uniqueChatRef = cleanEmailAddress(userObject.getEmail()) + "-" + cleanEmailAddress(userObject.getmCurrentUserEmail());
        }
        return uniqueChatRef;
    }


    private String cleanEmailAddress(String email) {

        email = email.toLowerCase();
        //replace dot with comma since firebase does not allow dot
        return email.replace(".", "_");

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
            messageRef.removeEventListener(mChatListener);
        }
        // Clean chat message
        mChatAdapter.cleanUp();

    }

}
