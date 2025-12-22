import java.awt.*;
import java.awt.geom.AffineTransform;

public class Wire extends Component {
    public Wire(int x, int y) { super(x, y, "Conduit"); }

    @Override
    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(rotation * 90), x + width/2, y + height/2);
        int w = (rotation % 2 == 0) ? width : height;
        int h = (rotation % 2 == 0) ? height : width;

        if (isPowered) {
            g2.setColor(new Color(0, 255, 0));
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(x, y + h/2, x + w, y + h/2);
            g2.setColor(new Color(200, 255, 200));
            g2.setStroke(new BasicStroke(1));
        } else {
            g2.setColor(new Color(100, 100, 100));
            g2.setStroke(new BasicStroke(2));
        }

        g2.drawLine(x, y + h/2, x + w, y + h/2);

        g2.fillOval(x - 3, y + h/2 - 3, 6, 6);
        g2.fillOval(x + w - 3, y + h/2 - 3, 6, 6);

        g2.setTransform(old);
    }
}