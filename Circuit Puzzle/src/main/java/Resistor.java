import java.awt.*;
import java.awt.geom.AffineTransform;

public class Resistor extends Component {
    public Resistor(int x, int y) { super(x, y, "Resistor"); }
    private double resistance = 100.0;

    @Override
    public double getValue() { return resistance; }

    @Override
    public void editValue() {
        String input = javax.swing.JOptionPane.showInputDialog("Resistance Input:", resistance);
        if (input != null) {
            try {
                resistance = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                //
            }
        }
    }
    @Override
    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(rotation * 90), x + width/2, y + height/2);
        int w = (rotation % 2 == 0) ? width : height;
        int h = (rotation % 2 == 0) ? height : width;

        // Hiệu ứng Neon Magenta
        g2.setColor(new Color(255, 0, 255));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x, y + h/2, x + 15, y + h/2);
        g2.drawLine(x + w - 15, y + h/2, x + w, y + h/2);

        // Vẽ Zigzag
        int[] xp = {x+15, x+20, x+30, x+40, x+50, x+60, x+w-15};
        int[] yp = {y+h/2, y+h/2-10, y+h/2+10, y+h/2-10, y+h/2+10, y+h/2-10, y+h/2};
        g2.drawPolyline(xp, yp, xp.length);

        g2.setFont(new Font("Consolas", Font.PLAIN, 10));
        g2.drawString(String.format("%.0fΩ", resistance), x + w/3, y + h + 12);
        g2.setTransform(old);
    }
}