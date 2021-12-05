package com.rilel.imc_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Persona {
    @SerializedName("id")
    private Long id;
    @SerializedName("idRegistros")
    private Long idRegistros;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("peso")
    private Double peso;
    @SerializedName("estatura")
    private Double estatura;
    @SerializedName("edad")
    private Integer edad;
    @SerializedName("genero")
    private String genero;
    @SerializedName("imc")
    private Double imc;
    @SerializedName("metabolismoBasal")
    private Double metabolismoBasal;
    @SerializedName("fechaRegistro")
    private String fechaRegistro;

    public Persona() {
    }

    public Persona(Long idRegistros, String nombre, Double peso, Double estatura, Integer edad, String genero, Double imc, Double metabolismoBasal, String fechaRegistro) {
        this.idRegistros = idRegistros;
        this.nombre = nombre;
        this.peso = peso;
        this.estatura = estatura;
        this.edad = edad;
        this.genero = genero;
        this.imc = imc;
        this.metabolismoBasal = metabolismoBasal;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getEstatura() {
        return estatura;
    }

    public void setEstatura(Double estatura) {
        this.estatura = estatura;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public Double getMetabolismoBasal() {
        return metabolismoBasal;
    }

    public void setMetabolismoBasal(Double metabolismoBasal) {
        this.metabolismoBasal = metabolismoBasal;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Long getIdRegistros() {
        return idRegistros;
    }

    public void setIdRegistros(Long idRegistros) {
        this.idRegistros = idRegistros;
    }
}
