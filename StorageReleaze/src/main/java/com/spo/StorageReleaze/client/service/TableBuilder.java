package com.spo.StorageReleaze.client.service;

import com.spo.StorageReleaze.service.model.StorageItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Antoha12018 on 19.05.2017.
 */
public class TableBuilder {

    private static TableColumn nameColumn;
    private static TableColumn countColumn;
    private static TableColumn dateColumn;
    private static TableColumn categoryColumn;
    private static TableView table = null;

    public static TableView createTable() {

        table = new TableView();

        nameColumn = new TableColumn("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<StorageItem, String>("name"));
        nameColumn.setPrefWidth(150);
        nameColumn.setSortable(true);

        countColumn = new TableColumn("Количество");
        countColumn.setCellValueFactory(new PropertyValueFactory<StorageItem, Integer>("count"));
        countColumn.setPrefWidth(100);
        countColumn.setSortable(true);

        dateColumn = new TableColumn("Дата поступления");
        dateColumn.setCellValueFactory(new PropertyValueFactory<StorageItem, String>("date"));
        dateColumn.setPrefWidth(138);
        dateColumn.setSortable(true);

        categoryColumn = new TableColumn("Категория");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<StorageItem, String>("category"));
        categoryColumn.setPrefWidth(140);
        categoryColumn.setSortable(true);

        table.getColumns().addAll(nameColumn, countColumn, dateColumn, categoryColumn);

        table.setPrefSize(2000, 1000);


        return table;
    }

    public static TableColumn getNameColumn() {
        return nameColumn;
    }

    public static TableColumn getCountColumn() {
        return countColumn;
    }

    public static TableColumn getDateColumn() {
        return dateColumn;
    }

    public static TableColumn getCategoryColumn() {
        return categoryColumn;
    }
}
