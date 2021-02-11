package com.traidev.asylum;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.traidev.asylum.Utility.DefaultResponse;
import com.traidev.asylum.Utility.Network.RetrofitClient;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.ViewHolder>{


    private List<HospitalViewModal> plist;
    private Context context;
    private int mYear, mMonth, mDay;



    public HospitalsAdapter(List<HospitalViewModal> plist, Context context)
    {
        this.plist = plist;
        this.context = context;

    }

    @NonNull
    @Override
    public HospitalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_hopitals, parent, false);
            return new HospitalsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.Title.setText(plist.get(position).getTitle());
        holder.Rating.setText(plist.get(position).getRating());

        Glide.with(context).load("https://traidev.com/LIVE_APPS/Asylum/con/"+
                plist.get(position).getBanner()).into((holder).Banner);

        final String Id = plist.get(position).getID();

        holder.Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailHospitalActivity.class);
                i.putExtra("key", Id);
                context.startActivity(i);
            }
        });

        holder.en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    BookAppointment(plist.get(position).getTitle(),plist.get(position).getID(),v);
            }
        });

    }

    @Override
    public int getItemCount() {
            return plist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Title,Rating;
        Button en;
        ImageView Banner;

       public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Banner =  itemView.findViewById(R.id.hBanner);
            Title =  itemView.findViewById(R.id.hTitle);
            Rating =  itemView.findViewById(R.id.hRating);
            en = itemView.findViewById(R.id.hEnq);
       }

    }

    public void BookAppointment(String title, final String id,View v)
    {

        final BottomSheetDialog book = new BottomSheetDialog(v.getContext());
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
        hosName.setText(title);

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

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().bookAppointment(dname,dmob,problem,date,id);

                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();

                            if (response.code() == 201) {
                                Toast.makeText(context, "We will Connect you Soon!", Toast.LENGTH_LONG).show();
                                book.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            prog.setVisibility(View.GONE);
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

        book.show();
    }



}
