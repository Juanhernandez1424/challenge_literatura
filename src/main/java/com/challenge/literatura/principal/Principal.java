package com.challenge.literatura.principal;

import com.challenge.literatura.model.Autor;
import com.challenge.literatura.model.Datos;
import com.challenge.literatura.model.DatosAutor;
import com.challenge.literatura.model.Libro;
import com.challenge.literatura.repository.AutorRepository;
import com.challenge.literatura.repository.LibroRepository;
import com.challenge.literatura.service.ConsumoApi;
import com.challenge.literatura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
  private Scanner sc = new Scanner(System.in);
  private ConsumoApi consumoApi = new ConsumoApi();
  private ConvierteDatos convierteDatos = new ConvierteDatos();
  private static final String API_URL = "https://gutendex.com/books/";

  private LibroRepository libroRepository;
  private AutorRepository autorRepository;
  public Principal(LibroRepository libroRepository, AutorRepository autorRepository){
    this.libroRepository = libroRepository;
    this.autorRepository = autorRepository;
  }

  private int opc = -1;

  public void mostrarMenu(){
    while(opc != 0){
      System.out.println("---------- Menú ---------- ");
      System.out.println("""
                    1. Buscar libro por título
                    2. Listar libros registrados
                    3. Buscar autor por nombre
                    4. Listar autores registrados
                    5. Listar autores vivos en un determinado año
                    6. Listar libros por idioma
                    7. Top 10 libros más descargados
                    0. Salir
                    """);

      try{
        opc = Integer.parseInt(sc.nextLine());

        switch(opc){
          case 1:
            getBook();
            break;
          case 2:
            System.out.println("----- Libros Registrados -----\n");
            listarLibros();
            break;
          case 3:
            buscarAutorPorNombre();
            break;
          case 4:
            System.out.println("----- Autores Registrados -----\n");
            litsadoAutores();
            break;
          case 5:
            buscarPorAutoresVivos();
            break;
          case 6:
            buscarLibroPorIdioma();
            break;
          case 7:
            System.out.println("----- Top 10 libros más descargados -----\n");
            top10Libros();
            break;
          case 0:
            System.out.println("Gracias por usar LiterAlura!\n");
            break;
          default:
            System.out.println("Opción elegida incorrecta. Elija nuevamente.\n");
        }
      }catch(NumberFormatException e){
        System.out.println("Debes seleccionar un número.");
      }
    }
  }


  // Obtener y guardar datos de libro en base de datos
  private void getBook() {
    System.out.println("Escriba el nombre del libro: ");
    String nombreLibro = sc.nextLine();

    String json = consumoApi.obtenerDatos(API_URL + "?search=" + nombreLibro.replace(" ", "+"));
    Datos datos = convierteDatos.obtenerDatos(json, Datos.class);

    Optional<Libro> libros = datos.resultados().stream()
            .findFirst()
            .map(l -> new Libro(l));

    if (libros.isPresent()) {
      Libro libro = libros.get();

      if (libro.getAutor() != null) {
        Autor autor = autorRepository.findAutorByNombre(libro.getAutor().getNombre());

        if (autor == null) {
          // Crear y guardar un nuevo autor si no existe
          Autor nuevoAutor = libro.getAutor();
          autor = autorRepository.save(nuevoAutor);
        }

        try {
          // Asociar el autor existente con el libro
          libro.setAutor(autor);
          libroRepository.save(libro);
          System.out.println(libro);
        } catch (DataIntegrityViolationException e) {
          System.out.println("El libro ya se encuentra registrado en la base de datos.");
        }
      }
    } else {
      System.out.println("No se encontró el libro: " + nombreLibro);
    }
  }

  // Listado de libros registrados
  private void listarLibros(){
    List<Libro> libros = libroRepository.findAll();
    libros.forEach(System.out::println);
  }

  // buscar autor por nombre en API
  private void buscarAutorPorNombre(){
    System.out.println("Escribe el nombre del autor que deseas buscar: ");
    String nombreAutor = sc.nextLine();

    if(esNumero(nombreAutor)){
      System.out.println("Debes ingresar un nombre, no un número.");
    }else{
      String json = consumoApi.obtenerDatos(API_URL + "?search=" + nombreAutor.replace(" ", "+"));
      Datos datos = convierteDatos.obtenerDatos(json, Datos.class);

      Optional<DatosAutor> autorBuscado = datos.resultados().stream()
              .findFirst()
              .map(a -> new DatosAutor(a.autor().get(0).nombre(), a.autor().get(0).fechaNacimiento(), a.autor().get(0).fechaFallecimiento()));

      if(autorBuscado.isPresent()){
        System.out.println(autorBuscado.get());
      }else{
        System.out.println("No se encontró autor con el nombre: " + nombreAutor);
      }
    }
  }

  //Validar si un valor es número
  private boolean esNumero(String nombreAutor) {
    try {
      Double.parseDouble(nombreAutor);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  // Listado de autores registrados
  private void litsadoAutores(){
    List<Autor> autores = autorRepository.findAll();
    autores.forEach(System.out::println);
  }

  // Buscar autores vivos

  private void buscarPorAutoresVivos(){
    System.out.println("Ingrese el año vivo del autor(es) que desea buscar: ");
    try{
      String anioBuscado = sc.nextLine();
      List<Autor> autoresVivos = autorRepository.buscarAutoresVivos(anioBuscado);
      if(autoresVivos.isEmpty()){
        System.out.println("No se encontraron registros de autores vivos durante ese año en la base de datos.");
      }else{
        autoresVivos.forEach(System.out::println);
      }

    }catch (NumberFormatException e){
      System.out.println("Debes ingresar un año válido.");
    }
    sc.nextLine();
  }

  // Buscar libros por idioma
  private void buscarLibroPorIdioma(){
    System.out.println("Ingrese el idioma que desea buscar: ");
    System.out.println("""
                es -> Español
                en -> Inglés
                fr -> Francés
                pt -> Portugés
                """);

    String idioma = sc.nextLine();

    List<Libro> librosBuscados = libroRepository.findLibroByIdiomas(idioma.toUpperCase());
    if(librosBuscados.isEmpty()){
      System.out.println("No se encontraron libros en ese idioma");
    }else{
      librosBuscados.forEach(System.out::println);
    }
  }

  //
  private void top10Libros(){
    String json = consumoApi.obtenerDatos(API_URL);
    Datos datos = convierteDatos.obtenerDatos(json, Datos.class);

    List<Libro> top10Books = datos.resultados().stream()
            .map(b -> new Libro(b))
            .sorted(Comparator.comparingDouble(Libro::getNumeroDescargas).reversed())
            .limit(10)
            .collect(Collectors.toList());

    top10Books.stream()
            .forEach(b -> System.out.println(b.getTitulo() + " : (" + b.getNumeroDescargas() + " descargas)\n"));
  }
}
