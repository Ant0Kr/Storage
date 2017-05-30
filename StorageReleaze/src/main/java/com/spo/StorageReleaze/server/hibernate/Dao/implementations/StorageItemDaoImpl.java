package com.spo.StorageReleaze.server.hibernate.Dao.implementations;

import com.spo.StorageReleaze.server.hibernate.Dao.interfaces.StorageItemDao;
import com.spo.StorageReleaze.server.hibernate.Factory;
import com.spo.StorageReleaze.server.hibernate.HibernateUtil;
import com.spo.StorageReleaze.service.model.StorageItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public class StorageItemDaoImpl implements StorageItemDao {

    private Session session;
    private static final Logger log = Logger.getLogger(StorageItemDaoImpl.class);

    public StorageItemDaoImpl() {
        session = HibernateUtil.getSessionFactory().openSession();
    }


    @Override
    public void loadItem(StorageItem item) throws Exception {
        log.info("Perform add " + item.getName() + " items into DB.");
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
    }

    @Override
    public ObservableList<StorageItem> getAllItems() throws Exception {
        log.info("Get all items from DB");
        List<StorageItem> items;
        items = session.createCriteria(StorageItem.class).list();
        ObservableList<StorageItem> list = FXCollections.observableArrayList();
        for (int i = 0; i < items.size(); i++)
            list.add(items.get(i));
        return list;
    }

    @Override
    public void deleteItem(StorageItem item) throws Exception {
        log.info("Delete item:" + item.getName() + " from DB.");
        session.beginTransaction();
        session.delete(item);
        session.getTransaction().commit();
    }

    @Override
    public StorageItem getItem(String name, String category) {
        log.info("Perform get item from database on:" + name + " name.");
        Query query = session.createQuery("FROM StorageItem WHERE name='" + name + "' and category='" + category + "'");
        return (StorageItem) query.uniqueResult();
    }

}
