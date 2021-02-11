package com.traidev.asylum.Utility;

import com.traidev.asylum.DoctorViewModal;
import com.traidev.asylum.HospitalViewModal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Main_Interface {


    @POST("hospitals_list.php")
    Call<DefaultResponse> getSingleHospital(
            @Query("hosId") String key
    );


    @POST("hospitals_list.php")
    Call<DefaultResponse> getServices(
            @Query("hosServices") String key
    );


    @POST("hospitals_list.php")
    Call<List<HospitalViewModal>> getHospitals(@Query("hospitals_list") String all);



    @POST("doctors_list.php")
    Call<List<DoctorViewModal>> getDoctors(
            @Query("doctors_list") String key
    );

    @POST("doctors_list.php")
    Call<DefaultResponse> getDoctor(
            @Query("docId") String key
    );



    @POST("appointment.php")
    Call<DefaultResponse> bookAppointment(
            @Query("pName") String nm,
            @Query("pMob") String mo,
            @Query("pProb") String prob,
            @Query("pdate") String date,
            @Query("hId") String hos
    );



}
