package com.springrobotwebapp.app.controller;

import com.jcraft.jsch.JSchException;
import com.springrobotwebapp.app.service.RaspberryServiceImpl;
import com.springrobotwebapp.app.service.RobotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.springrobotwebapp.app.repository.RobotRepository;

import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping("/robot")
public class RobotController {

    private final RobotServiceImpl robotService;
    private final RaspberryServiceImpl raspberryService;

    private final String isListeningStr = "IS_LISTENING";
    private final String isMqttProcessRunningStr = "IS_MQTT_PROCESS_RUNNING";

    @Autowired
    public RobotController(RobotServiceImpl robotService, RaspberryServiceImpl raspberryService) throws JSchException, IOException {
        this.robotService = robotService;
        this.raspberryService = raspberryService;
    }

    @GetMapping
    public String robot(Model model) {
        model.addAttribute(raspberryService);
        return "robot";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> controlRobot(@RequestParam("button") String button) throws IOException, JSchException {
        System.out.println(button);
        HashMap<String, String> hashMap = new HashMap();
        if ("forward".equals(button)) {
            robotService.sendGoForwardMessage();
        } else if ("left".equals(button)) {
            robotService.sendGoLeftMessage();
        } else if ("stop".equals(button)) {
            robotService.sendStopMessage();
        } else if ("right".equals(button)) {
            robotService.sendGoRightMessage();
        } else if ("backward".equals(button)) {
            robotService.sendGoBackwardMessage();
        } else if ("run".equals(button)) {
            raspberryService.runMqListener();
            hashMap.put(isListeningStr, raspberryService.isListening.toString());
            hashMap.put(isMqttProcessRunningStr, raspberryService.isMqttProcessRunning.toString());
        } else if ("kill".equals(button)) {
            raspberryService.killMqListenerProcess();
        }
        return ResponseEntity.ok(hashMap);
    }

    @GetMapping("/connectionInfo")
    public ResponseEntity<?> getConnectionInfo(){
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(isListeningStr, raspberryService.isListening.toString());
        hashMap.put(isMqttProcessRunningStr, raspberryService.isMqttProcessRunning.toString());
        return ResponseEntity.ok(hashMap);
    }

}
