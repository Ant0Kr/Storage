package com.spo.StorageReleaze.server.hibernate.Dao.interfaces;

import com.spo.StorageReleaze.service.model.StorageItem;
import javafx.collections.ObservableList;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public interface StorageItemDao {
    void loadItem(StorageItem item) throws Exception;

    ObservableList<StorageItem> getAllItems() throws Exception;

    void deleteItem(StorageItem item) throws Exception;

    StorageItem getItem(String name, String category);
}
