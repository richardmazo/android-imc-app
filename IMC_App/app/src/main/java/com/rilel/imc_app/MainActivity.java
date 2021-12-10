package com.rilel.imc_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.rilel.imc_app.model.Persona;
import com.rilel.imc_app.model.Ubicacion;
import com.rilel.imc_app.model.Usuario;
import com.rilel.imc_app.network.IMCAPIClient;
import com.rilel.imc_app.pojo.Autorizacion;
import com.rilel.imc_app.repository.IMCRepository;
import com.rilel.imc_app.repository.LocationFirebase;
import com.rilel.imc_app.repository.LocationFirebaseImpl;
import com.rilel.imc_app.utils.GpsUtils;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText editTextEmail, editTextPassword;

    IMCRepository repoIMCAutenticacion;

    ArrayList<Persona> personaArray;
    Usuario usuario;

    LocationFirebase locationFirebase;
    private FusedLocationProviderClient fusedLocationClient;

    private static  final String[] LOCATION_PERMS ={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int LOCATION_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.txt_email);
        editTextPassword = (EditText) findViewById(R.id.txt_clave);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(V -> {
            validarCampos();
            validarUsuario();
        });

    }

    private void validarUsuario() {

        usuario = new Usuario();

        usuario.setUsername(editTextEmail.getText().toString());
        usuario.setPassword(editTextPassword.getText().toString());

        repoIMCAutenticacion = IMCAPIClient.getInstance().create(IMCRepository.class);
        Call<Usuario> callIMCAutenticacion = repoIMCAutenticacion.validarUsuario(usuario);


        callIMCAutenticacion.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                usuario = response.body();
                Log.d("Logs de Retrofit", "Respuesta con Retrofit, " + usuario.getAccess());
                Autorizacion autorizacion = new Autorizacion();
                autorizacion.setAccess(usuario.getAccess());
                guardarUbicacion();
                listar();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                mensaje("Usuario o contraseña incorrecto");
                Log.d("Logs de Retrofit", "Respuesta con Retrofit, ERROR ERROR ERROR");
            }
        });
    }

    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        try {
            wait(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void listar() {
        if(usuario.getRol().equals("comunidad")){
            Call<ArrayList<Persona>> callReadPersona = repoIMCAutenticacion.readPersonaRegistros(usuario.getId(),usuario.getAccess());
            callReadPersona.enqueue(new Callback<ArrayList<Persona>>() {
                @Override
                public void onResponse(Call<ArrayList<Persona>> call, Response<ArrayList<Persona>> response) {
                    personaArray = response.body();
                    Log.d("Logs de Retrofit", "Respuesta con Retrofit, readPersonaRegistros " + personaArray);
                    listarIMC(personaArray);
                }

                @Override
                public void onFailure(Call<ArrayList<Persona>> call, Throwable t) {
                    Log.d("Logs de Retrofit", "Respuesta con Retrofit, ERROR ERROR callIMCAutenticacion");
                }
            });
        }else{
            if(usuario.getRol().equals("bienestar")){
                    Call<ArrayList<Persona>> callIMCAutenticacion = repoIMCAutenticacion.readAllPersona(usuario.getAccess());
                    callIMCAutenticacion.enqueue(new Callback<ArrayList<Persona>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Persona>> call, Response<ArrayList<Persona>> response) {
                            personaArray = response.body();
                            listarIMC(personaArray);
                            Log.d("Logs de Retrofit", "Respuesta con Retrofit, readAllPersona " + personaArray);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Persona>> call, Throwable t) {
                            Log.d("Logs de Retrofit", "Respuesta con Retrofit, ERROR ERROR callIMCAutenticacion");
                        }
                    });
            }
        }

    }

    private void listarIMC(ArrayList<Persona> personaArray) {
        Gson gson = new Gson();
        String arrayStringPersonas = gson.toJson(personaArray);
        String usuario = gson.toJson(this.usuario);
        Intent intent = new Intent(MainActivity.this, PersonListActivity.class);
        Bundle extras = new Bundle();
        extras.putString("Personas",arrayStringPersonas);
        extras.putString("Usuario",usuario);
        intent.putExtras(extras);
        startActivity(intent);
    }


    private void validarCampos() {

        if (editTextEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite el correo", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return;
        }
        if (editTextPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite la contraseña", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            return;
        }
    }

    private void guardarUbicacion() {
        locationFirebase = new LocationFirebaseImpl();
        com.rilel.imc_app.utils.Callback callbackRegistroUbicacion = new com.rilel.imc_app.utils.Callback() {
            @Override
            public void OnSuccess(Object object) {

            }

            @Override
            public void OnFailure(Object object) {

            }
        };

        obtenerUbicacion(callbackRegistroUbicacion);
    }

    private void obtenerUbicacion(com.rilel.imc_app.utils.Callback callbackRegistroUbicacion) {

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);

        }else{

            (new GpsUtils(MainActivity.this)).turnGPSOn(new GpsUtils.onGpsListener(){
                @Override
                public void gpsStatus(boolean isGPSEnable){

                }
            });


            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                double latitude, longitude;
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                //LatLng punto = new LatLng(location.getLatitude(), location.getLongitude());

                                Calendar calendario = Calendar.getInstance();
                                int hora, minutos, segundos;

                                hora = calendario.get(Calendar.HOUR_OF_DAY);
                                minutos = calendario.get(Calendar.MINUTE);
                                segundos = calendario.get(Calendar.SECOND);



                                Ubicacion ubicacion = new Ubicacion(usuario.getId(),latitude,longitude,hora + ":" + minutos + ":" + segundos);
                                locationFirebase.crear(ubicacion, callbackRegistroUbicacion);

                            }
                        }
                    });
        }
    }

}