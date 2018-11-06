package com.example.flixnet.flixnet.Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Lista {

  @Expose @SerializedName("idLista") private Integer idLista;
  @Expose @SerializedName("nombre") private String nombre;
  @Expose @SerializedName("peliculas") private Map<String, String> peliculas;

  public Lista() { }

  public Integer getIdLista() {
    return idLista;
  }

  public void setIdLista(Integer idLista) {
    this.idLista = idLista;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Map<String, String> getPeliculas() {
    return peliculas;
  }

  public void setPeliculas(Map<String, String> peliculas) {
    this.peliculas = peliculas;
  }
}
