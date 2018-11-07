package com.example.flixnet.flixnet.Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lista {

  @Expose @SerializedName("idLista") private Integer idLista;
  @Expose @SerializedName("nombre") private String nombre = "";
  @Expose @SerializedName("peliculas") private List<Pelicula> peliculas = null;

  /**
   * Contructor Clase Lista con solo el identificador de la Lista
   * @param id
   */
  public Lista(int id) { this.idLista = id; }

  /**
   * Constructor Clase Lista con todos los parametros
   * @param idLista
   * @param nombre
   * @param peliculas
   */
  public Lista(Integer idLista, String nombre, List<Pelicula> peliculas) {
    this.idLista = idLista;
    this.nombre = nombre;
    this.peliculas = peliculas;
  }

  /**
   * Devuelve el identificador de la Lista
   * @return idLista
   */
  public Integer getIdLista() { return idLista; }

  /**
   * Devuelve el nombre de la Lista
   * @return nombre
   */
  public String getNombre() { return nombre; }

  /**
   * Devuelve la lista de películas que estan en la lista
   * @return peliculas
   */
  public List<Pelicula> getPeliculas() { return peliculas; }
  /**
   * Asignar o cambiar el identificador de la Lista
   * @param idLista
   */
  public void setIdLista(Integer idLista) { this.idLista = idLista; }

  /**
   * Asignar o cambiar el nombre de la lista
   * @param nombre
   */
  public void setNombre(String nombre) { this.nombre = nombre; }

  /**
   * Asignar o cambiar la lista de películas de la lista
   * @param peliculas
   */
  public void setPeliculas(List<Pelicula> peliculas) { this.peliculas = peliculas; }
}
