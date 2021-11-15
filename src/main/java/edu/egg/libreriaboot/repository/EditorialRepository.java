/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.libreriaboot.repository;

import edu.egg.libreriaboot.entity.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author WANDA
 */
public interface EditorialRepository extends JpaRepository<Editorial, String>{
    
    @Modifying
    @Query("UPDATE Editorial e SET e.nombre = :nombre WHERE e.id = :id")
    void modificar(@Param("id") String id, @Param("nombre") String nombre);
    
    @Query("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    Editorial buscarPorNombre(@Param("nombre") String nombre);
}
