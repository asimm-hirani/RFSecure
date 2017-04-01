package ui;

import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

import javafx.stage.Stage;
import javafx.util.Pair;

import serial.SerialReader;
import webio.RFSecureClient;

public class Main extends Application {

    private Stage primaryStage;
    private Scene startScene;
    private Group group;
    private RFSecureClient client;

    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 400;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("RFSecure Log In");
        group = new Group();
        setupStartScene();
    }

    /**
     * Sets startScene and adds elements to it. startScene is composed of:
     * 1) A title label
     * 2) A group of radio buttons for setting the game mode
     * 3) A button that when pressed will call setupGameScene and call play
     */// Create the custom dialog.
    private void setupStartScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();

        Text scenetitle = new Text("RFSecure");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6,2, 2);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String user = userTextField.getText();
                String pass = pwBox.getText();
                client = new RFSecureClient(user, pass);
                int type = 2;
                try { type = client.loginUser(); } catch (Exception t) {
                    t.printStackTrace();
                }
                if (type == 2 || type == 3) {
                    setupSearchScene();
                } else if (type == 1) {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("You do not have the necessary permissions \nto access this app");
                } else {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Log in failed. Invalid Username/Password");
                }
            }
        });
    }

    /**
     * Sets the gameScene and adds elements to it. gameScene is composed of:
     * 1) A Rectangle for the background
     * 2) All of the elements from world
     * world handles the addition of all the game graphics to the screen with
     * the exception of the background, which you must add to gameScene
     * manually. You will need to set world in this method as well. Also, this
     * method must add an event to gameScene such that when WASD or the arrow
     * keys are pressed the snake will change direction.
     */
    private void setupSearchScene() {
        //monitorScene = new Scene(group, SCREEN_WIDTH, SCREEN_HEIGHT);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("RFSecure");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 1, 2, 1);

        grid.add(new Label("Last Name:"), 0, 2);

        grid.add(new Label("First Name:"), 0, 3);

        grid.add(new Label("ID Number:"), 0, 4);

        TextField lastName = new TextField();
        grid.add(lastName, 1, 2);

        TextField firstName = new TextField();
        grid.add(firstName, 1, 3);

        TextField idNumber = new TextField();
        grid.add(idNumber, 1, 4);


        Button btn = new Button("Sign out");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 0);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                setupStartScene();
            }
        });

        Button btn2 = new Button("Register Visitor");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(btn2);
        grid.add(hbBtn2, 1, 5);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                SerialReader sr = new SerialReader();
                String keyID = sr.getCardID();
                try {
                    client.checkInVisitor(firstName.getText(), lastName.getText(), idNumber.getText(), keyID);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(grid, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}