package com.nulp.rostykboiko.aicalendar.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nulp.rostykboiko.aicalendar.R;
import com.nulp.rostykboiko.aicalendar.editor.EditorActivity;
import com.nulp.rostykboiko.aicalendar.login.LoginActivity;
import com.nulp.rostykboiko.aicalendar.settings.SettingsActivity;
import com.nulp.rostykboiko.aicalendar.utils.GoogleAccountAdapter;
import com.nulp.rostykboiko.aicalendar.utils.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Intent editorIntent;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        editorIntent = new Intent(this, EditorActivity.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        session = new SessionManager(getApplicationContext());
        initUserData();
    }

    private void startLoginActivity(){
        startActivity(editorIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initUserData() {
        HashMap<String, String> user = session.getUserDetails();

        GoogleAccountAdapter.setUserName(user.get(SessionManager.KEY_NAME));
        GoogleAccountAdapter.setUserEmail(user.get(SessionManager.KEY_EMAIL));
        GoogleAccountAdapter.setAccountID(user.get(SessionManager.KEY_ACCOUNTID));
        GoogleAccountAdapter.setDeviceToken(user.get(SessionManager.KEY_TOKEN));
        GoogleAccountAdapter.setProfileIcon(user.get(SessionManager.KEY_ICON));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.action_profile:
                if (GoogleAccountAdapter.getUserEmail() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    onResume();
                    item.setTitle(R.string.action_logout);
                } else {
                    session.logoutUser();
                    GoogleAccountAdapter.logOut();
                    item.setTitle(R.string.action_login);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
