package com.rilel.imc_app.model;

import java.util.Date;

public class Persona {

    private String nombre;
    private Double peso;
    private Double estatura;
    private Integer edad;
    private String genero;
    private Double imc;
    private Double metabolismoBasal;
    private Date fechaRegistro;

    public Persona() {
    }

    public Persona(String nombre, Double peso, Double estatura, Integer edad, String genero, Double imc, Double metabolismoBasal, Date fechaRegistro) {
        this.nombre = nombre;
        this.peso = peso;
        this.estatura = estatura;
        this.edad = edad;
        this.genero = genero;
        this.imc = imc;
        this.metabolismoBasal = metabolismoBasal;
        this.fechaRegistro = fechaRegistro;
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
