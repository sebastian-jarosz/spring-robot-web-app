package com.springrobotwebapp.app.controller;

import com.jcraft.jsch.JSchException;
import com.springrobotwebapp.app.service.RaspberryServiceImpl;
import com.springrobotwebapp.app.service.RobotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping("/robot")
public class RobotController {

    public Boolean isTopSpeedModeOn = Boolean.FALSE;

    private final RobotServiceImpl robotService;
    private final RaspberryServiceImpl raspberryService;

    private final String IS_LISTENING_STRING = "IS_LISTENING";
    private final String IS_MQTT_PROCESS_RUNNING_STRING = "IS_MQTT_PROCESS_RUNNING";
    private final String IS_TOP_SPEED_MODE_ON_STRING = "IS_TOP_SPEED_MODE_ON";

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
        } else if ("speed".equals(button)) {
            robotService.sendChangeSpeedMessage();
            isTopSpeedModeOn = isTopSpeedModeOn ? Boolean.FALSE : Boolean.TRUE;
            hashMap.put(IS_TOP_SPEED_MODE_ON_STRING, isTopSpeedModeOn.toString());
        } else if ("connect".equals(button)) {
            raspberryService.runMqListener();
            hashMap.put(IS_LISTENING_STRING, raspberryService.isListening.toString());
            hashMap.put(IS_MQTT_PROCESS_RUNNING_STRING, raspberryService.isMqttProcessRunning.toString());
        } else if ("kill".equals(button)) {
            raspberryService.killMqListenerProcess();
            isTopSpeedModeOn = Boolean.FALSE;
            hashMap.put(IS_LISTENING_STRING, raspberryService.isListening.toString());
            hashMap.put(IS_MQTT_PROCESS_RUNNING_STRING, raspberryService.isMqttProcessRunning.toString());
        }
        return ResponseEntity.ok(hashMap);
    }

    @GetMapping("/connectionInfo")
    public ResponseEntity<?> getConnectionInfo(){
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(IS_LISTENING_STRING, raspberryService.isListening.toString());
        hashMap.put(IS_MQTT_PROCESS_RUNNING_STRING, raspberryService.isMqttProcessRunning.toString());
        hashMap.put(IS_TOP_SPEED_MODE_ON_STRING, isTopSpeedModeOn.toString());
        return ResponseEntity.ok(hashMap);
    }

}
