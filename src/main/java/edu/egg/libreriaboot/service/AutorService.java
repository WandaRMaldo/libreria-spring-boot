package edu.egg.libreriaboot.service;

import edu.egg.libreriaboot.entity.Autor;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.repository.AutorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorService {
    
    @Autowired
    private AutorRepository autorRepository;
    
    @Transactional
    public void crear(String nombre) throws MiExcepcion{
        
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("el nombre es obligatorio");
        }
        
        if (autorRepository.buscarAutorPorNombre(nombre) != null) {
            throw new MiExcepcion("ya existe un autor con ese nombre");
        }
        
        Autor autor = new Autor();
        autor.setNombre(nombre.toUpperCase());
        
        autorRepository.save(autor);
    }
    
    @Transactional
    public void modificar(String id, String nombre) throws MiExcepcion{
        Autor autor = autorRepository.findById(id).orElse(null);
        if (autor == null) {
            throw new MiExcepcion("no existe es autor");
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new MiExcepcion("el nombre es obligatorio");
        }

        if (autorRepository.buscarAutorPorNombre(nombre) != null) {
            throw new MiExcepcion("ya existe un autor con ese nombre");
        }

        autorRepository.modificar(id, nombre.toUpperCase());
    }
    
    @Transactional(readOnly = true)
    public List<Autor> buscarTodas(){
        return autorRepository.findAll();
    }
    
    @Transactional
    public void darBaja(String id) throws MiExcepcion{
        Optional<Autor> autorOptional = autorRepository.findById(id) ; /*veamos si funciona*/
        if (autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            autor.setAlta(!autor.getAlta());
            autorRepository.save(autor);
        }else{
            throw new MiExcepcion("No se encontro el autor solicitado");
        }
    }
    
    @Transactional(readOnly = true)
    public Autor buscarPorId(String id) {
        Optional<Autor> autorOptional = autorRepository.findById(id);
        return autorOptional.orElse(null);
    }
}
