package com.rilel.imc_app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rilel.imc_app.R;
import com.rilel.imc_app.model.Persona;
import com.rilel.imc_app.viewholder.PersonaViewHolder;

import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaViewHolder> {
    Context context;
    int layout;
    List<Persona> personaList;
    LayoutInflater layoutInflater;

    public PersonaAdapter(Context context, int layout, List<Persona> personaList) {
        this.context = context;
        this.layout = layout;
        this.personaList = personaList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PersonaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = layoutInflater.inflate(layout,parent,false);
        return new PersonaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonaViewHolder holder, int position) {
        Persona persona = personaList.get(position);

        holder.getTextViewNombre().setText("Nombre: " + persona.getNombre());
        holder.getTextViewPeso().setText("Peso: " + persona.getPeso()+"");
        holder.getTextViewEstatura().setText("Estatura: " + persona.getEstatura()+"");
        holder.getTextViewEdad().setText("Edad: " + persona.getEdad()+"");
        holder.getTextViewGenero().setText("Genero: " + persona.getGenero());
        holder.getTextViewImc().setText("IMC: " + persona.getImc()+"");
        holder.getTextViewMetabolismoBasal().setText("Metabolismo basal: " + persona.getMetabolismoBasal()+"");
        holder.getTextViewDate().setText("Fecha registro: " + persona.getFechaRegistro()+"");
    }

    @Override
    public int getItemCount() {
        return personaList.size();
    }
}
