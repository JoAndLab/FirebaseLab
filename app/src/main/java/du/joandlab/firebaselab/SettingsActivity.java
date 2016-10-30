package du.joandlab.firebaselab;

/**
 * Created by Anders Mellberg on 2016-10-27.
 */


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private Boolean _settingsTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get default sharedpreferences
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Change theme
        loadTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        // Add toolbar
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction().replace(R.id.frame_settings,
                new SettingsFragment()).commit();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }
    public void loadTheme() {
        // Change theme (default light)
        _settingsTheme = sharedPrefs.getBoolean("change_theme", false);
        setTheme(_settingsTheme ? R.style.AppThemeDark : R.style.AppTheme );
    }
}
