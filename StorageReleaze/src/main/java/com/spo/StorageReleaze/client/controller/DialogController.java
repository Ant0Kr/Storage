package com.spo.StorageReleaze.client.controller;

import com.spo.StorageReleaze.client.main.Main;
import com.spo.StorageReleaze.server.main.MainServer;
import com.spo.StorageReleaze.service.SerializeMaker;
import com.spo.StorageReleaze.service.model.Request;
import com.spo.StorageReleaze.service.model.StorageItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Antoha12018 on 19.05.2017.
 */
public class DialogController {

    private static Label name_lbl;
    private static Label count_lbl;
    private static Label category_lbl;
    private static TextField name_field;
    private static TextField count_field;
    private static TextField category_field;
    private static Button ok_btn;
    private static Button okUnload;

    public static BorderPane getLoadPane() {

        ok_btn = new Button("Ok");
        ok_btn.setPrefSize(70, 0);
        ok_btn.setDisable(true);
        ok_btn.setOnAction(loadAction());
        name_lbl = new Label("Название");
        count_lbl = new Label("Количество");
        category_lbl = new Label("Категория");
        name_field = new TextField();
        count_field = new TextField();

        category_field = new TextField();
        name_field.setPromptText("-название-");
        count_field.setPromptText("-количество-");
        category_field.setPromptText("-категория-");
        name_field.lengthProperty().addListener(input_Listener());
        count_field.lengthProperty().addListener(input_Listener());
        category_field.lengthProperty().addListener(input_Listener());
        VBox labelBox = new VBox(20);
        labelBox.getChildren().addAll(name_lbl, count_lbl, category_lbl);

        VBox fieldBox = new VBox(10);
        fieldBox.getChildren().addAll(name_field, count_field, category_field);

        HBox preFinish = new HBox(10);
        preFinish.getChildren().addAll(labelBox, fieldBox);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().add(ok_btn);
        buttonBox.setPadding(new Insets(10, 0, 0, 80));

        VBox finishBox = new VBox(5);
        finishBox.getChildren().addAll(preFinish, buttonBox);
        finishBox.setPadding(new Insets(20, 0, 0, 20));

        BorderPane pane = new BorderPane();
        pane.setCenter(finishBox);

        return pane;

    }

    public static BorderPane getUnloadPane() {
        count_lbl = new Label("Количество");
        count_field = new TextField();
        count_field.setPromptText("-количество-");
        count_field.setPrefWidth(100);
        okUnload = new Button("Ок");
        okUnload.setPrefSize(70, 0);
        okUnload.setOnAction(unloadAction());

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(count_lbl, count_field);

        HBox btnBox = new HBox();
        btnBox.getChildren().addAll(okUnload);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(0, 0, 0, 0));

        VBox finish = new VBox(5);
        finish.getChildren().addAll(hBox, btnBox);

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20, 20, 20, 20));
        pane.setCenter(finish);
        return pane;
    }

    public static ChangeListener<Number> input_Listener() {

        return (arg0, arg1, arg2) -> {
            if (category_field.getText().isEmpty() || name_field.getText().isEmpty()
                    || count_field.getText().isEmpty()) {
                ok_btn.setDisable(true);
            } else {
                ok_btn.setDisable(false);
            }
        };
    }

    public static EventHandler<ActionEvent> loadAction() {
        return e -> {
            if (e.getSource() == ok_btn) {

                if (!count_field.getText().matches("[0-9]{0,10}")) {
                    count_field.setText("");
                    MainController.getDialogStage().setAlwaysOnTop(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Сообщение");
                    alert.setHeaderText("Ошибка!");
                    alert.setContentText("Пожалуйста! Введите целочисленное значение в поле 'количество'.\n" +
                            "(до 10 млрд)");
                    alert.showAndWait();
                    MainController.getDialogStage().setAlwaysOnTop(true);
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                int Date = calendar.get(Calendar.DAY_OF_MONTH);
                int Month = calendar.get(Calendar.MONTH);
                Month++;
                int Year = calendar.get(Calendar.YEAR);
                String date = Integer.toString(Date) + "-" + Integer.toString(Month) + "-" + Integer.toString(Year);

                StorageItem item = new StorageItem(name_field.getText(), Integer.parseInt(count_field.getText()),
                        date, category_field.getText());
                item.setOperation(false);
                Request request = new Request(Request.RequestName.LOAD_ITEM, null, item);
                MainController.getCancelList().addFirst(item);
                synchronized (MainController.getStorageList()) {
                    MainController.getStorageList().add(item);
                }
                synchronized (MainController.getClient()) {
                    try {
                        MainController.getClient().getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                name_field.clear();
                count_field.clear();
                category_field.clear();
                MainController.getDialogStage().hide();

            }

        };
    }

    public static EventHandler<ActionEvent> unloadAction() {
        return e -> {
            if (e.getSource() == okUnload) {

                StorageItem item = MainController.getSelectedItem();
                Request request = new Request(Request.RequestName.UNLOAD_ITEM, null, item);
                if (!count_field.getText().matches("[0-9]{0,10}")) {
                    MainController.getDialogStage().setAlwaysOnTop(false);
                    count_field.setText("");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Сообщение");
                    alert.setHeaderText("Ошибка!");
                    alert.setContentText("Пожалуйста! Введите целочисленное значение в поле 'количество'.\n" +
                            "(до 10 млрд)");
                    alert.showAndWait();
                    MainController.getDialogStage().setAlwaysOnTop(true);
                    return;
                }
                request.getStorageItem().setCount(Integer.parseInt(count_field.getText()));
                String response;
                int state = 0;
                synchronized (MainController.getClient()) {
                    try {
                        MainController.getClient().getOutputStream().writeUTF(
                                SerializeMaker.serializeToXML(request));
                        response = MainController.getClient().getInputStream().readUTF();
                        state = SerializeMaker.deserializeFromXML(response);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (state == 1 || state == 2) {
                    MainController.getDialogStage().hide();
                    count_field.clear();
                } else {
                    MainController.getDialogStage().setAlwaysOnTop(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Сообщение");
                    alert.setHeaderText("Ошибка!");
                    alert.setContentText("Склад не содержит такого количества едениц.");
                    alert.showAndWait();
                }

            }

        };
    }
}
