import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Bullet extends Parent {

    private static final double SHIP_BULLET_SPEED = 10;
    private static final double ALIEN_BULLET_SPEED = 10;


    public static final int TYPE_PINK = 0;
    public static final int TYPE_BLUE = 1;
    public static final int TYPE_GREEN = 2;
    public static final int TYPE_SHIP = 3;

    private static final Image SHIP_BULLET = new Image("images/ship_bullet.png");
    private static final Image PINK_BULLET = new Image("images/pink_bullet.png");
    private static final Image GREEN_BULLET = new Image("images/green_bullet.png");
    private static final Image BLUE_BULLET = new Image("images/blue_bullet.png");

    private ImageView bulletView = new ImageView();

    private int size = 5;
    private int height = 40;
    private int width = 10;
    private int type;
    public double speed;

    public int getSize() { return size; }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getType() { return type; }

    Bullet(int t) {
        speed = ALIEN_BULLET_SPEED;
        type = t;
        Group group = new Group();
        bulletView.setFitHeight(40);
        bulletView.setFitWidth(10);
        if (t == TYPE_PINK) {
            bulletView.setImage(PINK_BULLET);
        } else if (t == TYPE_GREEN) {
            bulletView.setImage(GREEN_BULLET);
        } else if (t == TYPE_BLUE) {
            bulletView.setImage(BLUE_BULLET);
        }  else {
            speed = SHIP_BULLET_SPEED;
            bulletView.setImage(SHIP_BULLET);
        }
        group.getChildren().add(bulletView);
        getChildren().add(group);
        setMouseTransparent(true);
    }

}
