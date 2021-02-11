package com.traidev.asylum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.traidev.asylum.Utility.Main_Interface;
import com.traidev.asylum.Utility.Network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHosptials;
    private RecyclerView.LayoutManager layoutManager;
    private List<DoctorViewModal> posts;
    private DoctorsAdapter adapter;
    FrameLayout frameShow;
    private Main_Interface main_interface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);

        recyclerViewHosptials = findViewById(R.id.all_doctors_list);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHosptials.setLayoutManager(layoutManager);
        recyclerViewHosptials.setHasFixedSize(true);
        frameShow = findViewById(R.id.showBeforeLoadFrame);


        ImageView BackBtn = findViewById(R.id.backClick);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           String type = extras.getString("service");
            fetchDoctors(type);
        }
        else
        {
            fetchDoctors("all");
        }


    }


    public void fetchDoctors(String type)
    {
        frameShow.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<DoctorViewModal>> call = main_interface.getDoctors(type);

        call.enqueue(new Callback<List<DoctorViewModal>>() {
            @Override
            public void onResponse(Call<List<DoctorViewModal>> call, Response<List<DoctorViewModal>> response) {
                if(response.code() != 404)
                {
                    posts = response.body();
                    adapter = new DoctorsAdapter(posts,getApplicationContext());
                    recyclerViewHosptials.setAdapter(adapter);
                }
                else
                { }
                frameShow.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<DoctorViewModal>> call, Throwable t) {

            }
        });
    }

}
