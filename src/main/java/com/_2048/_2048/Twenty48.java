package com._2048._2048;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Twenty48 extends Application {

    @Override
    public void start(Stage stage) {


        GridPane gridPane = new GridPane();

        UI ui = new UI(gridPane);

        ui.generateBoard();

        for (Tile tile : ui.getTiles()) {
            gridPane.add(tile.getTile(), tile.getXPos(), tile.getYPos());
        }

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setMinSize(445,500);
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        Scene scene = new Scene(gridPane);

        stage.setTitle("2048");
        stage.setScene(scene);
        stage.show();

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        scene.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), Boolean.TRUE));

        final boolean[] keyIsDown = {false};
        scene.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), Boolean.FALSE));
        scene.setOnKeyReleased(
                e -> {
                        pressedKeys.put(e.getCode(), false);
                        keyIsDown[0]=false; // IMPORTANT!!!
                    System.out.println(ui.checkForValidMoves());
                    }
        );

        ui.addTile();
        ui.addTile();
        ui.updateSquares(gridPane);


        new AnimationTimer() {

            @Override
            public void handle(long now) {

                if(pressedKeys.getOrDefault(KeyCode.SPACE, false)) {
                    if (!keyIsDown[0]) {
                        System.out.println("space");
                        ui.updateSquares(gridPane);
                        ui.addTile();
                        ui.updateSquares(gridPane);
                    }
                    keyIsDown[0] = true;
                }
                if (pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
                    if (!keyIsDown[0]) {
                        ui.move("DOWN", gridPane);
                    }
                    keyIsDown[0] = true;
                }
                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    if (!keyIsDown[0]){
                        ui.move("UP", gridPane);
                    }
                    keyIsDown[0] = true;
                }
                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    if (!keyIsDown[0]) {
                        ui.move("RIGHT", gridPane);
                    }
                    keyIsDown[0] = true;
                }
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    if (!keyIsDown[0]) {
                        ui.move("LEFT", gridPane);
                    }
                    keyIsDown[0] = true;
                }
            }
            }.start();
    }


    public static void main(String[] args) {
        launch();
    }
}