package com.spo.StorageReleaze.client.main;

import com.spo.StorageReleaze.service.model.User;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainClient {

    private String address;
    private int PORT;
    private DataInputStream in;
    private DataOutputStream out;
    private User infoUser;
    private Socket socket;
    private static final Logger log = Logger.getLogger(MainClient.class);

    public MainClient(User user) {
        address = "127.0.0.1";
        PORT = 5555;
        infoUser = new User(user);
    }

    public MainClient() {
        address = "127.0.0.1";
        PORT = 5555;
    }


    public Boolean ClientConnection() throws IOException {
        InetAddress IpAdress = InetAddress.getByName(address);
        try {
            socket = new Socket(IpAdress, PORT);
        } catch (Exception e) {
            log.info("Server disconnected.");
            return false;
        }
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        log.info("System connected to server.");
        return true;
    }

    public DataInputStream getInputStream() {
        return in;
    }

    public DataOutputStream getOutputStream() {
        return out;
    }

    public User getUser() {
        return infoUser;
    }

    public void setUser(User user) {
        this.infoUser = new User(user);
    }

}