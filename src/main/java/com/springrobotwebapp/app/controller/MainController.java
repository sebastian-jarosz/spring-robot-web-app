package com.springrobotwebapp.app.controller;

import com.springrobotwebapp.app.model.User;
import com.springrobotwebapp.app.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController implements ErrorController {

    @Autowired
    User user;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute(Constants.USERNAME, Constants.WELCOME_MESSAGE + user.getUsername() + Constants.EXCLAMATION_MARK);
        return "main"; //view
    }

    //Return author page
    @GetMapping("/author")
    public String author() {
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

    //Handling Post request to /welcome path
    @PostMapping("/welcome")
    public String postWelcome(@RequestParam(Constants.USERNAME) String username, Model model) {
        System.out.println(username);
        user.setUsername(username);
        model.addAttribute(Constants.USERNAME, Constants.WELCOME_MESSAGE + user.getUsername() + Constants.EXCLAMATION_MARK);
        return "main";
    }

    //Return quit page
    @GetMapping("/quit")
    public String logout(Model model) {
        model.addAttribute(Constants.USERNAME, Constants.QUIT_MESSAGE + user.getUsername() + Constants.EXCLAMATION_MARK);
        user.removeUsername();
        return "quit";
    }

    //Return error page
    @GetMapping("/error")
    public String error() {
        return "error";
    }

    //Change handled error path
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
