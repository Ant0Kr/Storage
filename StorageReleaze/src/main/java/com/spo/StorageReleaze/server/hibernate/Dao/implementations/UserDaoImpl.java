package com.spo.StorageReleaze.server.hibernate.Dao.implementations;

import com.spo.StorageReleaze.server.hibernate.Dao.interfaces.UserDao;
import com.spo.StorageReleaze.server.hibernate.HibernateUtil;
import com.spo.StorageReleaze.service.model.StorageItem;
import com.spo.StorageReleaze.service.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collection;
import java.util.List;

/**
 * Created by Antoha12018 on 19.05.2017.
 */
public class UserDaoImpl implements UserDao {

    private Session session;
    private static final Logger log = Logger.getLogger(StorageItemDaoImpl.class);

    public UserDaoImpl() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public User getUser(String login) {
        log.info("Get user:" + login + " from DB");
        Query query = session.createQuery("FROM User WHERE login='" + login + "'");
        return (User) query.uniqueResult();
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        log.info("Get all Users from DB");
        List<User> items;
        items = session.createCriteria(User.class).list();
        return items;
    }

    @Override
    public void addUser(User user) throws Exception {
        log.info("Perform add " + user.getLogin() + " into DB.");
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }
}
