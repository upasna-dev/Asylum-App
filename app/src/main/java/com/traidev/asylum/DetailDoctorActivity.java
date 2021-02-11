package com.traidev.asylum;

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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.traidev.asylum.Utility.DefaultResponse;
import com.traidev.asylum.Utility.Network.RetrofitClient;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDoctorActivity extends AppCompatActivity {

    String DoctorId = null;
    FrameLayout frameShow;
    CircleImageView profile;
    Button Enq;

    private int mYear, mMonth, mDay;


    TextView name,spec,degree,rating,about,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_detail_activity);

        name = findViewById(R.id.dName);
        rating = findViewById(R.id.dRating);
        degree = findViewById(R.id.dDegree);
        about = findViewById(R.id.dAbout);
        spec = findViewById(R.id.dSpec);
        address = findViewById(R.id.dAddress);
        Enq = findViewById(R.id.en);
        profile = findViewById(R.id.profile);


        ImageView  BackBtn = findViewById(R.id.backClick);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DoctorsActivity.class));
                finish();
            }
        });

        frameShow = findViewById(R.id.showBeforeLoadFrame);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            DoctorId = extras.getString("key");
            fetchSingleDoctor();
        }

        Enq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookAppointment();
            }
        });

 }

    public void fetchSingleDoctor()
    {
        frameShow.setVisibility(View.VISIBLE);
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getDoctor(DoctorId);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201)
                {
                    String data = dr.getMessage();
                    String[] dif = data.split("#");
                    name.setText(dif[0]);
                    about.setText(dif[1]);

                    rating.setText(dif[2]);
                    address.setText(dif[3]);
                    degree.setText(dif[5]);
                    spec.setText(dif[6]);

                    Glide.with(getApplicationContext())
                            .load("https://traidev.com/LIVE_APPS/Asylum/con/doc/"+dif[4]).into((profile));
                    frameShow.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });

    }



    public void BookAppointment()
    {

        final BottomSheetDialog book = new BottomSheetDialog(DetailDoctorActivity.this);
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
        hosName.setText(name.getText().toString());

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

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().bookAppointment(dname,dmob,problem,date,DoctorId);

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
