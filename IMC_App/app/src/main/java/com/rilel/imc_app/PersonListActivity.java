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

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PersonListActivity extends AppCompatActivity {

    ArrayList<Persona> personaArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        Button regresar = (Button) findViewById(R.id.btnRegresar);

        Bundle bundle = this.getIntent().getExtras();
        Gson gson = new Gson();
        String ArrayListPersonas = bundle.getString("ArrayListPersonas");
        Type type = new TypeToken<ArrayList<Persona>>(){}.getType();
        personaArray = gson.fromJson(ArrayListPersonas, type);

        PersonaAdapter personaAdapter = new PersonaAdapter(this, R.layout.persona_recycler_adapter, personaArray);
        RecyclerView recyclerViewPersonas = (RecyclerView) findViewById(R.id.lista_personas);
        recyclerViewPersonas.setAdapter(personaAdapter);
        recyclerViewPersonas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPersonas.setHasFixedSize(true);

        regresar.setOnClickListener(v -> {
             Intent intent = new Intent(PersonListActivity.this, MainActivity.class);
             startActivity(intent);
             finish();
        });

    }
}