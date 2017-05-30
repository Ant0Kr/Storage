package com.spo.StorageReleaze.client.controller.threads;

import com.spo.StorageReleaze.client.controller.MainController;
import com.spo.StorageReleaze.service.SerializeMaker;
import com.spo.StorageReleaze.service.model.Request;
import com.spo.StorageReleaze.service.model.StorageItem;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public class StorageTableThread extends Thread {

    private static final Logger log = Logger.getLogger(StorageTableThread.class);

    public StorageTableThread() {
    }

    public void run() {

        while (true) {

            Request request = new Request(Request.RequestName.SHOW_ALL, null, null);
            synchronized (MainController.getClient()) {
                try {
                    if (!MainController.getSearchFlag()) {

                        MainController.getClient().getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                        String response = MainController.getClient().getInputStream().readUTF();

                        synchronized (MainController.getStorageList()) {
                            MainController.setStorageList(SerializeMaker.deserializeFromXML(response));
                        }
                    }
                    synchronized (MainController.getStorageList()) {
                        synchronized (MainController.getStorageTable()) {
                            MainController.changeStorageTable();
                        }

                    }
                } catch (IOException e1) {
                    return;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                return;
            }

        }
    }

}
