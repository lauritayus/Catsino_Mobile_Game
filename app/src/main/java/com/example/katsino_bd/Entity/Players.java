package com.example.katsino_bd.Entity;

import java.io.Serializable;

public class Players implements Serializable {

    private Integer id;
    private String nombre;
    private String cantidad;
    private String correo;
    private String foto;

    public Players(Integer id, String nombre, String cantidad, String correo, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.correo = correo;
        this.foto = foto;
    }

    public Players() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}