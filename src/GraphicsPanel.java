import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener {
    private BufferedImage background;
    private BufferedImage sunImg;
    private BufferedImage peashooterCard;
    private BufferedImage repeaterCard;
    private BufferedImage sunflowerCard;
    private BufferedImage wallnutCard;
    private Rectangle sunflower;
    private Rectangle peashooter;
    private Rectangle repeater;
    private Rectangle wallnut;
    private String selectedPlant;

    private Timer timer;
    private ArrayList<Plants> plants;
    private ArrayList<Sun> suns;
    private int time;
    private int sun;
    private final int PEASHOOTER_COST = 100;
    private final int SUNFLOWER_COST = 50;
    private final int WALLNUT_COST = 50;
    private final int REPEATER_COST = 200;

    public GraphicsPanel(String name) {
        try {
            background = ImageIO.read(new File("images/background.png"));
            sunImg = ImageIO.read(new File("images/sun.png"));
            peashooterCard = ImageIO.read(new File("images/peashooterCard.png"));
            repeaterCard = ImageIO.read(new File("images/repeaterCard.png"));
            sunflowerCard = ImageIO.read(new File("images/sunflowerCard.png"));
            wallnutCard = ImageIO.read(new File("images/wallnutCard.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        sunflower = new Rectangle(0, 80, 60, 60);
        peashooter = new Rectangle(0, 150, 60, 60);
        wallnut = new Rectangle(0, 220, 60, 60);
        repeater = new Rectangle(0, 290, 60, 60);
        plants = new ArrayList<>();
        suns = new ArrayList<>();
        sun = 10000;
        time = 0;
        timer = new Timer(1000, this);
        timer.start();

        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.drawImage(sunImg, 0, 0, null);
        g.drawImage(sunflowerCard, 0, 80, null);
        g.drawImage(peashooterCard, 0, 150, null);
        g.drawImage(wallnutCard, 0, 220, null);
        g.drawImage(repeaterCard, 0, 290, null);

        g.setFont(new Font("Courier New", Font.BOLD, 48));
        g.drawString(Integer.toString(sun), 65, 45);

        for (Plants plant : plants) {
            g.drawImage(plant.getImage(), plant.getxCoord(), plant.getyCoord(), null);
        }

        for (Sun sun : suns) {
            g.drawImage(sun.getImage(), sun.getX(), sun.getY(), null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point clicked = e.getPoint();
        for (int i = 0; i < suns.size(); i++) {
            Sun sun = suns.get(i);
            if (sun.getBounds().contains(clicked)) {
                suns.remove(i);
                this.sun += 25;
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        Point clicked = e.getPoint();
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (peashooter.contains(clicked)) {
                selectedPlant = "peashooter";
            } else if (sunflower.contains(clicked)) {
                selectedPlant = "sunflower";
            } else if (wallnut.contains(clicked)) {
                selectedPlant = "wallnut";
            } else if (repeater.contains(clicked)) {
                selectedPlant = "repeater";
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (selectedPlant != null && sun >= getPlantCost(selectedPlant)) {
                Plants plant = new Plants(clicked.x, clicked.y, selectedPlant);
                plants.add(plant);
                sun -= getPlantCost(selectedPlant); // plants the plant and reduces sun based off cost
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            time++;
            if (time % 24 == 0) {
                for (Plants plant : plants) {
                    if (plant.isSunflower()) {
                        suns.add(new Sun(plant.getxCoord(), plant.getyCoord(), sunImg, true));
                    } // sunflower drops sun every 24s
                }
            }
            if (time % 10 == 0) {
                int randomX = (int) (Math.random() * (getWidth() - sunImg.getWidth()));
                suns.add(new Sun(randomX, 0, sunImg, false));
            } // sky drops sun every 10s

            for (int i = 0; i < suns.size(); i++) {
                Sun sun = suns.get(i);
                sun.update();
                if (sun.getY() > getHeight()) {
                    suns.remove(i);
                    i--;
                }
            }
        }
        repaint();
    }



    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private int getPlantCost(String plantName) {
        if ("peashooter".equals(plantName)) {
            return PEASHOOTER_COST;
        } else if ("sunflower".equals(plantName)) {
            return SUNFLOWER_COST;
        } else if ("wallnut".equals(plantName)) {
            return WALLNUT_COST;
        } else if ("repeater".equals(plantName)) {
            return REPEATER_COST;
        }
        return 0; // default cost
    }
}