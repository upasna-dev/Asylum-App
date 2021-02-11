package com.traidev.asylum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.traidev.asylum.Utility.Main_Interface;
import com.traidev.asylum.Utility.Network.RetrofitClient;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchHospitals extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<HospitalViewModal> posts;
    private HospitalsAdapter adapter;
    private Main_Interface main_interface;
    private ImageView mike,back;
    private EditText SearchKey;
    Bundle extras;
    GifImageView loader;
    ImageView notFound;
    private TextView data;
    String searchString;
    ScrollView searchResutl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_posts);

        SearchKey = findViewById(R.id.searchKey);
        mike = findViewById(R.id.mike);
        notFound = findViewById(R.id.notFoundImg);
        back = findViewById(R.id.back);
        data = findViewById(R.id.searchfor);
        searchResutl = findViewById(R.id.ResultSearch);
        loader = findViewById(R.id.updateImgProgress);

        recyclerView = (RecyclerView) findViewById(R.id.all_postRec);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });


        if(savedInstanceState == null)
        {
            extras = getIntent().getExtras();
            if(extras == null) {
                searchString= null;
            }
            else {
                searchString= extras.getString("msg");
                SearchPost(searchString);
                data.setText("You Searched for " +searchString);
                SearchKey.setText(searchString);
            }
        }

        SearchKey.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    SearchPost(SearchKey.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        mike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchPost(SearchKey.getText().toString());
                data.setText("You Search for " +SearchKey.getText().toString());
                SearchKey.onEditorAction(EditorInfo.IME_ACTION_DONE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });
    }

    public void SearchPost(String keyword)
    {
        notFound.setVisibility(View.GONE);

        loader.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);
        Call<List<HospitalViewModal>> call = main_interface.getHospitals(keyword);

        call.enqueue(new Callback<List<HospitalViewModal>>() {
            @Override
            public void onResponse(Call<List<HospitalViewModal>> call, Response<List<HospitalViewModal>> response) {
                if(response.code() == 201)
                {
                    searchResutl.setVisibility(View.VISIBLE);
                    posts = response.body();
                    adapter = new HospitalsAdapter(posts,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    loader.setVisibility(View.GONE);
                    notFound.setVisibility(View.GONE);
                }
                else
                {
                    recyclerView.setAdapter(null);
                    notFound.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<List<HospitalViewModal>> call, Throwable t) {
            }
        });
    }


}
