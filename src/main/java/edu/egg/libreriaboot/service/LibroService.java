package edu.egg.libreriaboot.service;

import edu.egg.libreriaboot.entity.Autor;
import edu.egg.libreriaboot.entity.Editorial;
import edu.egg.libreriaboot.entity.Libro;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.repository.AutorRepository;
import edu.egg.libreriaboot.repository.EditorialRepository;
import edu.egg.libreriaboot.repository.LibroRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    
    @Autowired
    private AutorRepository autorRepository;
    
    @Autowired
    private EditorialRepository editorialRepository;
    
    @Transactional
    public void crear(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, String idAutor, String idEditorial) throws MiExcepcion{
        validarIsbn(isbn);
        validarTitulo(titulo);
        validarAnio(anio);
        validarEjemplares(ejemplares);
        validarEjemplaresPrestados(ejemplaresPrestados, ejemplares);
        if (idAutor == null || idAutor.isEmpty()) {
            throw new MiExcepcion("el autor es obligatorio");
        }
        
        if (idEditorial == null || idEditorial.isEmpty()) {
            throw new MiExcepcion("la editorial es obligatorio");
        } 
        Libro libro = new Libro();
        
        libro.setIsbn(isbn);
        libro.setTitulo(titulo.toUpperCase());
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(restantes(ejemplares, ejemplaresPrestados));
        libro.setAutor(autorRepository.findById(idAutor).orElse(null));
        libro.setEditorial(editorialRepository.findById(idEditorial).orElse(null));
    
        libroRepository.save(libro);
    }
    
    @Transactional
    public void modificar(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Autor autor, Editorial editorial) throws MiExcepcion{
        //validarIsbn(isbn);
        //validarTitulo(titulo);
        validarAnio(anio);
        validarEjemplares(ejemplares);
        validarEjemplaresPrestados(ejemplaresPrestados, ejemplares);
        if (autor == null) {
            throw new MiExcepcion("el autor es obligatorio");
        }
        
        if (editorial == null) {
            throw new MiExcepcion("la editorial es obligatorio");
        }
        Integer ejemplaresRestantes = restantes(ejemplares, ejemplaresPrestados);
        libroRepository.modificar(id, isbn, titulo.toUpperCase(), anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
    }
    
    @Transactional(readOnly = true)
    public List<Libro> buscarTodas(){
        return libroRepository.findAll();
    }
    
    @Transactional
    public void darBaja(String id) throws MiExcepcion{
        Optional<Libro> libroOptional = libroRepository.findById(id) ;
        if (libroOptional.isPresent()) {
            Libro libro = libroOptional.get();
            libro.setAlta(!libro.getAlta());
            libroRepository.save(libro);
        }else{
            throw new MiExcepcion("No se encontro el libro solicitado");
        }
    }
    
    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) {
        Optional<Libro> libroOptional = libroRepository.findById(id);
        return libroOptional.orElse(null);
    }
    
    public Integer restantes(Integer ejemplares, Integer ejemplaresPrestados){
        Integer ejemplaresRestantes;
        ejemplaresRestantes = ejemplares - ejemplaresPrestados;
        return ejemplaresRestantes;
    }
    
    public void validarTitulo(String titulo) throws MiExcepcion {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new MiExcepcion("el nombre es obligatorio");
        }

        if (libroRepository.buscarPorTitulo(titulo) != null) {
            throw new MiExcepcion("ya existe un libro con ese nombre");
        }
    }

    public void validarEjemplares(Integer ejemplares) throws MiExcepcion {
        if (ejemplares < 0 || ejemplares == null) {
            throw new MiExcepcion("la cantidad de ejemplares no puede ser menor a 0");
        }
    }

    public void validarEjemplaresPrestados(Integer ejemplaresPrestados, Integer ejemplares) throws MiExcepcion {

        if (ejemplaresPrestados < 0 || ejemplaresPrestados == null) {
            throw new MiExcepcion("la cantidad de ejemplares prestados no puede ser menor a 0");
        }
        if (ejemplaresPrestados > ejemplares) {
            throw new MiExcepcion("la cantidad de ejemplares prestados no puede superar la cantidad de ejemplares");
        }
    }

    public void validarAnio(Integer anio) throws MiExcepcion {

        Date date = new Date();

        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
        String anioString = getYearFormat.format(date);
        Integer anioActual = Integer.parseInt(anioString);

        if (anio > anioActual) {
            throw new MiExcepcion("el año no puede ser mayor al actual");
        }
        
        if (!anio.toString().matches("\\d{4}")) {
                throw new MiExcepcion("Año: debe ingresar 4 digitos");
            }
    }

    public void validarIsbn(Long isbn) throws MiExcepcion {

        if (isbn.toString().trim().isEmpty()) {
                throw new MiExcepcion("campo obligatorio");
            }

        if (!isbn.toString().matches("^[0-9]{13}$")) {
            throw new MiExcepcion("debe ingresar 13 digitos");
        }

        if (libroRepository.buscarLibroPorIsbn(isbn) != null) {
            throw new MiExcepcion("ya existe un libro con ese ISBN");
        }

    }
}
