package com.rilel.imc_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rilel.imc_app.adapter.PersonaAdapter;
import com.rilel.imc_app.model.Persona;
import com.rilel.imc_app.model.Usuario;
import com.rilel.imc_app.repository.LocationFirebase;
import com.rilel.imc_app.repository.LocationFirebaseImpl;
import com.rilel.imc_app.utils.Callback;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PersonListActivity extends AppCompatActivity {

    ArrayList<Persona> personaArray;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        Button btnCrearNuevoRegistro = (Button) findViewById(R.id.btnCrearNuevoRegistro);
        Button btnVerMapa = (Button) findViewById(R.id.btnVerUbicaciones);

        Bundle bundle = this.getIntent().getExtras();
        Gson gson = new Gson();
        String ArrayListPersonas = bundle.getString("Personas");
        String Usuario = bundle.getString("Usuario");
        Type typePersona = new TypeToken<ArrayList<Persona>>(){}.getType();
        Type typeUsuario = new TypeToken<Usuario>(){}.getType();
        personaArray = gson.fromJson(ArrayListPersonas, typePersona);
        usuario = gson.fromJson(Usuario, typeUsuario);


        PersonaAdapter personaAdapter = new PersonaAdapter(this, R.layout.persona_recycler_adapter, personaArray);
        RecyclerView recyclerViewPersonas = (RecyclerView) findViewById(R.id.lista_personas);
        recyclerViewPersonas.setAdapter(personaAdapter);
        recyclerViewPersonas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPersonas.setHasFixedSize(true);

        btnVerMapa.setOnClickListener(v -> {
            Gson gsonSend = new Gson();
            String usuario = gsonSend.toJson(this.usuario);
            Intent intent = new Intent(PersonListActivity.this, LocationActivity.class);
            Bundle extras = new Bundle();
            extras.putString("Usuario",usuario);
            intent.putExtras(extras);
            startActivity(intent);
        });

        btnCrearNuevoRegistro.setOnClickListener(v -> {
            Gson gsonSend = new Gson();
            String usuario = gsonSend.toJson(this.usuario);
            Intent intent = new Intent(PersonListActivity.this, CrearIMC.class);
            Bundle extras = new Bundle();
            extras.putString("Usuario",usuario);
            intent.putExtras(extras);
            startActivity(intent);
        });



    }
}