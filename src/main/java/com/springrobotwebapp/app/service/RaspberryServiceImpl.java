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

    private final String user = "pi";
    private final String password = "Sebaj132!";
    private final String host = "malinkaseba.local";
    private final int port = 22;
    private Session session;

    //Commands
    private String runListenerCommand = "nohup python3 Desktop/robot_mqtt_subscriber.py\n";
    private String killListenerCommand = "pkill python3\n exit\n";

    public RaspberryServiceImpl() {
    }

    @Override
    public void connectToRaspberry() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
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
        Channel channel = session.openChannel("shell");
        channel.setInputStream(new ByteArrayInputStream(runListenerCommand.getBytes(StandardCharsets.UTF_8)));
        channel.setOutputStream(System.out);
        InputStream in = channel.getInputStream();
        StringBuilder outBuff = new StringBuilder();

        channel.connect();

        while (!outBuff.toString().contains("'nohup.out'")) {
            for (int c; ((c = in.read()) >= 0); ) {
                outBuff.append((char) c);
                if (outBuff.toString().contains("'nohup.out'")) {
                    break;
                }
            }
        }

        channel.disconnect();
        System.out.println(outBuff.toString());
    }


    @Override
    public void killMqListenerProcess() throws JSchException, IOException {
        getSession();
        Channel channel = session.openChannel("shell");
        channel.setInputStream(new ByteArrayInputStream(killListenerCommand.getBytes(StandardCharsets.UTF_8)));
        channel.setOutputStream(System.out);
        InputStream in = channel.getInputStream();
        StringBuilder outBuff = new StringBuilder();

        channel.connect();

        while (true) {
            for (int c; ((c = in.read()) >= 0); ) {
                outBuff.append((char) c);
            }

            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                break;
            }
        }

        channel.disconnect();
        System.out.println(outBuff.toString());
    }

    public Session getSession() {
        //In case of closed connection
        if(session == null || !session.isConnected()) {
            connectToRaspberry();
        }
        return session;
    }
}
