/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.libreriaboot.controller;

import edu.egg.libreriaboot.entity.Rol;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.service.RolService;
import edu.egg.libreriaboot.service.UsuarioService;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private RolService rolService;
    //private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {
        ModelAndView mav = new ModelAndView("login");

        
        if (error != null) {
            mav.addObject("error", "Correo o contraseña inválida");
        }

        if (logout != null) {
            mav.addObject("logout", "Ha salido correctamente de la plataforma");
        }

        if (principal != null) {
            //LOGGER.info("Principal -> {}", principal.getName());
            mav.setViewName("redirect:/");
        }

        return mav;
    }
    
     @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal) {
        ModelAndView mav = new ModelAndView("signup"); //seteamos un html
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("nombre", flashMap.get("nombre"));
            mav.addObject("apellido", flashMap.get("apellido"));
            mav.addObject("correo", flashMap.get("correo"));
            mav.addObject("clave", flashMap.get("clave"));
        }
        if (principal != null) {
            //LOGGER.info("Principal -> {}", principal.getName());
            mav.setViewName("redirect:/");
        }
        return mav;
    }

    @PostMapping("/registro")
    public RedirectView signup(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, @RequestParam Rol rol, RedirectAttributes attributes, HttpServletRequest request) {
        RedirectView redirectView = new RedirectView("/login"); //redirije a rutas

        try {
            usuarioService.crear(nombre, apellido, correo, clave, rol);
            attributes.addFlashAttribute("exito", "SE HA REGISTRADO CON ÉXITO.");
            //request.login(correo, clave);
            redirectView.setUrl("/index");

        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
            attributes.addFlashAttribute("nombre", nombre);
            attributes.addFlashAttribute("apellido", apellido);
            attributes.addFlashAttribute("correo", correo);
            attributes.addFlashAttribute("clave", clave);
            redirectView.setUrl("/signup");
        }
        return redirectView;
    }
}
