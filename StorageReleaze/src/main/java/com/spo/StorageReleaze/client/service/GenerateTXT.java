package com.spo.StorageReleaze.client.service;

import com.spo.StorageReleaze.client.controller.MainController;
import com.spo.StorageReleaze.service.model.StorageItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public class GenerateTXT {

    public static void GenerateTXT(String way) throws IOException {

        File file = new File(way);
        if (file.exists())
            file.delete();
        file.createNewFile();

        LinkedList<StorageItem> items;
        synchronized (MainController.getStorageList()) {
            items = MainController.getStorageList();
        }
        FileWriter fstream = new FileWriter(way);// конструктор с одним параметром - для перезаписи
        BufferedWriter out = new BufferedWriter(fstream); //  создаём буферезированный поток
        out.write("***Склад***\r\n" + "***********\r\n");
        String writeStr;
        int size = items.size();
        for (int i = 0; i < size; i++) {

            writeStr = "";
            StorageItem item = items.get(i);
            writeStr = writeStr + "\r\nНазвание:" + item.getName() + "\r\n";
            writeStr = writeStr + "Количество:" + item.getCount() + "\r\n";
            writeStr = writeStr + "Дата поступления:" + item.getDate() + "\r\n";
            writeStr = writeStr + "Категория:" + item.getCategory() + "\r\n";
            out.write(writeStr);

        }
        out.close();
        fstream.close();

    }
}
