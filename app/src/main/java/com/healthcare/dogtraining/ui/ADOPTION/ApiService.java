package com.healthcare.dogtraining.ui.ADOPTION;

import com.healthcare.dogtraining.API.Base_Url;
import com.healthcare.dogtraining.Adapter.GetAddressAdapter;
import com.healthcare.dogtraining.Model.GetAllDataModel;
import com.healthcare.dogtraining.Model.GetAllPlanListModel;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {


    @Multipart
    @POST(Base_Url.addAdoption)
    Call<ResponseMessage> AdoptionForm(

            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody name,
            @Part("age") RequestBody age,
            @Part("gender") RequestBody gender,
            @Part("breed") RequestBody breed,
            @Part("weight") RequestBody weight,
            @Part("description") RequestBody description,
            @Part("address") RequestBody address,
            @Part("mobile") RequestBody mobile,

            @Part MultipartBody.Part[] surveyBody

    );



    @FormUrlEncoded
    @POST(Base_Url.get_command_list)
    Call<GetAllDataModel> GetCommandList(
            @Field("user_id") String user_id,
            @Field("course_trining_id") String course_trining_id);

    @FormUrlEncoded
    @POST(Base_Url.plan_command_list)
    Call<GetAllPlanListModel> PlanCommandList(
            @Field("user_id") String user_id);

}
