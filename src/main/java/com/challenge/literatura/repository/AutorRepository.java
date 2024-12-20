package com.challenge.literatura.repository;

import com.challenge.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
  Autor findAutorByNombre(String name);
  @Query("SELECT a FROM Autor a WHERE :year >= a.fechaNacimiento AND :year <= a.fechaFallecimiento")
  List<Autor> buscarAutoresVivos(String year);
}
