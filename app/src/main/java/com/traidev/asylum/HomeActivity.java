package com.traidev.asylum;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.traidev.asylum.Utility.Main_Interface;
import com.traidev.asylum.Utility.Network.RetrofitClient;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerViewHosptials;
    private RecyclerView.LayoutManager layoutManager;
    private List<HospitalViewModal> posts;
    private HospitalsAdapter adapter;
    FrameLayout frameShow;
    private Main_Interface main_interface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences sharedPreferences = getSharedPreferences("USER_CREDENTIALS", MODE_PRIVATE);
        final String name = sharedPreferences.getString("NAME", "Asylum");
        final String email = sharedPreferences.getString("EMAIL", "info@asylum@gmail.com");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        TextView navEmail = (TextView) headerView.findViewById(R.id.navEmail);
        navUsername.setText(name);
        navEmail.setText(email);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.login);


        recyclerViewHosptials = findViewById(R.id.all_hospitals_list);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHosptials.setLayoutManager(layoutManager);
        recyclerViewHosptials.setHasFixedSize(true);
        frameShow = findViewById(R.id.showBeforeLoadFrame);

        fetchHospitals();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            alertOpen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SearchHospitals.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_doctors) {
            startActivity(new Intent(this,DoctorsActivity.class));

        } else if (id == R.id.nav_donate) {
            Intent i = new Intent(getApplicationContext(), AboutDonate.class);
            i.putExtra("pType", "donate");
            startActivity(i);
        } else if (id == R.id.nav_share) {
            shareApp();

        } else if (id == R.id.nav_about) {
            Intent i = new Intent(getApplicationContext(), AboutDonate.class);
            i.putExtra("pType", "about");
            startActivity(i);
        }
        else if (id == R.id.rate) {
            RateUs();
        }
        else if (id == R.id.login) {
            final SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
            final String name=sharedPreferences.getString("NAME","DEFAULT_NAME");

            sharedPreferences.edit().putBoolean("ISLOGGEDIN",false).commit();
            startActivity(new Intent(this,LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void fetchHospitals()
    {
        frameShow.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<HospitalViewModal>> call = main_interface.getHospitals("all");

        call.enqueue(new Callback<List<HospitalViewModal>>() {
            @Override
            public void onResponse(Call<List<HospitalViewModal>> call, Response<List<HospitalViewModal>> response) {
                if(response.code() != 404)
                {
                    posts = response.body();
                    adapter = new HospitalsAdapter(posts,getApplicationContext());
                    recyclerViewHosptials.setAdapter(adapter);
                }
                else
                { }
                frameShow.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<HospitalViewModal>> call, Throwable t) {

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void alertOpen()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(getDrawable(R.drawable.searchdoctor))
                .setTitle("Are you sure want to Exit!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        finish();

                    }
                })
//set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                    }
                })
                .show();
    }

    private void RateUs() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void shareApp() {

        Toast.makeText(getApplicationContext(), "Hello Friends", Toast.LENGTH_SHORT).show();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Message from Asylum   \n" +
                        "  \nCheckout Asylum App at: https://play.google.com/store/apps/details?id=" + getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }





}
