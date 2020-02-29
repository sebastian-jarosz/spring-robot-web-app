package com.springrobotwebapp.app.controller;

import com.springrobotwebapp.app.model.User;
import com.springrobotwebapp.app.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController implements ErrorController {

    @Autowired
    User user;

    //Inject via application.properties
    @Value("${welcome.message}")
    private String message;

    private ArrayList<String> tasks = new ArrayList<String>(Arrays.asList("a", "b", "c"));

    @GetMapping("/")
    public String main(Model model){
        model.addAttribute("message", message);
        model.addAttribute(Constants.USERNAME, user.getUsername());
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

    //Return welcome page
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @PostMapping("/welcome")
    public String postWelcome(@RequestParam(Constants.USERNAME) String username, Model model) {
        System.out.println(username);
        user.setUsername(username);
        model.addAttribute(Constants.USERNAME, Constants.WELCOME_MESSAGE + user.getUsername());
        return "main";
    }

    @GetMapping("/quit")
    public String logout(Model model) {
        model.addAttribute(Constants.USERNAME, Constants.QUIT_MESSAGE + user.getUsername());
        user.removeUsername();
        return "quit";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
