package com.spo.StorageReleaze.service.model;

import javax.persistence.*;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
@Entity
@Table(name = "storage")
public class StorageItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "count")
    private int count;

    @Column(name = "date")
    private String date;


    @Column(name = "category")
    private String category;


    @Transient
    private boolean operation;

    public StorageItem(String name, int count, String date, String category) {
        this.name = name;
        this.count = count;
        this.date = date;
        this.category = category;
    }

    public StorageItem(int id, String name, int count, String date, String category) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.date = date;
        this.category = category;
    }

    public StorageItem(StorageItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.count = item.getCount();
        this.date = item.getDate();
        this.category = item.getCategory();

    }

    public StorageItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOperation(Boolean operation) {
        this.operation = operation;
    }

    public Boolean getOperation() {
        return operation;
    }
}
