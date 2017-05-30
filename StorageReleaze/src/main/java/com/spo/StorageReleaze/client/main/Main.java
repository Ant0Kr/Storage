package com.spo.StorageReleaze.client.main;

import com.spo.StorageReleaze.client.controller.DialogController;
import com.spo.StorageReleaze.client.controller.InputController;
import com.spo.StorageReleaze.client.controller.MainController;
import com.spo.StorageReleaze.client.service.TableBuilder;
import javafx.application.Application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    private static Stage stage;

    public void start(Stage primaryStage) throws Exception {
        stage = new Stage();

        Scene scene = new Scene(InputController.getPane(), 240, 130);
        scene.getStylesheets().add
                (Main.class.getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Склад");
        stage.setResizable(false);
        stage.show();

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (TableBuilder.getNameColumn() != null && stage.isResizable()) {
                double value = ((double) newValue) - 170;
                TableBuilder.getNameColumn().setPrefWidth(value / 528 * 150);
                TableBuilder.getCountColumn().setPrefWidth(value / 528 * 100);
                TableBuilder.getDateColumn().setPrefWidth(value / 528 * 138);
                TableBuilder.getCategoryColumn().setPrefWidth(value / 528 * 140);
            }
        });


    }

    public static Stage get_stage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


