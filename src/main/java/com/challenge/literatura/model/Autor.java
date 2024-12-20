package com.challenge.literatura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  private String fechaNacimiento;
  private String fechaFallecimiento;
  @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Libro> libros;

  public Autor() {
  }

  public Autor(DatosAutor datosAutor){
    this.nombre = datosAutor.nombre();
    this.fechaNacimiento = datosAutor.fechaNacimiento();
    this.fechaFallecimiento = datosAutor.fechaFallecimiento();
  }

  public Autor(Long id, String nombre, String fechaNacimiento, String fechaFallecimiento, List<Libro> libros) {
    this.id = id;
    this.nombre = nombre;
    this.fechaNacimiento = fechaNacimiento;
    this.fechaFallecimiento = fechaFallecimiento;
    this.libros = libros;
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

  public String getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public String getFechaFallecimiento() {
    return fechaFallecimiento;
  }

  public void setFechaFallecimiento(String fechaFallecimiento) {
    this.fechaFallecimiento = fechaFallecimiento;
  }

  public List<Libro> getLibros() {
    return libros;
  }

  public void setLibros(List<Libro> libros) {
    this.libros = libros;
  }

  @Override
  public String toString() {
    return "----- Autor -----" +
            "\n Nombre: " + nombre +
            "\n Fecha de Nacimiento: " + fechaNacimiento +
            "\n Fecha de Fallecimiento: " + fechaFallecimiento +
            "\n Libros: " + libros.stream().map(b -> b.getTitulo()).collect(Collectors.toList()) +
            "\n---------------\n";
  }
}
