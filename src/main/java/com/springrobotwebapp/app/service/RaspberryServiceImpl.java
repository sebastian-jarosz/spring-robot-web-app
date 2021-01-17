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
public class RaspberryServiceImpl implements RaspberryService {

    public Boolean isListening = Boolean.FALSE;
    public Boolean isMqttProcessRunning = Boolean.FALSE;

    //Commands
    private final String RUN_LISTENER_COMMAND = "nohup python3 Desktop/robot_mqtt_subscriber.py\n";
    private final String KILL_LISTENER_COMMAND = "pkill python3\n exit\n";
    private final String CHECK_MQTT_PROCESS_COMMAND = "sudo rabbitmq-plugins list rabbitmq_mqtt\n";
    private final String TURN_OFF_RASPBERRY_COMMAND = "sudo halt\n";

    private final String USER = "pi";
    private final String PASSWORD = "";
    private final String HOST = "malinkaseba.local";
    private final int PORT = 22;
    private final String RUNNING_STATUS = "[E*]";

    private Session session;

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
            System.out.println("Connection not established.");
        }
    }

    @Override
    public void runMqListener() throws JSchException, IOException {
        getSession();
        if(session == null) {
            return;
        }
        checkMqttProcess();
        if (session != null && isMqttProcessRunning && !isListening) {
            Channel channel = session.openChannel("shell");
            channel.setInputStream(new ByteArrayInputStream(RUN_LISTENER_COMMAND.getBytes(StandardCharsets.UTF_8)));
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
    }

    @Override
    public void checkMqttProcess() throws JSchException, IOException {
        if (session != null && !isMqttProcessRunning) {
            Channel channel = session.openChannel("shell");
            channel.setInputStream(new ByteArrayInputStream(CHECK_MQTT_PROCESS_COMMAND.getBytes(StandardCharsets.UTF_8)));
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
            if (outBuff.toString().contains(RUNNING_STATUS)) {
                isMqttProcessRunning = Boolean.TRUE;
            } else {
                isMqttProcessRunning = Boolean.FALSE;
            }
        }
    }

    @Override
    public void killMqListenerProcess() throws JSchException, IOException {
        getSession();
        if (session != null) {
            Channel channel = session.openChannel("shell");
            channel.setInputStream(new ByteArrayInputStream(KILL_LISTENER_COMMAND.getBytes(StandardCharsets.UTF_8)));
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
    }

    public Session getSession() {
        //In case of closed connection
        if (session == null || !session.isConnected()) {
            connectToRaspberry();
        }
        return session;
    }

    @Override
    public void turnOffRaspberry() throws JSchException, IOException {
        getSession();
        if (session != null) {
            Channel channel = session.openChannel("shell");
            channel.setInputStream(new ByteArrayInputStream(TURN_OFF_RASPBERRY_COMMAND.getBytes(StandardCharsets.UTF_8)));
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
        }
    }
}
