package com.app.profileapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.profileapplication.models.User;
import com.app.profileapplication.ui.home.HomeFragment;
import com.app.profileapplication.utilities.Parameters;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String token;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageView userProfileImageView;
    private TextView displayNameTextView;
    private TextView displayEmailIdTextView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "HomeActivity";
    private User user;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager = getSupportFragmentManager();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString(Parameters.TOKEN, "");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView logOut = findViewById(R.id.logout);
        logOut.setOnClickListener(view -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        getData(Parameters.API_URL_LOCAL+"/user/profile");

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View hView = navigationView.getHeaderView(0);
        displayNameTextView = hView.findViewById(R.id.displayNameTextView);
        displayEmailIdTextView = hView.findViewById(R.id.displayEmailIdTextView);
        userProfileImageView = hView.findViewById(R.id.userProfileImageView);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString(Parameters.TOKEN, token);
            bundle.putSerializable(Parameters.USER_ID, user);
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment, "Chat Fragment").addToBackStack(null).commit();
            toolbar.setTitle(R.string.menu_home);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        int backCount = fragmentManager.getBackStackEntryCount();
        if(backCount > 1){
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            fragmentManager.popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Parameters.TOKEN, token);
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment, Parameters.HOME_FRAGMENT).addToBackStack(null).commit();
                toolbar.setTitle(R.string.menu_home);
                navigationView.setCheckedItem(R.id.nav_home);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getData(String url){
        if (token!=null){
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization",Parameters.BEARER + " " + token)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);

                        Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            Log.v(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }

                        String responseString = responseBody.string();
                        Log.v(TAG, responseString);
                        try {
                            JSONObject json = new JSONObject(responseString);
                            user = new User(json);
                            displayNameTextView.setText(user.getFirstName()+" "+user.getLastName());
                            displayEmailIdTextView.setText(user.getEmail());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }else{
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
