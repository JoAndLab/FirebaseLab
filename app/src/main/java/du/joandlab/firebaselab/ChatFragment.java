package du.joandlab.firebaselab;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = ChatFragment.class.getSimpleName();

    private RecyclerView mChatRecyclerView;
    private TextView mChatMessage;
    private EditText editChatText;
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

    /* Listen to change in chat in firabase-remember to remove it */
    private ChildEventListener mChatListener;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Reference to recyclerView and text view
        mChatRecyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

    }

}
