import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Sun {
    private int x;
    private int y;
    private BufferedImage image;
    private boolean fromSunflower;

    public Sun(int x, int y, BufferedImage image, boolean fromSunflower) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.fromSunflower = fromSunflower;
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

    public void update() {
        if (!fromSunflower) {
            y += 2; // sun drops down if not from sunflower
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth() + 30, image.getHeight() + 50);
    }
}
