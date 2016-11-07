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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private static final String TAG = UsersFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private UserAdapter mUserAdapter;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Ref.CHILD_USERS);

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    /* progress bar */
    private View mProgressBarForUsers;

    /* Listen to users change in firebase-remember to detach it */
    private ChildEventListener mListenerUsers;

    /* current user uid */
    private String mCurrentUserUid;

    /* current user email */
    private String mCurrentUserEmail;

    /* List holding user key */
    private List<String> mUsersKeyList;

    private List<UserObject> userObjectList;


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
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mProgressBarForUsers = view.findViewById(R.id.progress_bar_users);
        // Initialize adapter
        userObjectList = new ArrayList<>();
        mUsersKeyList = new ArrayList<>();
        mUserAdapter = new UserAdapter(getContext(), userObjectList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mUserAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Chat");
        if (mFirebaseUser != null) {
            mCurrentUserUid = mFirebaseUser.getUid();
            mCurrentUserEmail = mFirebaseUser.getEmail();

        }

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
                        String username = currentUser.getUsername(); //Get current user first name
                        int avatarId = currentUser.getAvatarId(); //Get current user avatarId
                        mUserAdapter.setNameAndCreatedAt(username, avatarId); //Add it the adapter*/
                        Log.d(TAG, "onChildAdded: avatarID = " + avatarId);
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
    }
}
