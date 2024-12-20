package com.challenge.literatura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String titulo;
  @ManyToOne
  @JoinColumn(name = "id_autor")
  private Autor autor;
  private String idiomas;
  private Double numeroDescargas;

  public Libro() {
  }

  public Libro(DatosLibro datosLibro) {
    this.titulo = datosLibro.titulo();
    this.idiomas = datosLibro.lenguajes().get(0).toUpperCase();
    this.autor = new Autor(datosLibro.autor().get(0));
    this.numeroDescargas = datosLibro.numeroDescargas();
  }

  public Libro(Long id, String titulo, Autor autor, String idiomas, Double numeroDescargas) {
    this.id = id;
    this.titulo = titulo;
    this.autor = autor;
    this.idiomas = idiomas;
    this.numeroDescargas = numeroDescargas;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Autor getAutor() {
    return autor;
  }

  public void setAutor(Autor autor) {
    this.autor = autor;
  }

  public String getIdiomas() {
    return idiomas;
  }

  public void setIdiomas(String idiomas) {
    this.idiomas = idiomas;
  }

  public Double getNumeroDescargas() {
    return numeroDescargas;
  }

  public void setNumeroDescargas(Double numeroDescargas) {
    this.numeroDescargas = numeroDescargas;
  }

  @Override
  public String toString() {
    return "----- Libro -----" +
            "\n Titulo: " + titulo +
            "\n Autor: " + autor.getNombre() +
            "\n Idioma: " + idiomas +
            "\n Número de descargas: " + numeroDescargas +
            "\n-----------------\n";
  }
}
