package edu.egg.libreriaboot.controller;

import edu.egg.libreriaboot.entity.Autor;
import edu.egg.libreriaboot.entity.Editorial;
import edu.egg.libreriaboot.entity.Libro;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.service.AutorService;
import edu.egg.libreriaboot.service.EditorialService;
import edu.egg.libreriaboot.service.LibroService;
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
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;
    
    @Autowired
    private AutorService autorService;
    
    @Autowired
    private EditorialService editorialService;
    
    @GetMapping
    public ModelAndView mostrarTodos(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("libros");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("libros", libroService.buscarTodas());
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearLibro(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("libro-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            //mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("libro", new Libro());
        mav.addObject("autores", autorService.buscarTodas());
        mav.addObject("editoriales", editorialService.buscarTodas());
        mav.addObject("title", "Crear Libro");
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarLibro(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("libro-formulario");  
        mav.addObject("libro", libroService.buscarPorId(id));
        mav.addObject("autores", autorService.buscarTodas());
        mav.addObject("editoriales", editorialService.buscarTodas());
        mav.addObject("title", "Editar Libro");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar") /* post:  recibir informacion de un formulario */
    public RedirectView guardar(@RequestParam String id, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam("autor") String idAutor, @RequestParam("editorial") String idEditorial, RedirectAttributes attributes) throws MiExcepcion {
        try {
            libroService.crear(isbn, titulo, anio, ejemplares, ejemplaresPrestados, idAutor, idEditorial);
            attributes.addFlashAttribute("exito-name", "EL LIBRO SE GUARDO EXITOSAMENTE");
        } catch (MiExcepcion e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
            return new RedirectView("/libros/crear");
        }
        
        return new RedirectView("/libros");
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Autor autor, @RequestParam Editorial editorial, RedirectAttributes attributes) throws MiExcepcion {
        try {
            libroService.modificar(id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, autor, editorial);
            attributes.addFlashAttribute("exito-name", "LIBRO MODIFICADO CON ÉXITO");
        } catch (MiExcepcion e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        
        return new RedirectView("/libros");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) throws MiExcepcion {
        libroService.darBaja(id);
        return new RedirectView("/libros");
    }
}
