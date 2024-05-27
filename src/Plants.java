import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Plants {
    private int xCoord;
    private int yCoord;
    private String type;
    private BufferedImage image;
    private long lastSunDropTime;

    public Plants(int x, int y, String type) {
        this.xCoord = x;
        this.yCoord = y;
        this.type = type;
        this.lastSunDropTime = System.currentTimeMillis();

        try {
            if ("peashooter".equals(type)) {
                image = ImageIO.read(new File("images/peashooter.png"));
            } else if ("sunflower".equals(type)) {
                image = ImageIO.read(new File("images/sunflower.png"));
            } else if ("wallnut".equals(type)) {
                image = ImageIO.read(new File("images/wallnut.png"));
            } else if ("repeater".equals(type)) {
                image = ImageIO.read(new File("images/repeater.png"));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isSunflower() {
        return "sunflower".equals(type);
    }

    public boolean dropSun() {
        long currentTime = System.currentTimeMillis();
        if (isSunflower() && currentTime - lastSunDropTime >= 15000) {
            lastSunDropTime = currentTime;
            return true; // sun was dropped
        }
        return false; // sun was not dropped
    }
}
