/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.libreriaboot.repository;

import edu.egg.libreriaboot.entity.Autor;
import edu.egg.libreriaboot.entity.Editorial;
import edu.egg.libreriaboot.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String>{
    
    @Modifying
    @Query("UPDATE Libro l SET l.isbn = :isbn, l.titulo = :titulo, l.anio = :anio, l.ejemplares = :ejemplares , l.ejemplaresPrestados = :ejemplaresPrestados, l.ejemplaresRestantes = :ejemplaresRestantes, l.autor = :autor, l.editorial = :editorial WHERE l.id = :id")
    void modificar(@Param ("id") String id, @Param ("isbn") Long isbn, @Param("titulo") String titulo, @Param("anio") Integer anio, @Param("ejemplares") Integer ejemplares, @Param("ejemplaresPrestados") Integer ejemplaresPrestados, @Param("ejemplaresRestantes") Integer ejemplaresRestantes, @Param("autor") Autor autor, @Param("editorial") Editorial editorial);
    
    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    Editorial buscarPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT l FROM Libro l WHERE l.isbn = :isbn")
    Libro buscarLibroPorIsbn(@Param("isbn") Long isbn);
}
