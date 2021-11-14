package com.rilel.imc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rilel.imc_app.model.Persona;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText editTextPeso, editTextEstatura, editTextEdad, editTextNombre;
    TextView imcTextVW, mBTextVW;
    RadioButton rdbHombre, rdbMujer;
    Button btnCalcularIMC, btnLista;
    ArrayList<Persona> personaArray;
    Double metabolismo_basal, imc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        personaArray = new ArrayList<Persona>();

        btnCalcularIMC.setOnClickListener(V -> {
            validarCampos();
        });

        btnLista.setOnClickListener(V -> {
            listar();
        });

    }

    private void listar() {

        if (personaArray.isEmpty()) {
            Toast.makeText(this, "Aún no hay usuarios registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        String arrayStringPersonas = gson.toJson(personaArray);
        Intent intent = new Intent(MainActivity.this, PersonListActivity.class);
        intent.putExtra("ArrayListPersonas",arrayStringPersonas);
        startActivity(intent);

    }


    private void validarCampos() {

        if (editTextNombre.getText().toString().isEmpty()) {
            Toast.makeText(this, "Escribir el nombre", Toast.LENGTH_SHORT).show();
            editTextNombre.requestFocus();
            return;
        }
        if (editTextPeso.getText().toString().isEmpty()) {
            Toast.makeText(this, "Escribir el peso", Toast.LENGTH_SHORT).show();
            editTextPeso.requestFocus();
            return;
        }
        if (editTextEstatura.getText().toString().isEmpty()) {
            Toast.makeText(this, "Escribir la estatura", Toast.LENGTH_SHORT).show();
            editTextEstatura.requestFocus();
            return;
        }
        if (editTextEdad.getText().toString().isEmpty()) {
            Toast.makeText(this, "Escribir la edad", Toast.LENGTH_SHORT).show();
            editTextEdad.requestFocus();
            return;
        }
        if (!rdbHombre.isChecked() && !rdbMujer.isChecked()) {
            Toast.makeText(this, "Seleccione el genero", Toast.LENGTH_SHORT).show();
            return;
        }
        calcularIMC();
        calcularMetabolismoBalsal();
        registrarDatos();
    }

    private void registrarDatos() {

        String genero;

        if (rdbHombre.isChecked()) {
            genero = "Hombre";
        } else {
            genero = "Mujer";
        }

        Calendar calendar = Calendar.getInstance();
        Date dateObj = calendar.getTime();

        personaArray.add(new Persona(editTextNombre.getText().toString(),
                Double.parseDouble(editTextPeso.getText().toString()),
                Double.parseDouble(editTextEstatura.getText().toString()),
                Integer.parseInt(editTextEdad.getText().toString()),
                genero,
                imc,
                metabolismo_basal,
                dateObj
                ));

        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();
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
        mBTextVW.setText("Metabolismo basal: " + metabolismo_basal);
    }

    private void calcularIMC() {

        String tabla;
        try {
            double peso = Double.parseDouble(editTextPeso.getText().toString());
            double estatura = Double.parseDouble(editTextEstatura.getText().toString());

            imc = peso / (estatura * estatura);

            if (imc < 18.5) {
                tabla = "Bajo peso";
            } else {
                if (imc >= 18.5 && imc <= 24.9) {
                    tabla = "Normal";
                } else {
                    if (imc >= 25.0 & imc <= 29.9) {
                        tabla = "Sobrepeso";
                    } else {
                        tabla = "Obeso";
                    }
                }
            }

            imcTextVW.setVisibility(View.VISIBLE);
            imcTextVW.setText("IMC: " + imc + " - " + tabla);

        } catch (Exception e) {
            Toast.makeText(this, R.string.ingrese_bien_los_valores, Toast.LENGTH_SHORT).show();
        }

    }
}