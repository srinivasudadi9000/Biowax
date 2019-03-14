package com.srinivas.rest;

import com.srinivas.Models.UploadInstall;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public abstract class ApiInterface {


    @Multipart
    @POST("index.php?r=restapi/user/upload-installation-image")
    public Call<UploadInstall> getUploadInstall(@Query("installation_date") String installation_date,
                                                @Query("installation_remarks") String installation_remarks,
                                                @Part("key") RequestBody key,
                                                @Part("user_id") RequestBody user_id, @Part("crew_person_id") RequestBody crew_person_id,
                                                @Part("recce_id") RequestBody recce_id, @Part("project_id") RequestBody project_id,
                                                @Part MultipartBody.Part installation_image, @Part MultipartBody.Part installation_image_1,
                                                @Part MultipartBody.Part installation_image_2
    ) {
        return null;
    }


}
