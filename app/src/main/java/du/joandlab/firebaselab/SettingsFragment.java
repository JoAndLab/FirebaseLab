package du.joandlab.firebaselab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Anders Mellberg on 2016-10-27.
 */


public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    final DatabaseReference rootRefUser = FirebaseDatabase.getInstance().getReference(Ref.CHILD_USERS);
    private String userUid;
    private int avatarId;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userUid = mFirebaseAuth.getCurrentUser().getUid();

        assert mFirebaseUser != null;
        if(mFirebaseUser.getProviders().contains("google.com")) {
            final PreferenceCategory password = (PreferenceCategory) findPreference("user_settings");
            PreferenceScreen mCategory = (PreferenceScreen) findPreference("pref_screen");
            mCategory.removePreference(password);
        }
        else {
            // Set Avatar
            final Preference avatar = (Preference) findPreference("setting_change_avatar");
            rootRefUser.child(userUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserObject user = dataSnapshot.getValue(UserObject.class);
                    avatar.setIcon(AvatarPicker.getDrawableAvatarId(user.getAvatarId()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            avatar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    int avatarId = AvatarPicker.generateRandomAvatarForUser();
                    avatar.setIcon(AvatarPicker.getDrawableAvatarId(avatarId));
                    rootRefUser.child(userUid).child("avatarId").setValue(avatarId);
                    Log.d(TAG, "onPreferenceClick: " + avatarId);
                    return false;
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals("change_theme")) {
            Preference pref = findPreference(key);
            pref.setDefaultValue(prefs.getBoolean(key, false));
            getActivity().recreate();
        }
    }
}

