package edu.egg.libreriaboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrincipalController {
    
    @GetMapping /* cuando entre al url localhost:8080 se abre el index, es como la ruta por defecto */
    public ModelAndView inicio(){
        return new ModelAndView("index");
    }
}
