package com.springrobotwebapp.app.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;

public interface RaspberryService {

    public void connectToRaspberry();
    public void runMqListener() throws JSchException, IOException;
    public void killMqListenerProcess() throws JSchException, IOException;
    public void checkMqttProcess() throws JSchException, IOException;

}
