package com.springrobotwebapp.app.service;

public interface RobotService {

    public void sendGoForwardMessage();
    public void sendGoBackwardMessage();
    public void sendGoLeftMessage();
    public void sendGoRightMessage();
    public void sendStopMessage();
    public void sendChangeSpeedMessage();

}
