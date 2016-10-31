package du.joandlab.firebaselab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private boolean homeFragmentActivated;
    boolean mFromSavedInstanceState;
    boolean mUserLearnedDrawer;
    int mCurrentSelectedPosition = 0;

    private static final String STATE_SELECTED_POSITION = null;
    private static final String PREF_USER_LEARNED_DRAWER = null;
    private static final String PREFERENCES_FILE = "My_settings";
    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    final DatabaseReference rootRefUser = FirebaseDatabase.getInstance().getReference(Ref.CHILD_USERS);
    private String userUid;

    private String mUsername;
    private String mPhotoUrl;

    private TextView fullName;
    private TextView textEmail;
    private CircleImageView profileImage;

    private SharedPreferences sharedPrefs;
    private Boolean _settingsTheme;
    private String _settingsChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get default sharedpreferences
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Register listner on changes made to settings
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        // Change theme
        loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition =
                    savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.navigation_view);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null)
            userUid = mFirebaseUser.getUid();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            rootRefUser.child(userUid).child(Ref.CHILD_CONNECTION).setValue("online");
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
    }
    public void loadTheme() {
        // Change theme (default light)
        _settingsTheme = sharedPrefs.getBoolean("change_theme", false);
        setTheme(_settingsTheme ? R.style.AppThemeDark : R.style.AppTheme );
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        if (!mUserLearnedDrawer) {
            mDrawer.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;

            case R.id.action_logout:
                rootRefUser.child(userUid).child(Ref.CHILD_CONNECTION).setValue("offline");
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_profile:
                fragmentClass = ProfileFragment.class;
                mCurrentSelectedPosition = 0;
                break;
            case R.id.navigation_item_chat:
                fragmentClass = ChatFragment.class;
                mCurrentSelectedPosition = 1;
                break;
            case R.id.navigation_item_about:
                fragmentClass = AboutFragment.class;
                mCurrentSelectedPosition = 2;
                break;
            default:
                fragmentClass = ProfileFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        homeFragmentActivated = false;
        // setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("Main", "onPostCreate");
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("Main", "onConfigChanged");
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = nvDrawer.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    public void setUserData(){

        rootRefUser.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserObject user = dataSnapshot.getValue(UserObject.class);
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
                View v = navigationView.getHeaderView(0);
                profileImage = (CircleImageView)v.findViewById(R.id.profile_image);
                fullName = (TextView)v.findViewById(R.id.user_name);
                textEmail = (TextView)v.findViewById(R.id.user_email);
                if(mFirebaseUser.getProviders().contains("google.com")) {
                    fullName.setText(mFirebaseUser.getDisplayName());
                    textEmail.setText(mFirebaseUser.getEmail());
                } else {
                    profileImage.setImageResource(AvatarPicker.getDrawableAvatarId(user.getAvatarId()));
                    fullName.setText(user.getName());
                    textEmail.setText(user.getEmail());
                }
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
        setUserData();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mFirebaseUser != null) {
            rootRefUser.child(userUid).child(Ref.CHILD_CONNECTION).setValue("offline");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("change_theme")) {
            recreate();
        }
        else if (key.equals("setting_change_password")) {
            _settingsChangePassword = sharedPrefs.getString("setting_change_password", "");
            changePassword(_settingsChangePassword);
        }
    }

    private void changePassword(String newPassword){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "User password changed!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "User password changed.");
                        }
                    }
                });
    }
}
