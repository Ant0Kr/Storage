package com.spo.StorageReleaze.client.controller;

import com.spo.StorageReleaze.client.main.Main;
import com.spo.StorageReleaze.client.main.MainClient;
import com.spo.StorageReleaze.service.SerializeMaker;
import com.spo.StorageReleaze.service.model.Request;
import com.spo.StorageReleaze.service.model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Antoha12018 on 19.05.2017.
 */
public class InputController {

    private static MainClient client;
    private static Label log_label;
    private static Label pass_label;
    private static TextField log_field;
    private static PasswordField pass_field;
    private static TextField regLogField;
    private static TextField regPassField;
    private static Button sign_btn;
    private static Button regBtn;
    private static Button goBtn;
    private static Button backBtn;
    private static BorderPane pane;
    private static final Logger log = Logger.getLogger(InputController.class);

    public static BorderPane getPane() {
        log_label = new Label("Логин");
        pass_label = new Label("Пароль");
        sign_btn = new Button("Войти");
        regBtn = new Button("Регистрация");
        sign_btn.setOnAction(signAction());
        regBtn.setPrefWidth(90);
        regBtn.setOnAction(regAction());
        regBtn.setAlignment(Pos.BOTTOM_LEFT);

        log_field = new TextField();
        pass_field = new PasswordField();

        VBox label_layout = new VBox(13);
        label_layout.getChildren().addAll(log_label, pass_label);

        VBox line_layout = new VBox(5);
        line_layout.getChildren().addAll(log_field, pass_field);

        HBox V_layout = new HBox(8);
        V_layout.getChildren().addAll(label_layout, line_layout);

        HBox hor_layout = new HBox(62);
        hor_layout.getChildren().addAll(regBtn, sign_btn);

        VBox fin_layout = new VBox(10);
        fin_layout.getChildren().addAll(V_layout, hor_layout);

        pane = new BorderPane();
        pane.setCenter(fin_layout);
        pane.setPadding(new Insets(30, 0, 0, 20));

        return pane;
    }


    public static EventHandler<ActionEvent> regAction() {

        return e -> {

            backBtn = new Button("Назад");
            backBtn.setPrefWidth(70);
            backBtn.setOnAction(backAction());
            goBtn = new Button("Зарегистрироваться");
            goBtn.setDisable(true);
            goBtn.setPrefWidth(130);
            goBtn.setOnAction(goAction());
            regLogField = new TextField();
            regPassField = new TextField();
            log_label = new Label("Логин");
            pass_label = new Label("Пароль");
            regLogField.lengthProperty().addListener(regListener());
            regPassField.lengthProperty().addListener(regListener());

            VBox label_layout = new VBox(13);
            label_layout.getChildren().addAll(log_label, pass_label);

            VBox line_layout = new VBox(5);
            line_layout.getChildren().addAll(regLogField, regPassField);

            HBox V_layout = new HBox(8);
            V_layout.getChildren().addAll(label_layout, line_layout);

            HBox hor_layout = new HBox(34);
            Label spaceLabel = new Label();
            hor_layout.getChildren().addAll(spaceLabel, goBtn);

            VBox fin_layout = new VBox(7);
            fin_layout.getChildren().addAll(backBtn, V_layout, hor_layout);

            BorderPane border = new BorderPane();
            border.setCenter(fin_layout);
            border.setPadding(new Insets(10, 0, 0, 20));

            Scene scene = new Scene(border, 240, 145);
            scene.getStylesheets().add
                    (Main.class.getResource("/css/style.css").toExternalForm());
            Main.get_stage().setScene(scene);

        };
    }

    public static EventHandler<ActionEvent> backAction() {

        return e -> {

            Scene scene = new Scene(getPane(), 240, 130);
            scene.getStylesheets().add
                    (Main.class.getResource("/css/style.css").toExternalForm());
            Main.get_stage().setScene(scene);
        };
    }

    public static EventHandler<ActionEvent> signAction() {

        return e -> {

            if ((pass_field.getText().equals("") || log_field.getText().equals("")) ||
                    (pass_field.getText().equals("") && log_field.getText().equals(""))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Сообщение");
                alert.setHeaderText("Ошибка!");
                alert.setContentText("Пожалуйста, заполните поля ввода.");
                alert.showAndWait();
            } else {

                client = new MainClient();
                try {
                    if (client.ClientConnection()) {

                        Request request = new Request(Request.RequestName.USER_VALIDATE, new User(
                                log_field.getText(), pass_field.getText(), null), null);
                        client.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                        String response = client.getInputStream().readUTF();
                        client.setUser(SerializeMaker.deserializeFromXML(response));

                        if (!client.getUser().getLogin().equals("ERROR")) {
                            Scene scene;
                            if (client.getUser().getRights().equals("ADMIN"))
                                scene = new Scene(MainController.getPane(true, client), 700, 550);
                            else
                                scene = new Scene(MainController.getPane(false, client), 700, 550);
                            scene.getStylesheets().add
                                    (Main.class.getResource("/css/style.css").toExternalForm());
                            Main.get_stage().setScene(scene);
                            Main.get_stage().setResizable(true);
                            log.info("User:" + client.getUser().getLogin() + " perform entrance into system!");

                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Сообщение");
                            alert.setHeaderText("Ошибка!");
                            alert.setContentText("Пользователя с таким логином и паролем не существует.");
                            alert.showAndWait();
                        }

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }


        };
    }

    public static EventHandler<ActionEvent> goAction() {

        return e -> {

            if (regLogField.getText().length() > 4 && regPassField.getText().length() > 4) {

                try {
                    client = new MainClient();
                    if (client.ClientConnection()) {
                        Request request = new Request(Request.RequestName.ADD_USER, new User(regLogField.getText(),
                                regPassField.getText(), "USER"), null);
                        client.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                String response = null;
                try {
                    response = client.getInputStream().readUTF();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                boolean flag = SerializeMaker.deserializeFromXML(response);
                if (flag) {
                    client.setUser(new User(regLogField.getText(), regPassField.getText(), "USER"));
                    Scene scene = null;
                    try {
                        scene = new Scene(MainController.getPane(false, client), 700, 550);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    scene.getStylesheets().add
                            (Main.class.getResource("/css/style.css").toExternalForm());
                    Main.get_stage().setScene(scene);
                    Main.get_stage().setResizable(true);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Сообщение");
                    alert.setHeaderText("Ошибка!");
                    alert.setContentText("Пользователь с таким логином уже зарегистрирован.");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Сообщение");
                alert.setHeaderText("Ошибка!");
                alert.setContentText("Длина логина и пароля должна быть\nбольше 4 символов.");
                alert.showAndWait();
            }

        };
    }

    public static ChangeListener<Number> regListener() {

        return (arg0, arg1, arg2) -> {
            if (!regLogField.getText().equals("") && !regPassField.getText().equals("")) {
                goBtn.setDisable(false);
            } else {
                goBtn.setDisable(true);
            }

        };
    }
}
