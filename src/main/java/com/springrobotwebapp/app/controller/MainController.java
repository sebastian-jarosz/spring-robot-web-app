package com.springrobotwebapp.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    //Inject via application.properties
    @Value("${welcome.message}")
    private String message;

    private List<String> tasks = Arrays.asList("a", "b", "c");

    @GetMapping("/")
    public String main(Model model){
        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);

        return "main"; //view
    }

    //Return author page
    @GetMapping("/author")
    public String author(){
        return "author";
    }

    //Return tech page
    @GetMapping("/tech")
    public String tech() {
        return "tech";
    }

}
