package com.springrobotwebapp.app.controller;

import com.springrobotwebapp.app.service.RobotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.springrobotwebapp.app.repository.RobotRepository;

import java.util.List;

@Controller
@RequestMapping("/robot")
public class RobotController {

    private final RobotRepository robotRepository;
    private final RobotServiceImpl robotService;

    @Autowired
    public RobotController(RobotRepository robotRepository, RobotServiceImpl robotService){
        this.robotRepository = robotRepository;
        this.robotService = robotService;
    }

    @GetMapping
    public String robot() {
        return "robot";
    }

    @PostMapping
    public void addRobot(@RequestParam("button") String button){
        System.out.println(button);
        if("forward".equals(button)){
            System.out.println("send");
            robotService.sendGoForwardMessage();
        } else if("left".equals(button)){
            robotService.sendGoLeftMessage();
        } else if("stop".equals(button)){
            robotService.sendStopMessage();
        } else if("right".equals(button)){
            robotService.sendGoRightMessage();
        } else if("backward".equals(button)){
            robotService.sendGoBackwardMessage();
        }
    }

}
