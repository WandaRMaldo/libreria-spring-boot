package edu.egg.libreriaboot.service;

import edu.egg.libreriaboot.entity.Usuario;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.repository.UsuarioRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
    
@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private final String MENSAJE_USERNAME = "No existe un usuario registrado con el correo %s";

    @Transactional
    public void crear(String nombre, String apellido, String correo, String clave) throws MiExcepcion {

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new MiExcepcion("Ya existe un usuario registrado con ese correo");
        }

        if (correo == null || correo.trim().isEmpty()) {
            throw new MiExcepcion("el correo es obligatorio");
        }
        if (clave == null || clave.trim().isEmpty()) {
            throw new MiExcepcion("la clave es obligatorio");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.toUpperCase());
        usuario.setApellido(apellido.toUpperCase());
        usuario.setCorreo(correo);
        usuario.setClave(encoder.encode(clave));
        usuario.setAlta(true);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public void darDeAlta(String id) {
        usuarioRepository.habilitar(id);
    }

    @Transactional
    public void eliminar(String id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(MENSAJE_USERNAME, correo)));

        //GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        //OJO HAY QUE IMPORTAR EL CORE en user
        //return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));
        return new User(usuario.getCorreo(), usuario.getClave(), Collections.emptyList());
    }

}