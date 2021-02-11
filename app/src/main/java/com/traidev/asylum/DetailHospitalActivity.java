package com.traidev.asylum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.traidev.asylum.Utility.DefaultResponse;
import com.traidev.asylum.Utility.Network.RetrofitClient;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailHospitalActivity extends AppCompatActivity {

    String HospitalId = null;
    FrameLayout frameShow;
    ImageView banner,b2,b3,b4;
    Button Emer,Enq;

    private int mYear, mMonth, mDay;


    Button s1,s2,s3,s4,s5,other;
    TextView title,rating,about,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hosptial_detail_activity);

        title = findViewById(R.id.hTitle);
        rating = findViewById(R.id.hRating);
        about = findViewById(R.id.hAbout);
        address = findViewById(R.id.hAddress);
        Emer = findViewById(R.id.em);
        Enq = findViewById(R.id.en);

        frameShow = findViewById(R.id.showBeforeLoadFrame);

        banner = findViewById(R.id.hThumb);
        b2 = findViewById(R.id.hThumb2);
        b3 = findViewById(R.id.hThumb3);
        b4 = findViewById(R.id.hThumb4);

        s1 = findViewById(R.id.ser1);
        s2 = findViewById(R.id.ser2);
        s3 = findViewById(R.id.ser3);
        s4 = findViewById(R.id.ser4);
        s5 = findViewById(R.id.ser5);
        other = findViewById(R.id.other);

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DoctorsActivity.class));
                finish();
            }
        });


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DoctorsActivity.class);
                i.putExtra("service", s1.getText());
                startActivity(i);
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DoctorsActivity.class);
                i.putExtra("service", s2.getText());
                startActivity(i);
            }
        });

        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DoctorsActivity.class);
                i.putExtra("service", s3.getText());
                startActivity(i);
            }
        });

        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DoctorsActivity.class);
                i.putExtra("service", s4.getText());
                startActivity(i);
            }
        });

        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DoctorsActivity.class);
                i.putExtra("service", s5.getText());
                startActivity(i);
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            HospitalId = extras.getString("key");
            fetchSingleHospital();
            fetchSingleServices();
        }

        ImageView BackBtn = findViewById(R.id.backClick);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });

        Enq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookAppointment();
            }
        });

 }

    public void fetchSingleHospital()
    {
        frameShow.setVisibility(View.VISIBLE);
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getSingleHospital(HospitalId);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201)
                {
                    String data = dr.getMessage();
                    String[] dif = data.split("#");
                    title.setText(dif[0]);
                    about.setText(dif[1]);

                    rating.setText(dif[2]);
                    address.setText(dif[3]);

                    Glide.with(getApplicationContext())
                            .load("https://traidev.com/LIVE_APPS/Asylum/con/"+dif[4]).into((banner));
                    Glide.with(getApplicationContext())
                            .load("https://traidev.com/LIVE_APPS/Asylum/con/"+dif[5]).into((b2));
                    Glide.with(getApplicationContext())
                            .load("https://traidev.com/LIVE_APPS/Asylum/con/"+dif[6]).into((b3));
                    Glide.with(getApplicationContext())
                            .load("https://traidev.com/LIVE_APPS/Asylum/con/"+dif[7]).into((b4));

                    frameShow.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });

    }

    public void fetchSingleServices()
    {
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getServices(HospitalId);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201)
                {
                    String data = dr.getMessage();
                    String[] dif = data.split("#");
                    s1.setText(dif[0]);
                    s2.setText(dif[1]);
                    s3.setText(dif[2]);
                    s4.setText(dif[3]);
                    s5.setText(dif[4]);
                }

            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });

    }



    public void BookAppointment()
    {

        final BottomSheetDialog book = new BottomSheetDialog(DetailHospitalActivity.this);
        book.setContentView(R.layout.book_appo_form);
        book.setCanceledOnTouchOutside(false);
        book.show();

        LinearLayout btnBook;
        final EditText pName,pProb,pMobile,pDate;
        final TextView hosName,datePick;
        final ProgressBar prog;

        btnBook = book.findViewById(R.id.bookBtnClick);
        pName = book.findViewById(R.id.bName);
        pProb = book.findViewById(R.id.bProblem);
        pMobile = book.findViewById(R.id.bMobile);
        datePick = book.findViewById(R.id.dateText);
        prog = book.findViewById(R.id.progressHos);

        hosName = book.findViewById(R.id.hospitalTitle);
        hosName.setText(title.getText().toString());

        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePick.setText(dayOfMonth + " / " + (monthOfYear + 1) + " ");

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dname,date,problem,dmob;

                dname = pName.getText().toString();
                date = datePick.getText().toString();
                problem = pProb.getText().toString();
                dmob = pMobile.getText().toString();

                if(dname.isEmpty() == true){ pName.setError("Add Name");}
                if(problem.isEmpty()== true){ pProb.setError("Add Problem");}
                if(dmob.isEmpty()== true){ pMobile.setError("Add Mobile");}

                else {

                    prog.setVisibility(View.VISIBLE);

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().bookAppointment(dname,dmob,problem,date,HospitalId);

                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();

                            if (response.code() == 201) {
                                Toast.makeText(getApplicationContext(), "We will Connect you Soon!", Toast.LENGTH_LONG).show();
                                book.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            prog.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

        book.show();
    }


}
