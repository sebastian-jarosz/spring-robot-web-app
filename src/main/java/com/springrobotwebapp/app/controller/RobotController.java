package com.springrobotwebapp.app.controller;

import com.jcraft.jsch.JSchException;
import com.springrobotwebapp.app.service.RaspberryServiceImpl;
import com.springrobotwebapp.app.service.RobotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.springrobotwebapp.app.repository.RobotRepository;

import java.io.IOException;

@Controller
@RequestMapping("/robot")
public class RobotController {

    private final RobotRepository robotRepository;
    private final RobotServiceImpl robotService;
    private final RaspberryServiceImpl raspberryService;

    @Autowired
    public RobotController(RobotRepository robotRepository, RobotServiceImpl robotService, RaspberryServiceImpl raspberryService) throws JSchException, IOException {
        this.robotRepository = robotRepository;
        this.robotService = robotService;
        this.raspberryService = raspberryService;
    }

    @GetMapping
    public String robot() {
        return "robot";
    }

    @PostMapping
    public void controlRobot(@RequestParam("button") String button) throws IOException, JSchException {
        System.out.println(button);
        if("forward".equals(button)){
            robotService.sendGoForwardMessage();
        } else if("left".equals(button)){
            robotService.sendGoLeftMessage();
        } else if("stop".equals(button)){
            robotService.sendStopMessage();
        } else if("right".equals(button)){
            robotService.sendGoRightMessage();
        } else if("backward".equals(button)){
            robotService.sendGoBackwardMessage();
        } else if("run".equals(button)){
            raspberryService.runMqListener();
        } else if("kill".equals(button)){
            raspberryService.killMqListenerProcess();
        }
    }

}
