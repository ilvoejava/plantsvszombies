import java.awt.Rectangle;

public class Zombie {
    private int x;
    private int y;
    private int speed;
    private int hp;

    public Zombie(int x, int y, int speed, int hp) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }

    public void update() {
        x -= speed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 60, 60);
    }
}
