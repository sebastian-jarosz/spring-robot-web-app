package com.springrobotwebapp.app.controller;

import com.springrobotwebapp.app.model.Robot;
import com.springrobotwebapp.app.service.RobotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.springrobotwebapp.app.repository.RobotRepository;

import java.util.List;

@RestController
@RequestMapping("/robot")
public class RobotController {

    private final RobotRepository robotRepository;
    private final RobotServiceImpl robotService;

    @Autowired
    public RobotController(RobotRepository robotRepository, RobotServiceImpl robotService){
        this.robotRepository = robotRepository;
        this.robotService = robotService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addRobot(@RequestBody Robot robot){
        robotRepository.save(robot);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Robot> getRobot(){
        return robotRepository.findAll();
    }

    @RequestMapping("/F")
    public void sendGoForwardMessage(){
        robotService.sendGoForwardMessage();
    }

    @RequestMapping("/S")
    public void sendSMessage(){
        robotService.sendStopMessage();
    }
}
