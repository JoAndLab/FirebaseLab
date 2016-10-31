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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private static final String TAG = UsersFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private UserAdapter mUserAdapter;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Ref.CHILD_USERS);
    private DatabaseReference connectionRef = FirebaseDatabase.getInstance().getReference(Ref.CHILD_CONNECTION);
    /* progress bar */
    private View mProgressBarForUsers;

    /* Listen to users change in firebase-remember to detach it */
    private ChildEventListener mListenerUsers;

    /* Listen for user presence */
    private ValueEventListener mConnectedListener;

    /* current user uid */
    private String mCurrentUserUid = "11";

    /* current user email */
    private String mCurrentUserEmail;

    /* List holding user key */
    private static List<String> mUsersKeyList;

    private List<UserObject> userObjectList;

    int mPosition;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        getActivity().setTitle(getString(R.string.userList));
        recyclerView = (RecyclerView) view.findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);
        mProgressBarForUsers = view.findViewById(R.id.progress_bar_users);
        // Initialize adapter
        userObjectList = new ArrayList<>();
        mUsersKeyList = new ArrayList<>();
        mUserAdapter = new UserAdapter(UserObject.class, R.layout.user_item, UserHolder.class, userRef, userObjectList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mUserAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queryChatUsers();

    }

    private void queryChatUsers() {

        //Show progress bar
        showProgressBarForUsers();

        mListenerUsers = userRef.limitToFirst(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //Hide progress bar
                hideProgressBarForUsers();

                if (dataSnapshot.exists()) {
                    //Log.e(TAG, "A new user was inserted");

                    String userUid = dataSnapshot.getKey();

                    if (!userUid.equals(mCurrentUserUid)) {

                        //Get recipient user name
                        UserObject user = dataSnapshot.getValue(UserObject.class);

                        //Add recipient uid
                        user.setRecipientUid(userUid);

                        //Add current user (or sender) info
                        user.setmCurrentUserEmail(mCurrentUserEmail); //email
                        user.setmCurrentUserUid(mCurrentUserUid);//uid
                        mUsersKeyList.add(userUid);
                        mUserAdapter.refill(user);

                    } else {
                        UserObject currentUser = dataSnapshot.getValue(UserObject.class);
                        /*String username = currentUser.getFirstName(); //Get current user first name
                        String createdAt = currentUser.getCreatedAt(); //Get current user date creation
                        mUsersChatAdapter.setNameAndCreatedAt(userName, createdAt); //Add it the adapter*/
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    String userUid = dataSnapshot.getKey();
                    if (!userUid.equals(mCurrentUserUid)) {
                        UserObject user = dataSnapshot.getValue(UserObject.class);

                        //Add recipient uid
                        user.setRecipientUid(userUid);

                        //Add current user (or sender) info
                        user.setmCurrentUserEmail(mCurrentUserEmail); //email
                        user.setmCurrentUserUid(mCurrentUserUid);//uid
                        int index = mUsersKeyList.indexOf(userUid);
                        Log.e(TAG, "change index " + index);
                        mUserAdapter.changeUser(index, user);
                    }

                }

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

        /*final DatabaseReference myConnection = userRef.child(mCurrentUserUid).child(Ref.CHILD_CONNECTION);

        mConnectedListener = myConnection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String connection = (String) dataSnapshot.getValue();
                if (connection) {

                    myConnection.setValue(Ref.KEY_ONLINE);

                    // When this device disconnects, remove it
                    myConnection.onDisconnect().setValue(Ref.KEY_OFFLINE);
                    Toast.makeText(getActivity(), "Connected to Firebase", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getActivity(), "Disconnected from Firebase", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void hideProgressBarForUsers() {
        if (mProgressBarForUsers.getVisibility() == View.VISIBLE) {
            mProgressBarForUsers.setVisibility(View.GONE);
        }
    }

    private void showProgressBarForUsers() {
        mProgressBarForUsers.setVisibility(View.VISIBLE);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mUsersKeyList.clear();

        // Stop all listeners
        // Make sure to check if they have been initialized
        if (mListenerUsers != null) {
            userRef.removeEventListener(mListenerUsers);
        }
        if (mConnectedListener != null) {
            connectionRef.removeEventListener(mConnectedListener);
        }
    }

    public void startNewChat(int position) {
        this.mPosition = position;
        Log.d(TAG, "startNewChat: " + position);

        String test = mUsersKeyList.get(position);

        Log.d(TAG, "startNewChat: " + test);


    }

}
