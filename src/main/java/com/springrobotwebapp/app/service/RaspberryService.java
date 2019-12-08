package com.springrobotwebapp.app.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;

public interface RaspberryService {

    public Session connectToRaspberry();
    public void runMqListener() throws JSchException, IOException;

}
