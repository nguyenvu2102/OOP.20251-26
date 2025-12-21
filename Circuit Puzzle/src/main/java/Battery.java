import java.awt.*;
import java.awt.geom.AffineTransform;

public class Battery extends Component {
    public Battery(int x, int y) { super(x, y, "Source"); }

    @Override
    public double getValue() { return 9.0; }

    @Override
    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(rotation * 90), x + width/2, y + height/2);
        int w = (rotation % 2 == 0) ? width : height;
        int h = (rotation % 2 == 0) ? height : width;

        // Vẽ hộp nguồn điện (Style Cyberpunk)
        g2.setColor(Color.BLACK);
        g2.fillRect(x + 10, y, w - 20, h);
        g2.setColor(new Color(0, 255, 255)); // Cyan Border
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x + 10, y, w - 20, h);

        // Các cực
        g2.drawLine(x, y + h/2, x + 10, y + h/2);
        g2.drawLine(x + w - 10, y + h/2, x + w, y + h/2);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("PWR", x + 25, y + 25);
        g2.setTransform(old);
    }
}