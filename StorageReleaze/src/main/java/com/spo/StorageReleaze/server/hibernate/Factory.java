package com.spo.StorageReleaze.server.hibernate;

import com.spo.StorageReleaze.server.hibernate.Dao.implementations.StorageItemDaoImpl;
import com.spo.StorageReleaze.server.hibernate.Dao.implementations.UserDaoImpl;
import com.spo.StorageReleaze.server.hibernate.Dao.interfaces.StorageItemDao;
import com.spo.StorageReleaze.server.hibernate.Dao.interfaces.UserDao;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public class Factory {
    private static Factory instance = null;
    private static StorageItemDao itemDao = null;
    private static UserDao userDao = null;


    public static synchronized Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public StorageItemDao getItemDAO() {
        if (itemDao == null) {
            itemDao = new StorageItemDaoImpl();
        }
        return itemDao;
    }

    public UserDao getUserDAO() {
        if (userDao == null) {
            userDao = new UserDaoImpl();
        }
        return userDao;
    }
}

