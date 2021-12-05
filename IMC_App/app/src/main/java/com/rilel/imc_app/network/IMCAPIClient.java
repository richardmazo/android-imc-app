package com.rilel.imc_app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IMCAPIClient {

    //private static String BASE_URL_API_AUTH = "http://192.168.1.66:8080/api/";
    private static String BASE_URL_API_AUTH = "http://132.226.208.196:8080/demo/api/";

    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_API_AUTH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
