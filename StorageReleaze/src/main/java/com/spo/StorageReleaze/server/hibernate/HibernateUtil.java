package com.spo.StorageReleaze.server.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    private static final Logger log = Logger.getLogger(HibernateUtil.class);

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable e) {
            log.info("Initial SessionFactory creation failed.");
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}