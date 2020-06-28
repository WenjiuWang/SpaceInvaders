import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Alien extends Parent {
    public static final int TYPE_PINK = 0;
    public static final int TYPE_BLUE = 1;
    public static final int TYPE_GREEN = 2;

    private static final Image PINK_ALIEN = new Image("images/alien_pink.png");
    private static final Image GREEN_ALIEN = new Image("images/alien_green.png");
    private static final Image BLUE_ALIEN = new Image("images/alien_blue.png");

    private ImageView alienView = new ImageView();

    private int size = 5;
    private int height = 75;
    private int width = 100;

    private int type;
    private ImageView content;

    public int getSize() { return size; }

    public int getType() { return type; }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Alien(int t) {
        type = t;
        Group group = new Group();
        alienView.setFitHeight(50);
        alienView.setFitWidth(75);
        if (t == TYPE_PINK) {
            alienView.setImage(PINK_ALIEN);
        } else if (t == TYPE_BLUE) {
            alienView.setImage(BLUE_ALIEN);
        } else {
            alienView.setImage(GREEN_ALIEN);
        }
        group.getChildren().add(alienView);
        getChildren().add(group);
        setMouseTransparent(true);
    }




}
