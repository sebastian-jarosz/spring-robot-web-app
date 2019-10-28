package com.springrobotwebapp.app.controller;

import com.springrobotwebapp.app.model.Robot;
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

    @Autowired
    public RobotController(RobotRepository robotRepository){
        this.robotRepository = robotRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addRobot(@RequestBody Robot robot){
        robotRepository.save(robot);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Robot> getRobot(){
        return robotRepository.findAll();
    }
}
