import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Pea {
    private int x;
    private int y;
    private int speed;
    private BufferedImage image;
    private Rectangle bounds;

    public Pea(int x, int y, int speed, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.image = image;
    }

    public void update() {
        x += speed;
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
    public boolean intersects(Rectangle other) {
        return bounds.intersects(other);
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight() - 10);
    }
}
