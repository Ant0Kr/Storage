package com.spo.StorageReleaze.client.controller;

/**
 * Created by Antoha12018 on 18.05.2017.
 */

import com.spo.StorageReleaze.client.controller.threads.StorageTableThread;
import com.spo.StorageReleaze.client.main.Main;
import com.spo.StorageReleaze.client.main.MainClient;
import com.spo.StorageReleaze.client.service.GeneratePDF;
import com.spo.StorageReleaze.client.service.GenerateTXT;
import com.spo.StorageReleaze.client.service.TableBuilder;
import com.spo.StorageReleaze.service.SerializeMaker;
import com.spo.StorageReleaze.service.model.Request;
import com.spo.StorageReleaze.service.model.StorageItem;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import java.io.*;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;

public class MainController {

    private static Button showBtn;
    private static Button loadBtn;
    private static Button unloadBtn;
    private static Button cancelBtn;
    private static Button exportBtn;
    private static Button exitBtn;
    private static Button searchBtn;
    private static Label timeLbl;
    private static TextField nameField;
    private static TextField countField;
    private static TextField dateField;
    private static TextField categoryField;
    private static TableView table;
    private static AnimationTimer at;
    private static BorderPane pane;
    private static MainClient client;
    private static StorageTableThread thread;
    private static boolean searchFlag = false;
    private static LinkedList<StorageItem> storageList;
    private static LinkedList<StorageItem> cancelList;
    private static Stage dialogStage = null;
    private static StorageItem selectedItem;
    private static final Logger log = Logger.getLogger(MainController.class);


    public static BorderPane getPane(boolean rights, MainClient newClient) throws Exception {
        client = newClient;
        initTop();
        initCenter();
        initBottom();
        initRight();
        if (!rights)
            initUser();
        initTimeThread();
        storageList = new LinkedList<>();
        cancelList = new LinkedList<>();
        thread = new StorageTableThread();
        thread.start();
        return pane;
    }

