package com.rilel.imc_app.model;

import java.io.Serializable;
import java.util.HashMap;

public class Ubicacion implements Serializable {

    private Long idUsuario;
    private Double latitud;
    private Double longitud;
    private String hora;

    public Ubicacion(Long idUsuario, Double latitud, Double longitud, String hora) {
        this.idUsuario = idUsuario;
        this.latitud = latitud;
        this.longitud = longitud;
        this.hora = hora;
    }

    public Ubicacion() {
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getReference(){
        return this.getIdUsuario()+"-"+this.getHora();
    }

    public HashMap<String, Object> getMap(){

        HashMap<String, Object> map = new HashMap();
        map.put("idUsuario", this.getIdUsuario());
        map.put("latitud", this.getLatitud());
        map.put("longitud",this.getLongitud());
        map.put("longitud",this.getLongitud());
        map.put("hora",this.getHora());

        return map;

    }

}
