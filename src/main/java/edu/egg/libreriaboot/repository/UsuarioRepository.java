/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.libreriaboot.repository;

import edu.egg.libreriaboot.entity.Usuario;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{

    @Modifying
    @Query("UPDATE Usuario u SET u.alta = true WHERE u.id = :id")
    void habilitar(@Param("id") String id);
    
    // Creación de consulta a partir del nombre de método
    Optional<Usuario> findByCorreo(String correo); //el optional si lo encuentra, lo asigna, sino me permite implementar una interfaz para generar una excepción

    // Creación de consulta a partir del nombre de método
    boolean existsByCorreo(String correo);
    
}
