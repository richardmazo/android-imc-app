package com.rilel.imc_app.repository;

import com.rilel.imc_app.model.Persona;
import com.rilel.imc_app.model.Usuario;
import com.rilel.imc_app.pojo.Autorizacion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IMCRepository {

    @Headers("Content-Type: application/json")
    @POST("auth")
    Call<Usuario> validarUsuario(@Body Usuario usuario);

    @Headers("Cache-Control: no-cache")
    @GET("personas")
    Call<ArrayList<Persona>> readAllPersona(@Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @GET("personas/{id}")
    Call<Persona> readPersona(@Path("id") Long id, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @GET("personas/registros/{id}")
    Call<ArrayList<Persona>> readPersonaRegistros(@Path("id") Long id, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @POST("personas")
    Call<Persona> createPersona(@Body Persona persona, @Header("Authorization") String token);


}
