import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;


public class SpaceInvaders extends Application {
    final static double STAGE_WIDTH = 1600;
    final static double STAGE_HEIGHT = 1200;

    Stage mainStage;
    Game game;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage)throws FileNotFoundException {
        mainStage = stage;
        System.out.println("Entering start method");

        AudioClip theme = new AudioClip(new File("src/sounds/spaceinvaders1.mpeg").toURI().toString());
        theme.setCycleCount(AudioClip.INDEFINITE);
        theme.play();

        // stage setting
        stage.setResizable(false);
        stage.setWidth(STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.setScene(createTitleScreen());
        stage.setTitle("SpaceInvaders - Title");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Entering init method");
        System.out.println("Entering init method");
    }


    public Scene createTitleScreen() throws FileNotFoundException {

        //starting scene setting
        Pane root = new Pane();

        // LOGO
        Image image = new Image("images/si_logo.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(800);
        imageView.setFitWidth(1000);
        imageView.setX(300);
        imageView.setY(50);
        imageView.setImage(image);
        imageView.setPreserveRatio(true);

        //Background
        final ImageView background = new ImageView();
        background.setFitWidth(STAGE_WIDTH);
        background.setFitHeight(STAGE_HEIGHT);
        background.setImage(new Image("images/space.jpg"));

        //icons
        //ENTER
        Image enter = new Image("images/enter_key.png");
        ImageView enterView = new ImageView(enter);
        enterView.setX(820);
        enterView.setY(860);
        enterView.setImage(enter);
        enterView.setFitWidth(45);
        enterView.setFitHeight(45);
        enterView.setPreserveRatio(true);

        //Text description
        Text text = new Text(650, 900, "PRESS [ENTER] TO START");
        text.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Machine_Regular.ttf"), 35));
        text.setFill(Color.WHITE);

        Text text1 = new Text(80, 1100, "Move Left / Right    [A/D]                  Fire   [SPACE]              Start on specific level  [1  2  3]              Quit [Q]");
        text1.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Machine_Regular.ttf"), 20));
        text1.setFill(Color.WHITE);

        Text credit = new Text(700, 20, "Implemented by Wenjiu Wang");
        credit.setFont(new Font(15));
        credit.setFill(Color.WHITE);


        //Layout
        root.getChildren().addAll(background, imageView, text, text1, credit);

        Scene titleScreen = new Scene(root);
        titleScreen.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                game = new Game(this, 1);

            } else if(event.getCode() == KeyCode.Q) {
                Alert quitAlert = new Alert(Alert.AlertType.WARNING, "", ButtonType.YES, ButtonType.NO);
                quitAlert.setTitle("Warning");
                quitAlert.setHeaderText("Are you sure you want to quit?");
                quitAlert.showAndWait().ifPresent(response -> {
                    if (response.equals(ButtonType.YES)) {
                        System.exit(0);
                    }
                });
            }
            if (event.getCode() == KeyCode.DIGIT1) {
                game = new Game(this, 1);
                event.consume();
            }
            if (event.getCode() == KeyCode.DIGIT2) {
                game = new Game(this, 2);
                event.consume();
            }
            if (event.getCode() == KeyCode.DIGIT3) {
                game = new Game(this, 3);
                event.consume();
            }
        });
        return titleScreen;
    }


}

