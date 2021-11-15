package edu.egg.libreriaboot.repository;

import edu.egg.libreriaboot.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AutorRepository extends JpaRepository<Autor, String>{
    
    @Modifying
    @Query("UPDATE Autor a SET a.nombre = :nombre WHERE a.id = :id")
    void modificar(@Param("id") String id, @Param("nombre") String nombre);
    
    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    Autor buscarAutorPorNombre(@Param("nombre") String nombre);
}
