/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.libreriaboot.controller;

import edu.egg.libreriaboot.entity.Usuario;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.service.UsuarioService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioServicio;
    //private RolServicio rolServicio;


    @GetMapping
    public ModelAndView mostrarTodos(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuarios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("usuarios", usuarioServicio.buscarTodos());
        return mav;
    }
    

    @GetMapping("/crear")
    //@PreAuthorize("hasRole('ADMIN')")
    public ModelAndView crear() {
        ModelAndView mav = new ModelAndView("usuario-formulario");
        mav.addObject("usuario", new Usuario());
        mav.addObject("title", "Crear Usuario");
        mav.addObject("action", "guardar");
        //mav.addObject("roles", rolServicio.buscarTodos());
        return mav;
    }

    @PostMapping("/guardar")
    //@PreAuthorize("hasRole('ADMIN')")
    public RedirectView guardar(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, RedirectAttributes attributes) {
        try {
            usuarioServicio.crear(nombre, apellido, correo, clave);
            attributes.addFlashAttribute("exito", "El usuario ha sido creado exitosamente");
        } catch (MiExcepcion e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/usuarios");
    }


    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable String id) {
        usuarioServicio.darDeAlta(id);
        return new RedirectView("/usuarios");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) {
        usuarioServicio.eliminar(id);
        return new RedirectView("/usuarios");
    }
}
