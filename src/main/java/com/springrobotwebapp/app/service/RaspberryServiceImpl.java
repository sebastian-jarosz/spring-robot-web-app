package com.springrobotwebapp.app.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

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
    private String command = "python3 Desktop/robot_mqtt_subscriber.py\n";
    String command2 = "hostname\ndf -h\nexit\n";
    private Session session;

    public RaspberryServiceImpl() {
    }

    @Override
    public Session connectToRaspberry() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            this.session = session;
            return session;
        } catch (JSchException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void runMqListener() throws JSchException, IOException {
        connectToRaspberry();
        if(session.isConnected()){
            Channel channel = session.openChannel("shell");
            channel.setInputStream(new ByteArrayInputStream(command.getBytes(StandardCharsets.UTF_8)));
            channel.setOutputStream(System.out);
            InputStream in = channel.getInputStream();
            StringBuilder outBuff = new StringBuilder();
            int exitStatus = -1;

            channel.connect();

            while(!outBuff.toString().contains("Subscribed")){
                for (int c; ((c = in.read()) >= 0);) {
                    outBuff.append((char) c);
                    if(outBuff.toString().contains("Subscribed")){
                        break;
                    }
                }

                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    exitStatus = channel.getExitStatus();
                    break;
                }
            }

            System.out.println(outBuff.toString());

        }
    }
}
