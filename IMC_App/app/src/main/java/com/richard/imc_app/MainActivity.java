package com.richard.imc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextPeso, editTextEstatura, editTextEdad;
    TextView imcTextVW, mBTextVW;
    RadioButton rdbHombre, rdbMujer;
    Button btnCalcularIMC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPeso = (EditText) findViewById(R.id.idEtPeso);
        editTextEstatura = (EditText) findViewById(R.id.idEtEstatura);
        editTextEdad = (EditText) findViewById(R.id.idEtEdad);
        imcTextVW = (TextView) findViewById(R.id.idTvIMC);
        mBTextVW = (TextView) findViewById(R.id.idTvMb);
        btnCalcularIMC = (Button) findViewById(R.id.btnCalcularIMC);
        rdbMujer = (RadioButton) findViewById(R.id.mujer);
        rdbHombre = (RadioButton) findViewById(R.id.hombre);

        btnCalcularIMC.setOnClickListener(V -> {
            validarCampos();
        });

    }


    private void validarCampos() {

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
    }

    private void calcularMetabolismoBalsal() {


        double metabolismo_basal;
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

        double imc;
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