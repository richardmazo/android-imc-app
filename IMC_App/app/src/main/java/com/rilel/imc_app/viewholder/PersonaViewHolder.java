package com.rilel.imc_app.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rilel.imc_app.R;

public class PersonaViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewNombre;
    private TextView textViewPeso;
    private TextView textViewEstatura;
    private TextView textViewEdad;
    private TextView textViewGenero;
    private TextView textViewImc;
    private TextView textViewMetabolismoBasal;
    private TextView textViewDate;

    public PersonaViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewNombre = (TextView) itemView.findViewById(R.id.txt_nombre);
        textViewPeso = (TextView) itemView.findViewById(R.id.txt_peso);
        textViewEstatura = (TextView) itemView.findViewById(R.id.txt_estatura);
        textViewEdad = (TextView) itemView.findViewById(R.id.txt_edad);
        textViewGenero = (TextView) itemView.findViewById(R.id.txt_genero);
        textViewImc = (TextView) itemView.findViewById(R.id.txt_imc);
        textViewMetabolismoBasal = (TextView) itemView.findViewById(R.id.txt_metabolismo_basal);
        textViewDate = (TextView) itemView.findViewById(R.id.txt_date);
    }

    public TextView getTextViewNombre() {
        return textViewNombre;
    }

    public TextView getTextViewPeso() {
        return textViewPeso;
    }

    public TextView getTextViewEstatura() {
        return textViewEstatura;
    }


    public TextView getTextViewEdad() {
        return textViewEdad;
    }

    public TextView getTextViewGenero() {
        return textViewGenero;
    }

    public TextView getTextViewImc() {
        return textViewImc;
    }

    public TextView getTextViewMetabolismoBasal() {
        return textViewMetabolismoBasal;
    }

    public TextView getTextViewDate() {
        return textViewDate;
    }

}
