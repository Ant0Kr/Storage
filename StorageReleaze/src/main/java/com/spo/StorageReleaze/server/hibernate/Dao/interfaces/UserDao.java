package com.spo.StorageReleaze.server.hibernate.Dao.interfaces;

import com.spo.StorageReleaze.service.model.User;

import java.util.List;

/**
 * Created by Antoha12018 on 19.05.2017.
 */
public interface UserDao {
    User getUser(String login);

    List<User> getAllUsers() throws Exception;

    void addUser(User user) throws Exception;
}
