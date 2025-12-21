import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Bulb extends Component {
    public Bulb(int x, int y) { super(x, y, "Emitter"); }

    @Override
    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(rotation * 90), x + width/2, y + height/2);
        int w = (rotation % 2 == 0) ? width : height;
        int h = (rotation % 2 == 0) ? height : width;

        g2.setColor(Color.WHITE);
        g2.drawLine(x, y + h/2, x + w, y + h/2);

        int d = 30;
        int cx = x + w/2 - d/2;
        int cy = y + h/2 - d/2;

        if (isPowered) {
            float[] dist = {0.0f, 0.8f};
            Color[] colors = {new Color(0, 255, 0, 255), new Color(0, 255, 0, 0)};
            RadialGradientPaint p = new RadialGradientPaint(new Point2D.Float(x+w/2, y+h/2), 50, dist, colors);
            g2.setPaint(p);
            g2.fillOval(cx - 20, cy - 20, d + 40, d + 40);
            g2.setColor(Color.WHITE);
        } else {
            g2.setColor(new Color(50, 50, 50));
        }

        g2.fillOval(cx, cy, d, d);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(cx, cy, d, d);

        g2.setTransform(old);
    }
}