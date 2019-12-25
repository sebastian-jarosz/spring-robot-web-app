package com.springrobotwebapp.app.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class RaspberryServiceImpl implements RaspberryService{

    public Boolean isListening;
    public Boolean isMqttProcessRunning;

    private final String USER = "pi";
    private final String PASSWORD = "Sebaj132!";
    private final String HOST = "malinkaseba.local";
    private final int PORT = 22;
    private final String RUNNING_STATUS = "[E*]";

    private Session session;

    //Commands
    private String runListenerCommand = "nohup python3 Desktop/robot_mqtt_subscriber.py\n";
    private String killListenerCommand = "pkill python3\n exit\n";
    private String checkMqttProcessCommand = "sudo rabbitmq-plugins list rabbitmq_mqtt\n";

    public RaspberryServiceImpl() {
    }

    @Override
    public void connectToRaspberry() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            this.session = session;
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runMqListener() throws JSchException, IOException {
        getSession();
        checkMqttProcess();
        Channel channel = session.openChannel("shell");
        channel.setInputStream(new ByteArrayInputStream(runListenerCommand.getBytes(StandardCharsets.UTF_8)));
        channel.setOutputStream(System.out);
        InputStream inputStream = channel.getInputStream();
        StringBuilder outBuff = new StringBuilder();

        channel.connect();

        while (!outBuff.toString().contains("nohup.out")) {
            for (int c; ((c = inputStream.read()) >= 0); ) {
                outBuff.append((char) c);
                if (outBuff.toString().contains("nohup.out")) {
                    break;
                }
            }
        }

        channel.disconnect();
        System.out.println(outBuff.toString());
        isListening = Boolean.TRUE;
    }

    @Override
    public void checkMqttProcess() throws JSchException, IOException {
        getSession();
        Channel channel = session.openChannel("shell");
        channel.setInputStream(new ByteArrayInputStream(checkMqttProcessCommand.getBytes(StandardCharsets.UTF_8)));
        channel.setOutputStream(System.out);
        InputStream inputStream = channel.getInputStream();
        StringBuilder outBuff = new StringBuilder();

        channel.connect();

        while (!outBuff.toString().contains("] rabbitmq_mqtt")) {
            for (int c; ((c = inputStream.read()) >= 0); ) {
                outBuff.append((char) c);
                if (outBuff.toString().contains("] rabbitmq_mqtt")) {
                    break;
                }
            }
        }

        channel.disconnect();
        System.out.println(outBuff.toString());
        if(outBuff.toString().contains(RUNNING_STATUS)){
            isMqttProcessRunning = Boolean.TRUE;
        } else {
            isMqttProcessRunning = Boolean.FALSE;
        }
    }

    @Override
    public void killMqListenerProcess() throws JSchException, IOException {
        getSession();
        Channel channel = session.openChannel("shell");
        channel.setInputStream(new ByteArrayInputStream(killListenerCommand.getBytes(StandardCharsets.UTF_8)));
        channel.setOutputStream(System.out);
        InputStream inputStream = channel.getInputStream();
        StringBuilder outBuff = new StringBuilder();

        channel.connect();

        while (true) {
            for (int c; ((c = inputStream.read()) >= 0); ) {
                outBuff.append((char) c);
            }

            if (channel.isClosed()) {
                if (inputStream.available() > 0) continue;
                break;
            }
        }

        channel.disconnect();
        System.out.println(outBuff.toString());
        isListening = Boolean.FALSE;
    }

    public Session getSession() {
        //In case of closed connection
        if(session == null || !session.isConnected()) {
            connectToRaspberry();
        }
        return session;
    }
}
