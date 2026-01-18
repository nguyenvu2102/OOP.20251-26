import java.awt.*;

public abstract class Component {
    protected int x, y;
    protected int width = 80, height = 40;
    protected int rotation = 0;
    protected boolean isPowered = false;
    protected String name;
    public Component(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void rotate() {
        rotation = (rotation + 1) % 4;
        int temp = width;
        width = height;
        height = temp;
    }
    public void setPowered(boolean p) { this.isPowered = p; }

    public abstract void draw(Graphics2D g2);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean contains(Point p) {
        return getBounds().contains(p);
    }

    public boolean isTouching(Component other) {
        if (this == other) return false;
        Rectangle r1 = getBounds();
        r1.grow(5, 5);
        return r1.intersects(other.getBounds());
    }
}