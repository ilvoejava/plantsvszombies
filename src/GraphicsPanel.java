import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener {
    private ArrayList<Zombie> zombies;
    private ArrayList<Pea> peas;

    private int zombieSpawnInterval;
    private int zombieSpawnCounter;
    private int phase;

    private BufferedImage background;
    private BufferedImage sunImg;
    private BufferedImage peashooterCard;
    private BufferedImage repeaterCard;
    private BufferedImage sunflowerCard;
    private BufferedImage wallnutCard;
    private BufferedImage browncoat;
    private BufferedImage conehead;
    private BufferedImage buckethead;
    private BufferedImage peaImage;
    private Rectangle sunflower;
    private Rectangle peashooter;
    private Rectangle repeater;
    private Rectangle wallnut;
    private String selectedPlant;
    private boolean plantPlacedThisClick;

    private Timer timer;
    private ArrayList<Plants> plants;
    private ArrayList<Sun> suns;
    private int time;
    private int sun;
    private final int PEASHOOTER_COST = 100;
    private final int SUNFLOWER_COST = 50;
    private final int WALLNUT_COST = 50;
    private final int REPEATER_COST = 200;
    private static final int browncoat_hp = 7;
    private static final int conehead_hp  = 13;
    private static final int buckethead_hp = 21;

    public GraphicsPanel(String name) {
        try {
            background = ImageIO.read(new File("images/background.png"));
            sunImg = ImageIO.read(new File("images/sun.png"));
            peashooterCard = ImageIO.read(new File("images/peashooterCard.png"));
            repeaterCard = ImageIO.read(new File("images/repeaterCard.png"));
            sunflowerCard = ImageIO.read(new File("images/sunflowerCard.png"));
            wallnutCard = ImageIO.read(new File("images/wallnutCard.png"));
            browncoat = ImageIO.read(new File("images/browncoat.png"));
            conehead = ImageIO.read(new File("images/conehead.png"));
            buckethead = ImageIO.read(new File("images/buckethead.png"));
            peaImage = ImageIO.read(new File("images/pea.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        sunflower = new Rectangle(0, 80, 105, 67);
        peashooter = new Rectangle(0, 150, 105, 67);
        wallnut = new Rectangle(0, 220, 105, 67);
        repeater = new Rectangle(0, 290, 105, 67);
        plants = new ArrayList<>();
        suns = new ArrayList<>();
        peas = new ArrayList<>();
        sun = 500;
        time = 0;
        phase = 0;
        timer = new Timer(16, this);
        timer.start();

        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();

        zombies = new ArrayList<>();
        zombieSpawnInterval = 1200; // Initial spawn interval (20 seconds)
        zombieSpawnCounter = 0;
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
        for (Pea pea : peas) {
            g.drawImage(pea.getImage(), pea.getX(), pea.getY(), null);
        }
        for (Zombie zombie : zombies) {
            g.drawImage(zombie.getImage(), zombie.getX(), zombie.getY(), null);
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
    public void mousePressed(MouseEvent e) {
        plantPlacedThisClick = false; // Reset the flag when a mouse press occurs
    }

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
        if (e.getButton() == MouseEvent.BUTTON1) {
            // Check if the click intersects with any sun object
            boolean clickedOnSun = false;
            for (int i = 0; i < suns.size(); i++) {
                Sun sun = suns.get(i);
                if (sun.getBounds().contains(clicked)) {
                    clickedOnSun = true;
                    break;
                }
            }

            if (!clickedOnSun && selectedPlant != null && sun >= getPlantCost(selectedPlant) && !plantPlacedThisClick) {
                // Check if the click is within the boundaries of the lawn
                if (clicked.y >= 80 && clicked.y < 525) {
                    // Check if the click is not on the plant packets
                    if (!(clicked.x >= 0 && clicked.x < 120)) {
                        // Calculate the lane based on the clicked y-coordinate
                        int lane = (clicked.y - 50) / 95; // Adjust according to your lane positions
                        if (lane >= 0 && lane <= 4) { // Ensure lane is within valid range
                            int y = 50 + lane * 95;
                            Plants plant = new Plants(clicked.x, y + 50, selectedPlant);
                            plants.add(plant);
                            sun -= getPlantCost(selectedPlant);
                            plantPlacedThisClick = true;
                        }
                    }
                }
            }
        }

    }

    private void spawnZombie() {
        int lane = (int) (Math.random() * 5);
        int y = 50 + lane * 95; // Adjust according to your lane positions
        int zombieType = 0;

        if (phase == 0 || phase == 1 || phase == 2) {
            // first phase: Only browncoat zombies
            zombieType = 0;
        } else if (phase == 3 || phase == 4 || phase == 5 || phase == 6) {
            // second phase: browncoat and conehead zombies
            zombieType = (int) (Math.random() * 2); // 0 or 1
        } else if (phase == 7) {
            // third phase: all zombie types
            zombieType = (int) (Math.random() * 3); // 0, 1, or 2
        }

        Zombie zombie;
        if (zombieType == 0) {
            zombie = new Zombie(getWidth(), y, 1, browncoat_hp, browncoat, "browncoat");
        } else if (zombieType == 1) {
            zombie = new Zombie(getWidth(), y, 1, conehead_hp, conehead, "conehead");
        } else {
            zombie = new Zombie(getWidth(), y, 1, buckethead_hp, buckethead, "buckethead");
        }

        zombies.add(zombie);
    }
    private void shootPeas(Plants plant) {
        if (plant.getName().equals("peashooter")) {
            for (Zombie zombie : zombies) {
                if (zombie.getY() + 50 == plant.getyCoord()) {
                    if (time % 70 == 0) {
                        peas.add(new Pea(plant.getxCoord() + plant.getImage().getWidth(), plant.getyCoord() + plant.getImage().getHeight() / 2 - 25, plant.getProjectileSpeed(), peaImage));
                        break;
                    }
                }
            }
        } else if (plant.getName().equals("repeater")) {
            for (Zombie zombie : zombies) {
                if (zombie.getY() + 50 == plant.getyCoord()) {
                    if (time % 70 == 0 || time % 70 == 10) { // Shoot every 70 ticks, including at 35 ticks
                        peas.add(new Pea(plant.getxCoord() + plant.getImage().getWidth(), plant.getyCoord() + plant.getImage().getHeight() / 2 - 25, plant.getProjectileSpeed(), peaImage));
                    }
                }
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
            int elapsedSeconds = time / 60;

            if (elapsedSeconds >= 180) {
                phase = 7;
            } else if (elapsedSeconds >= 140) {
                phase = 6;
            } else if (elapsedSeconds >= 120) {
                phase = 5;
            } else if (elapsedSeconds >= 100) {
                phase = 4;
            } else if (elapsedSeconds >= 80) {
                phase = 3;
            } else if (elapsedSeconds >= 60) {
                phase = 2;
            } else if (elapsedSeconds >= 40) {
                phase = 1;
            } else if (elapsedSeconds >= 30) {
                phase = 0;
            }

            if (phase == 0) {
                zombieSpawnInterval = 1200;
            } else if (phase == 1) {
                zombieSpawnInterval = 1000;
            } else if (phase == 2) {
                zombieSpawnInterval = 800;
            } else if (phase == 3) {
                zombieSpawnInterval = 600;
            } else if (phase == 4) {
                zombieSpawnInterval = 400;
            } else if (phase == 5) {
                zombieSpawnInterval = 300;
            } else if (phase == 6) {
                zombieSpawnInterval = 200;
            } else {
                zombieSpawnInterval = Math.max(60, zombieSpawnInterval - 30);
            }
            for (Plants plant : plants) {
                shootPeas(plant);
            }


            // spawn sun every 800 ticks (24 seconds)
            if (time % 800 == 0) {
                for (Plants plant : plants) {
                    if (plant.isSunflower()) {
                        suns.add(new Sun(plant.getxCoord(), plant.getyCoord(), sunImg, true));
                    }
                }
            }

            // spawn sky sun every 333 ticks (10 seconds)
            if (time % 333 == 0) {
                int randomX = (int) (Math.random() * (getWidth() - sunImg.getWidth()));
                suns.add(new Sun(randomX, 0, sunImg, false));
            }

            // Update sun positions
            for (int i = 0; i < suns.size(); i++) {
                Sun sun = suns.get(i);
                sun.update();
                if (sun.getY() > getHeight()) {
                    suns.remove(i);
                    i--;
                }
            }

            // Spawn zombies
            zombieSpawnCounter++;
            if (zombieSpawnCounter >= zombieSpawnInterval) {
                zombieSpawnCounter = 0;
                spawnZombie();
            }


            // Move peas
            for (int i = 0; i < peas.size(); i++) {
                Pea pea = peas.get(i);
                pea.update();
                if (pea.getX() > getWidth()) {
                    peas.remove(i);
                    i--;
                } else {
                    for (int j = 0; j < zombies.size(); j++) {
                        Zombie zombie = zombies.get(j);
                        if (pea.getBounds().intersects(zombie.getBounds())) {
                            zombie.hit();
                            peas.remove(i);
                            i--;
                            if (zombie.getHealth() <= 0) {
                                zombies.remove(j);
                                j--;
                            }
                            break;
                        }
                    }
                }
            }

            // Move zombies
            for (int i = 0; i < zombies.size(); i++) {
                Zombie zombie = zombies.get(i);
                zombie.update();
                if (zombie.getX() < -60) {
                    zombies.remove(i);
                    i--;
                }
            }
        }
        repaint();
    }

    private int getPlantCost(String plantName) {
        switch (plantName) {
            case "peashooter":
                return PEASHOOTER_COST;
            case "sunflower":
                return SUNFLOWER_COST;
            case "wallnut":
                return WALLNUT_COST;
            case "repeater":
                return REPEATER_COST;
            default:
                return Integer.MAX_VALUE;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
