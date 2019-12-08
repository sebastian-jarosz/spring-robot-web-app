package com.springrobotwebapp.app.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotServiceImpl implements RobotService {

    //Messages constants
    private final String GO_FORWARD = "forward";
    private final String GO_BACKWARD = "backward";
    private final String GO_LEFT = "left";
    private final String GO_RIGHT = "right";
    private final String STOP = "stop";

    //Message exchange
    private final String EXCHANGE = "amq.topic";

    //Message routing key
    private final String ROUTING_KEY = "malinkaseba.move";

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public RobotServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setExchange(EXCHANGE);
        this.rabbitTemplate.setRoutingKey(ROUTING_KEY);
    }

    @Override
    public void sendGoForwardMessage() {
        rabbitTemplate.convertAndSend(GO_FORWARD);
    }

    @Override
    public void sendGoBackwardMessage() {
        rabbitTemplate.convertAndSend(GO_BACKWARD);
    }

    @Override
    public void sendGoLeftMessage() {
        rabbitTemplate.convertAndSend(GO_LEFT);
    }

    @Override
    public void sendGoRightMessage() {
        rabbitTemplate.convertAndSend(GO_RIGHT);
    }

    @Override
    public void sendStopMessage() {
        rabbitTemplate.convertAndSend(STOP);
    }

}
