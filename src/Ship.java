import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ship extends Parent {
    public static final int DEFAULT_SIZE = 2;
    public static final int MAX_SIZE = 7;

    private static final Image SHIP = new Image("images/ship.png");
    private ImageView shipView = new ImageView(SHIP);

    private int size = 5;
    private int height = 50;
    private int width = 100;

    public int getSize() {
        return size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Ship() {
        Group group = new Group();
        shipView.setFitHeight(50);
        shipView.setFitWidth(100);
        group.getChildren().add(shipView);
        getChildren().add(group);
        setMouseTransparent(true);
    }


}