    private static void initTop() throws FileNotFoundException {
        pane = new BorderPane();
        pane.setPadding(new Insets(20, 20, 20, 20));

        HBox topLayout = new HBox(5);
        topLayout.setPadding(new Insets(0, 0, 0, 20));
        topLayout.setAlignment(Pos.CENTER_LEFT);
        topLayout.setPrefHeight(26.0);
        Image image = new Image(new FileInputStream("D:/Intellij_workspace/StorageReleaze/src/main/resources/css/img/icn.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(66.0);
        imageView.setFitWidth(64.0);
        timeLbl = new Label();
        timeLbl.setAlignment(Pos.BOTTOM_LEFT);
        timeLbl.setPrefHeight(52.0);
        timeLbl.setFont(new Font(17.0));
        topLayout.getChildren().addAll(imageView, timeLbl);

        pane.setTop(topLayout);

    }

    private static void initCenter() throws Exception {
        VBox center = new VBox();
        VBox tableLayout = new VBox();
        tableLayout.setPadding(new Insets(5, 20, 10, 10));
        table = TableBuilder.createTable();
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    selectedItem = (StorageItem) table.getSelectionModel().getSelectedItem();
                    unloadBtn.setDisable(false);
                }
            }
        });
        tableLayout.getChildren().add(table);
        center.getChildren().addAll(tableLayout);

        pane.setCenter(center);
    }

    private static void initBottom() {
        HBox bottom = new HBox(10);
        bottom.setAlignment(Pos.CENTER);
        nameField = new TextField();
        countField = new TextField();
        countField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!countField.getText().matches("[0-9]{0,10}")) {
                    countField.setText("");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Сообщение");
                    alert.setHeaderText("Ошибка!");
                    alert.setContentText("Пожалуйста! Введите целочисленное значение в поле 'количество'.\n" +
                            "(до 10 млрд)");
                    alert.showAndWait();

                }
            }

        });
        dateField = new TextField();
        categoryField = new TextField();
        nameField.setPromptText("-название-");
        countField.setPromptText("-количество-");
        dateField.setPromptText("-дата поступления-");
        categoryField.setPromptText("-категория-");
        searchBtn = new Button("Search");
        searchBtn.setPrefWidth(80);
        searchBtn.setOnAction(searchAction());
        bottom.getChildren().addAll(nameField, countField, dateField, categoryField, searchBtn);

        pane.setBottom(bottom);
    }

    private static void initRight() {
        VBox right = new VBox(5);
        right.setAlignment(Pos.TOP_CENTER);
        right.setPadding(new Insets(5, 10, 0, 0));
        showBtn = new Button("Показать все");
        showBtn.setOnAction(showAction());
        loadBtn = new Button("Загрузка");
        loadBtn.setOnAction(loadAction());
        unloadBtn = new Button("Выгрузка");
        unloadBtn.setDisable(true);
        unloadBtn.setOnAction(unloadAction());
        cancelBtn = new Button("Отмена");
        exportBtn = new Button("Экспорт...");
        exportBtn.setOnAction(exportAction());
        exitBtn = new Button("Выход");
        exitBtn.setOnAction(exitAction());
        showBtn.setPrefWidth(90);
        loadBtn.setPrefWidth(90);
        unloadBtn.setPrefWidth(90);
        cancelBtn.setPrefWidth(90);
        cancelBtn.setOnAction(cancelAction());
        exportBtn.setPrefWidth(90);
        exitBtn.setPrefWidth(90);

        right.getChildren().addAll(exitBtn, showBtn, exportBtn, loadBtn, unloadBtn, cancelBtn);
        pane.setRight(right);
    }

    private static void initUser() {
        loadBtn.setVisible(false);
        unloadBtn.setVisible(false);
        cancelBtn.setVisible(false);
    }

    private static void initTimeThread() {
        at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                timeLbl.setText("" + Calendar.getInstance().getTime() + "");
            }
        };
        at.start();
    }

    /**
     * Actions
     **/
    public static EventHandler<ActionEvent> unloadAction() {
        return event -> {
            if (event.getSource() == unloadBtn) {
                Scene scene = new Scene(DialogController.getUnloadPane(), 210, 80);
                scene.getStylesheets().add
                        (Main.class.getResource("/css/style.css").toExternalForm());

                if (dialogStage == null)
                    dialogStage = new Stage();

                dialogStage.setScene(scene);
                dialogStage.setResizable(false);
                dialogStage.show();
                dialogStage.setAlwaysOnTop(true);
                unloadBtn.setDisable(true);
                selectedItem.setOperation(true);
                cancelList.addFirst(selectedItem);
                log.info("Unload request was handle.");
            }
        };
    }

    public static EventHandler<ActionEvent> exitAction() {
        return e -> {
            if (e.getSource() == exitBtn) {
                log.info("User with login:" + client.getUser().getLogin() + " exit from system.");
                synchronized (client) {
                    Request request = new Request(Request.RequestName.EXIT, null, null);
                    try {
                        client.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
                thread.interrupt();
                at.stop();
                searchFlag = false;
                table = null;
                storageList.clear();
                storageList = null;

                Scene scene = new Scene(InputController.getPane(), 240, 130);
                Main.get_stage().setResizable(false);
                scene.getStylesheets().add
                        (Main.class.getResource("/css/style.css").toExternalForm());
                Main.get_stage().setScene(scene);

            }

        };
    }

    public static EventHandler<ActionEvent> showAction() {
        return e -> {
            if (e.getSource() == showBtn) {
                searchFlag = false;
                log.info("Show all action was handle.");
            }


        };
    }

    public static EventHandler<ActionEvent> exportAction() {
        return e -> {
            if (e.getSource() == exportBtn) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Экспорт");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF", "*.pdf");
                FileChooser.ExtensionFilter extFilter1 = new FileChooser.ExtensionFilter("TXT", "*.txt");
                fileChooser.getExtensionFilters().addAll(extFilter, extFilter1);
                File file = fileChooser.showSaveDialog(Main.get_stage());
                if (file != null) {
                    String fileName = file.getName();
                    if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                        fileName = fileName.substring(fileName.lastIndexOf("."));
                    if (file != null && fileName.equals(".pdf")) {

                        try {
                            GeneratePDF.createPDF(file.toString());
                            log.info("User export table into .pdf format.");
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (com.itextpdf.text.DocumentException e1) {
                            e1.printStackTrace();
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }


                    } else if (file != null && fileName.equals(".txt")) {
                        try {
                            GenerateTXT.GenerateTXT(file.toString());
                            log.info("User export table into .txt format.");

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }

        };
    }

    public static EventHandler<ActionEvent> searchAction() {
        return e -> {
            if (e.getSource() == searchBtn) {

                if (nameField.getText().equals("") && countField.getText().equals("") &&
                        dateField.getText().equals("") && categoryField.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Сообщение");
                    alert.setHeaderText("Ошибка!");
                    alert.setContentText("Поля для поиска пусты.Пожалуйста, введите значения.");
                    alert.showAndWait();
                } else {
                    Request request = new Request(Request.RequestName.SEARCH, null, new StorageItem(
                            nameField.getText(), 0,
                            dateField.getText(), categoryField.getText()));
                    if (!countField.getText().equals(""))
                        request.getStorageItem().setCount(Integer.parseInt(countField.getText()));

                    try {
                        client.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                        String response = client.getInputStream().readUTF();
                        synchronized (storageList) {
                            storageList = SerializeMaker.deserializeFromXML(response);
                        }
                        log.info(storageList.size());
                        searchFlag = true;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }

                log.info("Search request was handle.");
            }

        };
    }

    public static EventHandler<ActionEvent> loadAction() {
        return e -> {
            if (e.getSource() == loadBtn) {

                Scene scene = new Scene(DialogController.getLoadPane(), 255, 160);
                scene.getStylesheets().add
                        (Main.class.getResource("/css/style.css").toExternalForm());

                if (dialogStage == null)
                    dialogStage = new Stage();
                dialogStage.setScene(scene);
                dialogStage.setResizable(false);
                dialogStage.show();
                dialogStage.setAlwaysOnTop(true);
                log.info("Load request was handle.");
            }

        };
    }

    public static EventHandler<ActionEvent> cancelAction() {
        return e -> {
            if (e.getSource() == cancelBtn) {

                if (cancelList.size() == 0)
                    return;
                else {
                    StorageItem item = cancelList.pollFirst();
                    if (item.getOperation()) {
                        Request request = new Request(Request.RequestName.LOAD_ITEM, null, item);

                        synchronized (client) {
                            try {
                                client.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        Request request = new Request(Request.RequestName.UNLOAD_ITEM, null, item);
                        String response;
                        int state;
                        synchronized (client) {
                            try {
                                client.getOutputStream().writeUTF(
                                        SerializeMaker.serializeToXML(request));
                                response = MainController.getClient().getInputStream().readUTF();
                                state = SerializeMaker.deserializeFromXML(response);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
                log.info("Cancel request was handle.");
            }

        };
    }


    public static void changeStorageTable() {

        ObservableList<StorageItem> item = FXCollections.observableArrayList();
        for (int i = 0; i < storageList.size(); i++) {
            item.addAll(storageList.get(i));
        }
        table.setItems(item);
    }

    /**
     * Getters and setters
     **/
    public static MainClient getClient() {
        return client;
    }

    public static boolean getSearchFlag() {
        return searchFlag;
    }

    public static LinkedList<StorageItem> getStorageList() {
        return storageList;
    }


    public static void setStorageList(LinkedList<StorageItem> list) {
        storageList = list;
    }

    public static TableView getStorageTable() {
        return table;
    }

    public static Stage getDialogStage() {
        return dialogStage;
    }

    public static LinkedList<StorageItem> getCancelList() {
        return cancelList;
    }

    public static StorageItem getSelectedItem() {
        return selectedItem;
    }
}
