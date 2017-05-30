package com.spo.StorageReleaze.service.model;

import javax.persistence.*;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "rights")
    private String rights;

    public User(String login, String password, String rights) {
        this.login = login;
        this.password = password;
        this.rights = rights;
    }

    public User(int id, String login, String password, String rights) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.rights = rights;
    }

    public User(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.rights = user.getRights();
    }

    public void setUser(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.rights = user.getRights();
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
