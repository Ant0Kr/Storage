package com.spo.StorageReleaze.server.main;

import com.spo.StorageReleaze.server.hibernate.Factory;
import com.spo.StorageReleaze.service.model.StorageItem;
import com.spo.StorageReleaze.service.model.User;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public class MainServer {

    private static LinkedList<StorageItem> storage;
    private static LinkedList<User> userCatalog;
    final static int PORT = 5555;
    private static final Logger log = Logger.getLogger(MainServer.class);

    public static void main(String args[]) throws Exception {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (Exception e) {
            log.error("Server already connected.");
            return;
        }
        initUsersCatalog();
        initItemsCatalog();
        log.info("Server connected!");

        while (true) {
            socket = serverSocket.accept();
            ServerThread thread = new ServerThread(socket);
            thread.start();
        }
    }

    private static void initUsersCatalog() throws Exception {
        userCatalog = new LinkedList<>();
        List<User> list = Factory.getInstance().getUserDAO().getAllUsers();
        for (int i = 0; i < list.size(); i++)
            userCatalog.add(list.get(i));
        log.info("init Users catalog!");
    }

    private static void initItemsCatalog() throws Exception {
        storage = new LinkedList<>();
        ObservableList<StorageItem> list = Factory.getInstance().getItemDAO().getAllItems();
        for (int i = 0; i < list.size(); i++)
            storage.add(list.get(i));
        log.info("init Items catalog!");
    }

    public static LinkedList<User> getUserCatalog() {
        return userCatalog;
    }

    public static LinkedList<StorageItem> getStorage() {
        return storage;
    }

}
