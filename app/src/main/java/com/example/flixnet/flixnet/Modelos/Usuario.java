package com.example.flixnet.flixnet.Modelos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Usuario implements Serializable {

    @Expose @SerializedName("idUsuario") private String idUsuario;
    @Expose @SerializedName("nombre") private String nombre;
    @Expose @SerializedName("apellidos") private String apellidos;
    @Expose @SerializedName("email") private String email;
    @Expose @SerializedName("telefono") private String telefono;
    @Expose @SerializedName("alias") private String alias;
    @Expose @SerializedName("imagen") private String imagen;

    public Usuario(){ }

    public Usuario(String idUsuario, String nombre, String apellidos, String email, String telefono){
        this.apellidos = apellidos;
        this.email = email;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getAlias() {
        return alias;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) { this.telefono = telefono; }
}
