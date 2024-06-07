import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Plants {
    private int xCoord;
    private int yCoord;
    private String name;
    private String type;
    private BufferedImage image;
    private int sunCost;
    private boolean isRepeater;


    public Plants(int x, int y, String name) {
        this.xCoord = x;
        this.yCoord = y;
        this.name = name;
        this.image = loadImage(name);
        this.sunCost = getPlantCost(name);
        this.isRepeater = name.equals("repeater");
    }

    private BufferedImage loadImage(String name) {
        BufferedImage img = null;
        try {
            if (name.equals("peashooter")) {
                img = ImageIO.read(new File("images/peashooter.png"));
            } else if (name.equals("sunflower")) {
                img = ImageIO.read(new File("images/sunflower.png"));
            } else if (name.equals("wallnut")) {
                img = ImageIO.read(new File("images/wallnut.png"));
            } else if (name.equals("repeater")) {
                img = ImageIO.read(new File("images/repeater.png"));
            }
        } catch (IOException e) {
            System.out.println("Error loading plant image: " + e.getMessage());
        }
        return img;
    }
    public int getProjectileSpeed() {
        if (name.equals("peashooter")) {
            return 5;
        } else if (name.equals("repeater")) {
            return 7; // increase the speed for repeater peas
        }
        return 0;
    }


    private int getPlantCost(String name) {
        if (name.equals("peashooter")) {
            return 100;
        } else if (name.equals("sunflower")) {
            return 50;
        } else if (name.equals("wallnut")) {
            return 50;
        } else if (name.equals("repeater")) {
            return 200;
        }
        return 0;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }
    public boolean isSunflower() {
        return "sunflower".equals(name);
    }
    public boolean isRepeater() {
        return isRepeater;
    }


    public int getSunCost() {
        return sunCost;
    }
}
