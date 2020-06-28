import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends Parent {

    private final SpaceInvaders spaceinvaders;

    private static final double ALIEN_SPEED_RATE = 3;
    private static final double BASE_ALIEN_SPEED = 1;

    private static final double SHIP_SPEED = 5;
    private static final double SHIP_BULLET_SPEED = 10;
    private static final double ALIEN_VERTICAL_SPEED = -20;
    private static final double ALIEN_BULLET_SPEED = 10;

    private static final int STARTING_LEVEL = 0;
    private static final int PLAYING = 1;
    private static final int GAME_OVER = 2;
    private static final int GAME_WON = 3;


    private static final AudioClip explodeSound = new AudioClip(new File("src/sounds/explosion.wav").toURI().toString());

    private static final AudioClip killSound = new AudioClip(new File("src/sounds/invaderkilled.wav").toURI().toString());

    private static final AudioClip shootSound = new AudioClip(new File("src/sounds/shoot.wav").toURI().toString());

    private static final AudioClip moveSound = new AudioClip(new File("src/sounds/move.wav").toURI().toString());

    private static double ALIEN_SPEED;

    private int LIVES;
    private int SCORE;
    private int LEVEL;

    private Pane root;

    private Timer fireTimer;
    private int fireNumber;

    private int state;
    private ArrayList<Alien> aliens;
    private ArrayList<Bullet> bullets;

    GridPane alienPane;
    private Ship ship;
    private Group group;
    private AnimationTimer aniTimer = null;

    private double shipMovement;

    private Group infoPanel;
    private Text scoreCaption = new Text(30, 60, "SCORE:");
    private Text score= new Text(180, 60, "");
    private Text livesCaption = new Text(1050, 60, "LIVES:");
    private Text lives = new Text(1180, 60, "");
    private Text levelCaption = new Text(1350, 60, "LEVEL:");
    private Text level = new Text(1480, 60, "");

    public Game(SpaceInvaders inv, int levelNumber) {
        spaceinvaders = inv;
        group = new Group();
        getChildren().add(group);
        createGame(levelNumber, 0);
    }


    private void createGame(int levelNumber, int prevScore) {
        fireNumber = 0;
        if (fireTimer != null) { fireTimer.cancel();}
            fireTimer = new Timer();
        fireTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fireNumber = 0;
            }
        },0,1200);
        state = STARTING_LEVEL;


        final ImageView background = new ImageView();
        background.setFitWidth(SpaceInvaders.STAGE_WIDTH);
        background.setFitHeight(SpaceInvaders.STAGE_HEIGHT);
        background.setImage(new Image("images/space.jpg"));

        //Mouse control, for testing.
        /*background.setOnMouseMoved((MouseEvent me) -> {
            if (state == PLAYING) {
                moveShip(me.getX() - ship.getWidth() / 2);
                me.consume();
            }
        });
        background.setOnMouseDragged((MouseEvent me) -> {
            if (state == PLAYING) {
                moveShip(me.getX() - ship.getWidth() / 2);
                me.consume();
            }
        });*/

        levelCaption.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Machine_Regular.ttf"), 35));
        levelCaption.setFill(Color.WHITE);
        livesCaption.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Machine_Regular.ttf"), 35));
        livesCaption.setFill(Color.WHITE);
        scoreCaption.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Machine_Regular.ttf"), 35));
        scoreCaption.setFill(Color.WHITE);

        //Text
        root = new Pane();
        root.getChildren().addAll(background, levelCaption, livesCaption, scoreCaption);
        initLevel(levelNumber, prevScore);
        if (aniTimer != null) {aniTimer.stop();}
        aniTimer = initTimeline();
        aniTimer.start();
    }


    private void initLevel(int levelNumber, int prevScore) {
        LIVES = 3;
        SCORE = prevScore;
        LEVEL = levelNumber;
        ALIEN_SPEED = levelNumber * BASE_ALIEN_SPEED;

        aliens = new ArrayList<Alien>();
        bullets = new ArrayList<Bullet>();
        alienPane = new GridPane();

        //Text
        score.setText(String.valueOf(SCORE));
        level.setText(String.valueOf(LEVEL));
        lives.setText(String.valueOf(LIVES));
        score.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Machine_Regular.ttf"), 35));
        score.setFill(Color.WHITE);
        level.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Machine_Regular.ttf"), 35));
        level.setFill(Color.WHITE);
        lives.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Machine_Regular.ttf"), 35));
        lives.setFill(Color.WHITE);

        //ship
        ship = new Ship();
        ship.setTranslateY(SpaceInvaders.STAGE_HEIGHT-100);
        ship.setTranslateX(SpaceInvaders.STAGE_WIDTH / 2 - ship.getWidth() );

        //Aliens
        alienPane.setHgap(20);
        alienPane.setVgap(20);
        alienPane.relocate(325, 150);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                Alien newAlien;
                if (i < 2) {
                    newAlien = new Alien(Alien.TYPE_GREEN);
                } else if (4 > i && i >= 2) {
                    newAlien = new Alien(Alien.TYPE_BLUE);
                } else {
                    newAlien = new Alien(Alien.TYPE_PINK);
                }
                aliens.add(newAlien);
                alienPane.add(newAlien, j, i);
            }
        }
        root.getChildren().addAll(ship, alienPane, score, lives, level);

        //register scene and events
        Scene battleScreen = new Scene(root);
        battleScreen.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.Q) {
                Alert quitAlert = new Alert(Alert.AlertType.WARNING, "", ButtonType.YES, ButtonType.NO);
                quitAlert.setTitle("Warning");
                quitAlert.setHeaderText("Are you sure you want to return to title screen?");
                quitAlert.showAndWait().ifPresent(response -> {
                    if (response.equals(ButtonType.YES)) {
                        try {
                            spaceinvaders.mainStage.setScene(spaceinvaders.createTitleScreen());
                            spaceinvaders.mainStage.setTitle("SpaceInvaders - Title");
                            spaceinvaders.mainStage.show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            if (event.getCode() == KeyCode.A && state == PLAYING) {
                shipMovement = -SHIP_SPEED;
                event.consume();
            }
            if (event.getCode() == KeyCode.D && state == PLAYING) {
                shipMovement = SHIP_SPEED;
                event.consume();
            }
            if (event.getCode() == KeyCode.SPACE && state == PLAYING) {
                if (fireNumber < 2) {
                    fireNumber += 1;
                    Bullet newBullet = new Bullet(Bullet.TYPE_SHIP);
                    newBullet.setTranslateX(ship.getTranslateX() + 40);
                    newBullet.setTranslateY(ship.getTranslateY() + 20);
                    bullets.add(newBullet);
                    shootSound.play();
                    root.getChildren().add(newBullet);
                }
                event.consume();
            }
            if (event.getCode() == KeyCode.K) {
                LIVES = 0;
                Gameover();
                event.consume();
            }
            if (event.getCode() == KeyCode.R && state == GAME_OVER) {
                createGame(levelNumber,0);
                event.consume();
            }
            if (event.getCode() == KeyCode.DIGIT1 && state == GAME_OVER) {
                createGame(1,0);
                event.consume();
            }
            if (event.getCode() == KeyCode.DIGIT2 && state == GAME_OVER) {
                createGame(2,0);
                event.consume();
            }
            if (event.getCode() == KeyCode.DIGIT3 && state == GAME_OVER) {
                createGame(3,0);
                event.consume();
            }
            if (event.getCode() == KeyCode.I && state == GAME_OVER) {
                try {
                    spaceinvaders.mainStage.setScene(spaceinvaders.createTitleScreen());
                    spaceinvaders.mainStage.setTitle("SpaceInvaders - Title");
                    spaceinvaders.mainStage.show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                event.consume();
            }
            if (event.getCode() == KeyCode.ESCAPE && state == GAME_OVER) {
                System.exit(0);
                event.consume();
            }
        });

        battleScreen.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.D) {
                shipMovement = 0;
                event.consume();
            }
        });

        spaceinvaders.mainStage.setScene(battleScreen);
        spaceinvaders.mainStage.show();
        state = PLAYING;

    }

    private AnimationTimer initTimeline() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                handle_animation();
            }
        };
        return timer;
    }

    private void handle_animation() {
        if (state != PLAYING) {
            return;
        }

        if (shipMovement != 0) {
            moveShip(shipMovement + ship.getTranslateX());
        }
        moveAlien();
        moveBullet();
    }

    private void moveShip(double newX) {
        double x = newX;
        if (x < 0) {
            x = 0;
        }
        if (x + ship.getWidth() > SpaceInvaders.STAGE_WIDTH) {
            x = SpaceInvaders.STAGE_WIDTH - ship.getWidth();
        }
        ship.setTranslateX(x);
    }

    private void moveAlien() {
        double x = ALIEN_SPEED + alienPane.getLayoutX();
        double y = alienPane.getLayoutY();

        if (alienPane.getLayoutX() + ALIEN_SPEED < 0) {
            x = 0;
            y -= ALIEN_VERTICAL_SPEED;
            ALIEN_SPEED *= -1;
            alienFire();
        }
        else if (alienPane.getLayoutX() + ALIEN_SPEED + alienPane.getWidth() >= SpaceInvaders.STAGE_WIDTH) {
            x = SpaceInvaders.STAGE_WIDTH - alienPane.getWidth();
            ALIEN_SPEED *= -1;
            y -= ALIEN_VERTICAL_SPEED;
            alienFire();
        }
        Random rand = new Random();
        int randFire = rand.nextInt(100);
        if (randFire == 0) {
            alienFire();
        }
        alienPane.relocate(x, y);
        if (moveSound.isPlaying() == false) {
            moveSound.play();
        }
        if (alienPane.getBoundsInParent().getMaxY() >= ship.getBoundsInParent().getMaxY()) {
            Gameover();
        }
    }

    private void moveBullet() {
        double y;
        ArrayList<Bullet> removeList = new ArrayList<Bullet>();
        for (Bullet cur_bullet : bullets) {
            if (cur_bullet.getType() == Bullet.TYPE_SHIP) {
                y = cur_bullet.getTranslateY() - SHIP_BULLET_SPEED;
                if (cur_bullet.getTranslateY() - SHIP_BULLET_SPEED + cur_bullet.getHeight() <= 0) {
                    y = 0;
                }
            } else {
                y = ALIEN_BULLET_SPEED + cur_bullet.getTranslateY();
                if (cur_bullet.getTranslateY() + ALIEN_BULLET_SPEED + cur_bullet.getHeight() >= SpaceInvaders.STAGE_HEIGHT) {
                    y = SpaceInvaders.STAGE_HEIGHT;
                }
            }
            cur_bullet.setTranslateY(y);
            checkBulletHit(cur_bullet);
            if (cur_bullet.getTranslateY() == 0 || cur_bullet.getTranslateY() == SpaceInvaders.STAGE_HEIGHT) {
                removeList.add(cur_bullet);
            }
        }

        for (Bullet cur_bullet : removeList) {
            root.getChildren().remove(cur_bullet);
            bullets.remove(cur_bullet);
        }
    }

    private void alienFire() {
        Random rand = new Random();
        int randIndex = rand.nextInt(aliens.size());
        Alien cur_alien = aliens.get(randIndex);
        Bullet newBullet = new Bullet(cur_alien.getType());

        Bounds base = alienPane.getBoundsInParent();
        Bounds node = cur_alien.getBoundsInParent();

        newBullet.setTranslateX(base.getMinX() + node.getMaxX());
        newBullet.setTranslateY(base.getMinY() + node.getMaxY());

        bullets.add(newBullet);
        root.getChildren().add(newBullet);
    }

    private void checkBulletHit(Bullet bullet) {
        if(bullet.isVisible() == false) { return; }
        if (bullet.getType() == Bullet.TYPE_SHIP) {
            ArrayList<Alien> removeList = new ArrayList<Alien>();
            Bounds base = alienPane.getBoundsInParent();
            for (Node cur_alien : alienPane.getChildren()) {
                Bounds node = cur_alien.getBoundsInParent();
                if (bullet.getTranslateX() >= (base.getMinX() + node.getMinX()) &&
                        bullet.getTranslateX() <= (base.getMinX() + node.getMaxX()) &&
                        bullet.getTranslateY() >= (base.getMinY() + node.getMinY()) &&
                        bullet.getTranslateY() <= (base.getMinY() + node.getMaxY())) {
                    removeList.add((Alien)cur_alien);
                    bullet.setVisible(false);
                    SCORE += 10 + ((Alien) cur_alien).getType() * 10;
                    score.setText(String.valueOf(SCORE));
                    ALIEN_SPEED *= 1.02;
                    killSound.play();
                    break;
                }
            }

            for (Alien cur_alien : removeList) {
                alienPane.getChildren().remove(cur_alien);
                aliens.remove(cur_alien);
            }

            if (aliens.isEmpty()) {
                if (LEVEL < 3) {
                    createGame(LEVEL + 1, SCORE);
                } else {
                    Gameover();
                }
            }

        } else {
            Bounds b = ship.getBoundsInParent();
            if (bullet.getTranslateX() >= b.getMinX() && bullet.getTranslateX() <= b.getMaxX() && bullet.getTranslateY() >= b.getMinY() &&  bullet.getTranslateY() <= b.getMaxY()) {
                bullet.setVisible(false);
                LIVES -= 1;
                explodeSound.play();
                lives.setText(String.valueOf(LIVES));
                if (LIVES == 0) {
                    root.getChildren().remove(ship);
                    Gameover();
                }
                ship.setTranslateY(SpaceInvaders.STAGE_HEIGHT-100);
                ship.setTranslateX(SpaceInvaders.STAGE_WIDTH / 2 - ship.getWidth() );
            }
        }

    }

    private void Gameover() {
        state = GAME_OVER;
        StackPane gameOver = new StackPane();
        gameOver.setAlignment(Pos.CENTER);
        gameOver.setMaxWidth(600);
        gameOver.setMaxHeight(300);
        gameOver.setLayoutX(500);
        gameOver.setLayoutY(400);

        Rectangle rec = new Rectangle(600, 300);
        rec.setFill(Color.WHITE);
        rec.setArcWidth(50);
        rec.setArcHeight(50);
        rec.setOpacity(0.8);

        VBox texts = new VBox();
        texts.setAlignment(Pos.CENTER);
        texts.setSpacing(10);
        Text over;
        if (LEVEL == 3 && aliens.isEmpty()) {
            over = new Text("YOU WIN");
        } else {
            over = new Text("YOU LOSE");
        }
        over.setFont(new Font(80));
        over.setFill(Color.BLACK);

        Text overScore = new Text("Final Score: " + SCORE);
        overScore.setFont(new Font(30));
        overScore.setFill(Color.BLACK);

        Text overHint = new Text("Restart:  R     Quit:   ESC    Tile Screen:  I    \n" + "Start on specific level: 1 2 3");
        overHint.setFont(new Font(30));
        overHint.setFill(Color.BLACK);

        texts.getChildren().addAll(over, overScore, overHint);
        gameOver.getChildren().addAll(rec, texts);

        FadeTransition ft = new FadeTransition(Duration.millis(1000), gameOver);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        root.getChildren().add(gameOver);
        ft.play();

    }
}
