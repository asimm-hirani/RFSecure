package sample;

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

public class Main extends Application{

    private Stage primaryStage;
    private Scene startScene;
    private Scene monitorScene;
    private Scene searchScene;
    private Scene addScene;
    private long lastUpdateTime;
    private Group group;

    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 650;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RFSecure Log In");
        group = new Group();
        setupStartScene();
    }

    /**
     * Sets startScene and adds elements to it. startScene is composed of:
     *     1) A title label
     *     2) A group of radio buttons for setting the game mode
     *     3) A button that when pressed will call setupGameScene and call play
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

        Text scenetitle = new Text("Welcome");
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
        grid.add(actiontarget, 1, 6);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String user = userTextField.getText();
                String pass = pwBox.getText();
                RFSecureClient client = new RFSecureClient(user, pass);
                int type = client.logInUser();
                switch(type) {
                    case 3: setupMonitorScene();
                            primaryStage.setScene(monitorScene);
                            monitor();
                            break;
                    case 1: setupSearchScene();
                            primaryStage.setScene(searchScene);
                    case 2: Stage stage = new Stage();
                            stage.setTitle("Monitor");
                            stage.setScene(new Scene(group, 450, 450));
                            setupMonitorScene();
                            stage.setScene(monitorScene);
                            monitor();
                            stage.show();
                            break;
                    default:actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Log in failed. Invalid Username/Password");
                }
            }
        });
    }

    /**
     * Sets the gameScene and adds elements to it. gameScene is composed of:
     *     1) A Rectangle for the background
     *     2) All of the elements from world
     * world handles the addition of all the game graphics to the screen with
     * the exception of the background, which you must add to gameScene
     * manually. You will need to set world in this method as well. Also, this
     * method must add an event to gameScene such that when WASD or the arrow
     * keys are pressed the snake will change direction.
     */
    private void setupMonitorScene() {
        monitorScene = new Scene(group, SCREEN_WIDTH, SCREEN_HEIGHT);
        Rectangle background = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        background.setFill(Color.ANTIQUEWHITE);
        group.getChildren().add(background);
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.TOP_RIGHT);
        hbBtn.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                setupStartScene();
            }
        });
        StackPane pane = new StackPane(hbBtn);
        monitorScene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
    }


    private void setupSearchScene() {
        Label label = new Label("Score: ");
        label.setFont(new Font(30));
        ListView<Node> usernames = new ListView<>();
        ListView<Integer> scores = new ListView<>();
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(usernames, scores);
        hBox.setAlignment(Pos.CENTER);
        File file = new File("src/main/resources/highScores.csv");
        try {
            Scanner in = new Scanner(file);
            in.useDelimiter("[,\\v] *");
            Label username = null;
            Integer score = null;
            boolean bool = false;

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Button save = new Button("Save");
        save.setOnAction(a -> {
            try {
                PrintWriter text = new PrintWriter(file);
                for (int i = 0; i < usernames.getItems().size(); i++) {
                    Object value = usernames.getItems().get(i);
                    Integer b = scores.getItems().get(i);
                    if (value instanceof Label) {
                        Label d = (Label) value;
                        text.printf("%s, %d\n", d.getText(), b.intValue());
                    } else {
                        TextField c = (TextField) value;
                        if (c.getText().equals("")) {
                            text.printf("Unknown, %d\n", b.intValue());
                        } else {
                            text.printf("%s, %d\n", c.getText(),
                                    b.intValue());
                        }
                    }
                }
                text.close();
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
            //setupStartScene(stage);
            //window.setScene(logInScene);
        });
        VBox vBox = new VBox(40);
        vBox.getChildren().addAll(label, hBox, save);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMargin(label, new Insets(30, 0, 50, 0));
        vBox.setMargin(save, new Insets(0, 0, 30, 0));
        StackPane pane = new StackPane(vBox);
        searchScene = new Scene(pane, SCREEN_WIDTH, SCREEN_WIDTH);
    }


    public void monitor() {
        AnimationTimer timey = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                if (System.currentTimeMillis()
                    - lastUpdateTime > 50) {
                    Controller.update();
                    // DO NOT MODIFY ABOVE THIS LINE

                    

                    // DO NOT MODIFY BELOW THIS LINE
                    lastUpdateTime = System.currentTimeMillis();
                }
            }
        };
        lastUpdateTime = System.currentTimeMillis();
        timey.start();
    }
}
