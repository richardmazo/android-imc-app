package com.rilel.imc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rilel.imc_app.model.Persona;
import com.rilel.imc_app.model.Usuario;
import com.rilel.imc_app.network.IMCAPIClient;
import com.rilel.imc_app.pojo.Autorizacion;
import com.rilel.imc_app.repository.IMCRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import java.lang.reflect.Type;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearIMC extends AppCompatActivity {

    EditText editTextPeso, editTextEstatura, editTextEdad, editTextNombre;
    TextView imcTextVW, mBTextVW;
    RadioButton rdbHombre, rdbMujer;
    Button btnCalcularIMC, btnLista;
    ArrayList<Persona> personaArray;
    Double metabolismo_basal, imc;

    IMCRepository repoIMCAutenticacion;

    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_imc);

        Bundle bundle = this.getIntent().getExtras();
        Gson gson = new Gson();
        String Usuario = bundle.getString("Usuario");
        Type typeUsuario = new TypeToken<Usuario>(){}.getType();
        usuario = gson.fromJson(Usuario, typeUsuario);

        editTextPeso = (EditText) findViewById(R.id.idEtPeso);
        editTextEstatura = (EditText) findViewById(R.id.idEtEstatura);
        editTextEdad = (EditText) findViewById(R.id.idEtEdad);
        editTextNombre = (EditText) findViewById(R.id.idEditNombre);
        imcTextVW = (TextView) findViewById(R.id.idTvIMC);
        mBTextVW = (TextView) findViewById(R.id.idTvMb);
        btnCalcularIMC = (Button) findViewById(R.id.btnCalcularIMC);
        btnLista= (Button) findViewById(R.id.btnVer);
        rdbMujer = (RadioButton) findViewById(R.id.mujer);
        rdbHombre = (RadioButton) findViewById(R.id.hombre);

        btnCalcularIMC.setOnClickListener(V -> {
            validarCampos();
        });

        btnLista.setOnClickListener(V -> {
            listar();
        });
    }

    private void registrarDatos() {

        String genero;

        if (rdbHombre.isChecked()) {
            genero = this.getResources().getString(R.string.hombre);
        } else {
            genero = this.getResources().getString(R.string.mujer);
        }


        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateString = simpleDateFormat.format(new Date());


        Persona persona = new Persona(
                usuario.getId(),
                editTextNombre.getText().toString(),
                Double.parseDouble(editTextPeso.getText().toString()),
                Double.parseDouble(editTextEstatura.getText().toString()),
                Integer.parseInt(editTextEdad.getText().toString()),
                genero,
                imc,
                metabolismo_basal,
                dateString
        );

        repoIMCAutenticacion = IMCAPIClient.getInstance().create(IMCRepository.class);
        Call<Persona> callCrearIMC = repoIMCAutenticacion.createPersona(persona, Autorizacion.getAccess());

        callCrearIMC.enqueue(new Callback<Persona>() {
            @Override
            public void onResponse(Call<Persona> call, Response<Persona> response) {
                Log.d("Logs de Retrofit", "Respuesta con Retrofit, creada con exito!");
            }

            @Override
            public void onFailure(Call<Persona> call, Throwable t) {
                Log.d("Logs de Retrofit", "Respuesta con Retrofit, ERROR ERROR createPersona");
            }
        });

        Toast.makeText(this, R.string.usuario_registrado, Toast.LENGTH_SHORT).show();
    }


    private void calcularMetabolismoBalsal() {

        double peso = Double.parseDouble(editTextPeso.getText().toString());
        double estatura = Double.parseDouble(editTextEstatura.getText().toString());
        int edad = Integer.parseInt(editTextEdad.getText().toString());

        if (rdbHombre.isChecked()) {
            metabolismo_basal = (10 * peso) + (6.25 * estatura * 100) - (5 * edad) + 5;
        } else {
            metabolismo_basal = (10 * peso) + (6.25 * estatura * 100) - (5 * edad) - 161;
        }

        mBTextVW.setVisibility(View.VISIBLE);
        mBTextVW.setText(this.getResources().getString(R.string.metalbolismo_basal_resultado) + metabolismo_basal);
    }

    private void calcularIMC() {

        String tabla;
        try {
            double peso = Double.parseDouble(editTextPeso.getText().toString());
            double estatura = Double.parseDouble(editTextEstatura.getText().toString());

            imc = peso / (estatura * estatura);

            if (imc < 18.5) {
                tabla = this.getResources().getString(R.string.bajo_peso);
            } else {
                if (imc >= 18.5 && imc <= 24.9) {
                    tabla = this.getResources().getString(R.string.hombre);
                } else {
                    if (imc >= 25.0 & imc <= 29.9) {
                        tabla = this.getResources().getString(R.string.sobrepeso);
                    } else {
                        tabla = this.getResources().getString(R.string.obeso);
                    }
                }
            }

            imcTextVW.setVisibility(View.VISIBLE);
            imcTextVW.setText(this.getResources().getString(R.string.imc_resultado) + imc + " - " + tabla);

        } catch (Exception e) {
            Toast.makeText(this, R.string.ingrese_bien_los_valores, Toast.LENGTH_SHORT).show();
        }

    }

    private void validarCampos() {

        if (editTextNombre.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.escriba_el_nombre, Toast.LENGTH_SHORT).show();
            editTextNombre.requestFocus();
            return;
        }
        if (editTextPeso.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.escriba_el_peso, Toast.LENGTH_SHORT).show();
            editTextPeso.requestFocus();
            return;
        }
        if (editTextEstatura.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.escriba_la_estatura, Toast.LENGTH_SHORT).show();
            editTextEstatura.requestFocus();
            return;
        }
        if (editTextEdad.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.escriba_la_edad, Toast.LENGTH_SHORT).show();
            editTextEdad.requestFocus();
            return;
        }
        if (!rdbHombre.isChecked() && !rdbMujer.isChecked()) {
            Toast.makeText(this, R.string.seleccione_el_genero, Toast.LENGTH_SHORT).show();
            return;
        }
        calcularIMC();
        calcularMetabolismoBalsal();
        registrarDatos();

    }

    private void listarIMC(ArrayList<Persona> personaArray) {
        Gson gson = new Gson();
        String arrayStringPersonas = gson.toJson(personaArray);
        String usuario = gson.toJson(this.usuario);
        Intent intent = new Intent(CrearIMC.this, PersonListActivity.class);
        Bundle extras = new Bundle();
        extras.putString("Personas",arrayStringPersonas);
        extras.putString("Usuario",usuario);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    private void listar() {

        if(usuario.getRol().equals("comunidad")){
            Call<ArrayList<Persona>> callReadPersona = repoIMCAutenticacion.readPersonaRegistros(usuario.getId(),usuario.getAccess());
            callReadPersona.enqueue(new Callback<ArrayList<Persona>>() {
                @Override
                public void onResponse(Call<ArrayList<Persona>> call, Response<ArrayList<Persona>> response) {
                    personaArray = response.body();;
                    Log.d("Logs de Retrofit", "Respuesta con Retrofit, readPersonaRegistros " + personaArray);
                    listarIMC(personaArray);
                    /*Gson gson = new Gson();
                    String arrayStringPersonas = gson.toJson(personaArray);
                    Intent intent = new Intent(CrearIMC.this, PersonListActivity.class);
                    intent.putExtra("ArrayListPersonas",arrayStringPersonas);
                    startActivity(intent);*/

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
                        /*Gson gson = new Gson();
                        String arrayStringPersonas = gson.toJson(personaArray);
                        Intent intent = new Intent(CrearIMC.this, PersonListActivity.class);
                        intent.putExtra("ArrayListPersonas",arrayStringPersonas);
                        startActivity(intent);*/
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Persona>> call, Throwable t) {
                        Log.d("Logs de Retrofit", "Respuesta con Retrofit, ERROR ERROR callIMCAutenticacion");
                    }
                });
            }
        }

    }
}