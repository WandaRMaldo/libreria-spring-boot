/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.libreriaboot.service;

import edu.egg.libreriaboot.entity.Editorial;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.repository.EditorialRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialService {
    
    @Autowired
    private EditorialRepository editorialRepository;
    
    @Transactional
    public void crear(String nombre) throws MiExcepcion{
        
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("el nombre es obligatorio");
        }
        
        if (editorialRepository.buscarPorNombre(nombre) != null) {
            throw new MiExcepcion("ya existe un editorial con ese nombre");
        }
        
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre.toUpperCase());
        
        editorialRepository.save(editorial);
    }
    
    @Transactional
    public void modificar(String id, String nombre) throws MiExcepcion{
        Editorial editorial = editorialRepository.findById(id).orElse(null);
        if (editorial == null) {
            throw new MiExcepcion("no existe la editorial");
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new MiExcepcion("el nombre es obligatorio");
        }

        if (editorialRepository.buscarPorNombre(nombre) != null) {
            throw new MiExcepcion("ya existe una editorial con ese nombre");
        }
        editorialRepository.modificar(id, nombre.toUpperCase());
    }
    
    @Transactional(readOnly = true)
    public List<Editorial> buscarTodas(){
        return editorialRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Editorial buscarPorId(String id) {
        Optional<Editorial> editorialOptional = editorialRepository.findById(id);
        return editorialOptional.orElse(null);
    }
    
    @Transactional
    public void darBaja(String id) throws MiExcepcion{
        Optional<Editorial> editorialOptional = editorialRepository.findById(id); /*veamos si funciona*/
        if (editorialOptional.isPresent()) {
            Editorial editorial = editorialOptional.get();
            editorial.setAlta(!editorial.getAlta());
            editorialRepository.save(editorial);
        }else{
            throw new MiExcepcion("No se encontro la editorial solicitado");
        }
    }
}
