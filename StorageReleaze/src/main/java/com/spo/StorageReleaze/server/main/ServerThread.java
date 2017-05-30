package com.spo.StorageReleaze.server.main;

import com.spo.StorageReleaze.client.controller.MainController;
import com.spo.StorageReleaze.server.hibernate.Factory;
import com.spo.StorageReleaze.service.SerializeMaker;
import com.spo.StorageReleaze.service.model.Request;
import com.spo.StorageReleaze.service.model.StorageItem;
import com.spo.StorageReleaze.service.model.User;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
@SuppressWarnings("ALL")
public class ServerThread extends Thread {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static final Logger log = Logger.getLogger(ServerThread.class);

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(this.socket.getInputStream());
        out = new DataOutputStream(this.socket.getOutputStream());
    }

    public void run() {

        while (true) {
            String serializeObj;
            Request.RequestName requestName;
            try {
                serializeObj = in.readUTF();

            } catch (IOException e) {
                return;
            }
            Request request = SerializeMaker.deserializeFromXML(serializeObj);
            requestName = request.getReqName();
            log.info(request.getReqName());

            if (requestName == Request.RequestName.USER_VALIDATE)
                userValid(request.getUser());
            else if (requestName == Request.RequestName.ADD_USER)
                addUser(request.getUser());
            else if (requestName == Request.RequestName.EXIT)
                return;
            else if (requestName == Request.RequestName.SHOW_ALL)
                showAll();
            else if (requestName == Request.RequestName.SEARCH)
                search(request.getStorageItem());
            else if (requestName == Request.RequestName.LOAD_ITEM)
                loadItem(request.getStorageItem());
            else if (requestName == Request.RequestName.UNLOAD_ITEM)
                unload(request.getStorageItem());

        }

    }

    private void userValid(User user) {
        boolean flag = false;
        LinkedList<User> list;
        synchronized (MainServer.getUserCatalog()) {
            list = MainServer.getUserCatalog();
        }
        if (list.size() != 0) {
            User check;
            for (int i = 0; i < list.size(); i++) {
                check = list.get(i);
                if (check.getLogin().equals(user.getLogin()) && check.getPassword().equals(user.getPassword())) {
                    flag = true;
                    user = check;
                    break;
                }
            }

        }

        if (!flag) {
            user.setLogin("ERROR");
        }
        try {
            out.writeUTF(SerializeMaker.serializeToXML(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addUser(User user) {
        User checkUser = Factory.getInstance().getUserDAO().getUser(user.getLogin());
        if (checkUser != null) {
            boolean flag = false;
            try {
                out.writeUTF(SerializeMaker.serializeToXML(flag));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            boolean flag = true;
            try {
                out.writeUTF(SerializeMaker.serializeToXML(flag));
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (MainServer.getUserCatalog()) {
                MainServer.getUserCatalog().add(user);
            }
            try {
                Factory.getInstance().getUserDAO().addUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void showAll() {

        synchronized (MainServer.getStorage()) {
            try {
                out.writeUTF(SerializeMaker.serializeToXML(
                        MainServer.getStorage()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void search(StorageItem item) {

        LinkedList<StorageItem> myList;
        synchronized (MainServer.getStorage()) {
            myList = MainServer.getStorage();
        }
        LinkedList<StorageItem> finishList = new LinkedList<>();
        synchronized (myList) {

            for (int i = 0; i < myList.size(); i++) {
                if (!item.getName().equals("")) {
                    int size = item.getName().length();
                    boolean flag = false;
                    for(int j = 0;j<size;j++){
                        if(item.getName().charAt(j) == myList.get(i).getName().charAt(j)){
                            continue;
                        }

                        else{
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                        continue;
                    /*if (!myList.get(i).getName().equals(item.getName()))
                        continue;*/
                }
                if (item.getCount() != 0) {
                    if (myList.get(i).getCount() > item.getCount()+200 ||
                            myList.get(i).getCount() < item.getCount()-200)
                        continue;
                }
                if (!item.getDate().equals("")) {
                    if (!myList.get(i).getDate().equals(item.getDate()))
                        continue;
                }
                if (!item.getCategory().equals("")) {
                    int size = item.getCategory().length();
                    boolean flag = false;
                    for(int j = 0;j<size;j++){
                        if(item.getCategory().charAt(j) == myList.get(i).getCategory().charAt(j)){
                            continue;
                        }

                        else{
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                        continue;

                    /*if (!myList.get(i).getCategory().equals(item.getCategory()))
                        continue;*/
                }
                finishList.add(myList.get(i));
            }
            try {
                out.writeUTF(SerializeMaker.serializeToXML(finishList));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void loadItem(StorageItem item) {

        StorageItem itm = Factory.getInstance().getItemDAO().getItem(item.getName(), item.getCategory());
        int count;
        if (itm != null) {
            count = itm.getCount() + item.getCount();
            itm.setCount(count);
            Calendar calendar = Calendar.getInstance();
            int Date = calendar.get(Calendar.DAY_OF_MONTH);
            int Month = calendar.get(Calendar.MONTH);
            Month++;
            int Year = calendar.get(Calendar.YEAR);
            String date = Integer.toString(Date) + "-" + Integer.toString(Month) + "-" + Integer.toString(Year);
            itm.setDate(date);
            synchronized (MainServer.getStorage()) {
                for (int i = 0; i < MainServer.getStorage().size(); i++) {
                    if (MainServer.getStorage().get(i).getName().equals(item.getName())) {
                        MainServer.getStorage().get(i).setCount(count);
                        MainServer.getStorage().get(i).setDate(date);
                        break;
                    }

                }

            }
            try {
                Factory.getInstance().getItemDAO().loadItem(itm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            synchronized (MainServer.getStorage()) {
                MainServer.getStorage().add(item);
            }
            try {
                Factory.getInstance().getItemDAO().loadItem(item);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void unload(StorageItem item) {
        StorageItem itm = Factory.getInstance().getItemDAO().getItem(item.getName(), item.getCategory());
        int state = 0;
        if (itm.getCount() < item.getCount()) {
            state = 0;
            try {
                out.writeUTF(SerializeMaker.serializeToXML(state));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (itm.getCount() == item.getCount()) {
            try {
                Factory.getInstance().getItemDAO().deleteItem(itm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (MainServer.getStorage()) {
                for (int i = 0; i < MainServer.getStorage().size(); i++) {
                    if (itm.getCategory().equals(MainServer.getStorage().get(i).getCategory())) {
                        if (itm.getName().equals(MainServer.getStorage().get(i).getName())) {
                            MainServer.getStorage().remove(i);
                            break;
                        }
                    }
                }
            }

            state = 1;
            try {
                out.writeUTF(SerializeMaker.serializeToXML(state));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            int count = itm.getCount() - item.getCount();
            itm.setCount(count);
            Calendar calendar = Calendar.getInstance();
            int Date = calendar.get(Calendar.DAY_OF_MONTH);
            int Month = calendar.get(Calendar.MONTH);
            Month++;
            int Year = calendar.get(Calendar.YEAR);
            String date = Integer.toString(Date) + "-" + Integer.toString(Month) + "-" + Integer.toString(Year);
            itm.setDate(date);
            synchronized (MainServer.getStorage()) {
                for (int i = 0; i < MainServer.getStorage().size(); i++) {
                    if (itm.getCategory().equals(MainServer.getStorage().get(i).getCategory())) {
                        if (itm.getName().equals(MainServer.getStorage().get(i).getName())) {
                            MainServer.getStorage().get(i).setDate(date);
                            MainServer.getStorage().get(i).setCount(count);
                            break;
                        }
                    }
                }
            }

            try {
                Factory.getInstance().getItemDAO().loadItem(itm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            state = 2;
            try {
                out.writeUTF(SerializeMaker.serializeToXML(state));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}