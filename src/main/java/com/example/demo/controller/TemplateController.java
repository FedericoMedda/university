package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String getLoginView(){
        return "login"; //qua dobbiamo avere lo stesso nome scelto del file all'interno della cartella templates
    }

    @GetMapping("courses")
    public String getCourses(){
        return "courses"; //stesso nome del file html che vogliamo aprire, che si trova dentro la cartella templates
    }
}
