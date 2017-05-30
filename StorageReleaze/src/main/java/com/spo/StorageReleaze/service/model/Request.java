package com.spo.StorageReleaze.service.model;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public class Request {
    public enum RequestName {
        LOAD_ITEM, UNLOAD_ITEM, USER_VALIDATE,
        ADD_USER, SHOW_ALL, EXIT, SEARCH
    }

    private RequestName reqName;
    private User item;
    private StorageItem storageItem;

    public Request(RequestName reqName, User item, StorageItem storageItem) {
        this.reqName = reqName;
        this.item = item;
        this.storageItem = storageItem;
    }

    public RequestName getReqName() {
        return reqName;
    }

    public User getUser() {
        return item;
    }

    public StorageItem getStorageItem() {
        return storageItem;
    }
}
