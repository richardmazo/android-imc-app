package com.rilel.imc_app.repository;

import com.rilel.imc_app.model.Persona;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PersonaRepository {

    @GET("personas")
    Call<List<Persona>> readAllPersona(@Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @GET("personas/{id}")
    Call<Persona> readPersona(@Path("id") Long id, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @POST("personas")
    Call<Persona> createPersona(@Body Persona persona, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @PUT("personas/{id}")
    Call<Persona> updatePersona(@Path("id") Long id, @Body Persona persona, @Header("Authorization") String token);

    @DELETE("personas/{id}")
    Call<String> deletePersona(@Path("id") Long id, @Header("Authorization") String token);
}
