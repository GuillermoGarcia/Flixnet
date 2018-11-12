package com.example.flixnet.flixnet.Modelos;

/**
 * Programación multimedia y dispositivos móviles
 * @author Antonio J.Sánchez
 * @year 2018/19
 *
 */

public class Pelicula {

  private String idMDB ;
  private String titulo ;
  private String poster ;
  private String sinopsis ;
  private String estreno ;
  private String nota ;

  /**
   * @param idMDB
   * @param titulo
   * @param poster
   * @param sinopsis
   * @param estreno
   * @param nota
   */
  public Pelicula(String idMDB, String titulo,
                  String poster, String sinopsis, String estreno, String nota) {

    this.idMDB = idMDB;
    this.estreno = estreno;
    this.nota = nota;
    this.poster = poster;
    this.sinopsis = sinopsis;
    this.titulo = titulo;
  }

  /**
   * Devuelve el id de MDB de la película
   * @return idMDB
   */
  public String getIdMDB() { return idMDB; }

  /**
   * Devuelve la fecha de estreno de la película
   * @return estreno
   */
  public String getEstreno() { return estreno; }

  /**
   * Devuelve la Nota de la película
   * @return nota
   */
  public float getNota() { return (float) (0.5 * Double.valueOf(nota)); }

  /**
   * Devuelve el nombre del poster/imagen de la película
   * @return poster
   */
  public String getPoster() { return poster; }

  /**
   * Devuelve la sinopsis de la película
   * @return sinopsis
   */
  public String getSinopsis() { return sinopsis; }

  /**
   * Devuelve el titulo de la película
   * @return titulo
   */
  public String getTitulo() { return titulo; }

  /**
   * Asigna o cambia la fecha de estreno de la película
   * @param estreno
   */
  public void setEstreno(String estreno) { this.estreno = estreno; }

  /**
   * Asigna o cambia la nota de la película
   * @param nota
   */
  public void setNota(String nota) { this.nota = nota; }

  /**
   * Asigna o cambia el poster/imagen de la película
   * @param poster
   */
  public void setPoster(String poster) { this.poster = poster; }

  /**
   * Asigna o cambia la sinopsis de la película
   * @param sinopsis
   */
  public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

  /**
   * Asigna o cambia el titulo de la película
   * @param titulo
   */
  public void setTitulo(String titulo) { this.titulo = titulo; }

  @Override
  public String toString() { return "Título: " + this.titulo + " - Nota: " + this.nota; }
}
