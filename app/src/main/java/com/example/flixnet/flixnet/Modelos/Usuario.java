package com.example.flixnet.flixnet.Modelos;


public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String alias;
    private String imagen;

    public Usuario(){ }

    public Usuario(int idUsuario, String apellidos, String email, String nombre, String telefono){
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
