package edu.egg.libreriaboot.controller;

import edu.egg.libreriaboot.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/rol")
public class RolController {
    
    @Autowired
    private RolService rolService;

    @GetMapping("/crear")
    public ModelAndView crear() {
        ModelAndView mav = new ModelAndView("rol-formulario");
        mav.addObject("title", "Crear Rol");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre) {
        rolService.crear(nombre);
        return new RedirectView("/");
    }
    
}
