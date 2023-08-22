package com.heritart.control;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Iterator;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String MaxUploadSizeExceededException(HttpServletRequest request,
                                                 RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("error", "Massima dimensione consentita: 10 MB.");

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(false);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String ContraintViolationException(ConstraintViolationException ex,
                                              HttpServletRequest request,
                                              RedirectAttributes redirectAttributes) {

        ConstraintViolation<?> constraints =  ex.getConstraintViolations().iterator().next();
        String error = constraints.getMessage();
        Path path = constraints.getPropertyPath();
        Iterator<Path.Node> iter = path.iterator();
        iter.next();
        String param = iter.next().getName();

        if(param.equals("files")){
            redirectAttributes.addFlashAttribute("error", "Massimo numero di foto consentito: 6");
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Campo non valido: " + param + ".");
        }

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String MissingRequestParameterException(MissingServletRequestParameterException ex,
                                                   HttpServletRequest request,
                                                   RedirectAttributes redirectAttributes) {

        String param = ex.getParameterName();
        redirectAttributes.addFlashAttribute("error", "Inserire " + param + ".");
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                      HttpServletRequest request,
                                                      RedirectAttributes redirectAttributes) {

        String param = ex.getName();
        redirectAttributes.addFlashAttribute("error",  "Campo non valido: " + param + ".");
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @ExceptionHandler(NullPointerException.class)
    public String NullPointerException(NullPointerException e) {
        return "redirect:/error";
    }

}
