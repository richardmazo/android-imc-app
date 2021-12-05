package com.rilel.imc_app.repository;

import com.rilel.imc_app.model.Usuario;
import com.rilel.imc_app.pojo.Autorizacion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetAuth {

    @Headers("Cache-Control: no-cache")
    @POST("auth")
    Call<Autorizacion> getToken(@Body Usuario usuario);
}
