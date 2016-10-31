package du.joandlab.firebaselab;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private EditText editEmail;
    private EditText editUsername;
    private EditText editFullname;
    private EditText editPhone;
    private EditText editAddress;
    private TextView fullName;
    private TextView userName;
    private TextView userEmail;
    private TextView registerDate;
    private TextView updateDate;
    private String profileUpdate;
    private CircleImageView profileImage;


    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    final DatabaseReference rootRefUser = FirebaseDatabase.getInstance().getReference(Ref.CHILD_USERS);
    private String userUid;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUid = mFirebaseUser.getUid();

        profileImage = (CircleImageView) getActivity().findViewById(R.id.profile_image);
        fullName = (TextView) getActivity().findViewById(R.id.user_name);
        userName = (TextView) getActivity().findViewById(R.id.user_nick);
        userEmail = (TextView) getActivity().findViewById(R.id.user_email);
        editUsername = (EditText)getActivity().findViewById(R.id.edit_username);
        editEmail = (EditText)getActivity().findViewById(R.id.edit_email);
        editFullname = (EditText)getActivity().findViewById(R.id.edit_name);
        editAddress = (EditText)getActivity().findViewById(R.id.edit_address);
        editPhone = (EditText)getActivity().findViewById(R.id.edit_phone);
        registerDate = (TextView) getActivity().findViewById(R.id.register_date);
        updateDate = (TextView) getActivity().findViewById(R.id.update_date);
        editEmail.setEnabled(false);
        editFullname.setEnabled(false);

        editUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    updateUser(rootRefUser.child(userUid).child("username"), editUsername);
                    if(userName.getText().toString().length() != 0)
                        userName.setVisibility(View.GONE);
                    else
                        userName.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onFocusChange: " + userName.getText().toString().length());
                }
            }
        });
        editFullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    updateUser(rootRefUser.child(userUid).child("name"), editFullname);
                }
            }
        });
        editAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                   updateUser(rootRefUser.child(userUid).child("address"), editAddress);
                }
            }
        });
        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    updateUser(rootRefUser.child(userUid).child("phone"), editPhone);
                }
            }
        });
        rootRefUser.child(userUid).child("updated").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                profileUpdate = dataSnapshot.getValue(String.class);
                updateDate.setText("Profile updated: " + profileUpdate);
                hideField(updateDate, 22);
                Log.d(TAG, "onDataChange: " + profileUpdate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void updateUser(DatabaseReference ref, EditText field){
        ref.setValue(field.getText().toString());
        rootRefUser.child(userUid).child("updated").setValue(getDate());
    }

    private String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String updateDateandTime = dateFormatGmt.format(new Date());
        return updateDateandTime;
    }

    private TextView hideField(TextView field, int length){
        TextView mField = field;
        if(field.getText().toString().length() < length || field.getText().toString().equals(""))
            field.setVisibility(View.GONE);
        else
            field.setVisibility(View.VISIBLE);
        return mField;
    }

    private void getUserData(){

        rootRefUser.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserObject user = dataSnapshot.getValue(UserObject.class);
                if(mFirebaseUser.getProviders().contains("google.com")) {
                    rootRefUser.child(userUid).child("name").setValue(mFirebaseUser.getDisplayName());
                    rootRefUser.child(userUid).child("email").setValue(mFirebaseUser.getEmail());
                    profileImage.setImageResource(R.drawable.google_login);
                }
                else {
                    profileImage.setImageResource(AvatarPicker.getDrawableAvatarId(user.getAvatarId()));
                }
                fullName.setText(user.getName());
                userName.setText(user.getUsername());
                hideField(userName, 0);
                userEmail.setText(user.getEmail());
                editUsername.setText(user.getUsername());
                editEmail.setText(user.getEmail());
                editFullname.setText(user.getName());
                editAddress.setText(user.getAddress());
                editPhone.setText(user.getPhone());
                registerDate.setText("Account created: " + user.getRegisterdate());
                hideField(registerDate, 22);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getUserData();
    }

    @Override
    public void onPause(){
        super.onPause();

    }

}
