package com.rilel.imc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rilel.imc_app.model.Persona;
import com.rilel.imc_app.model.Usuario;
import com.rilel.imc_app.network.IMCAPIClient;
import com.rilel.imc_app.pojo.Autorizacion;
import com.rilel.imc_app.repository.IMCRepository;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText editTextEmail, editTextPassword;

    IMCRepository repoIMCAutenticacion;

    ArrayList<Persona> personaArray;
    Usuario usuario;

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
                listar();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("Logs de Retrofit", "Respuesta con Retrofit, ERROR ERROR ERROR");
            }
        });
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
}