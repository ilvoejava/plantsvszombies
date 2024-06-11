import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Zombie {
    private int x;
    private int y;
    private int speed;
    private int health;
    private BufferedImage image;
    private String type;

    public Zombie(int x, int y, int speed, int health, BufferedImage image, String type) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.image = image;
        this.type = type;
    }

    public void update() {
        x -= speed;
    }

    public void hit() {
        health--;
    }

    public int getHealth() {
        return health;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight() - 20);
    }
}
