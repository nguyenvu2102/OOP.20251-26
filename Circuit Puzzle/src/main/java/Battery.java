package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Battery extends Component implements Editable {
    public Battery(int x, int y) { super(x, y, "Source"); }
    private double voltage = 9.0;

    @Override
    public double getValue() { return voltage; }

    @Override
    public void editValue() {
        String input = javax.swing.JOptionPane.showInputDialog("Volt input:", voltage);
        if (input != null) {
            try {
                voltage = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(null,"Invalid input !", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    @Override
    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(rotation * 90), x + width/2, y + height/2);
        int w = (rotation % 2 == 0) ? width : height;
        int h = (rotation % 2 == 0) ? height : width;

        g2.setColor(Color.BLACK);
        g2.fillRect(x + 10, y, w - 20, h);
        g2.setColor(new Color(0, 255, 255));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x + 10, y, w - 20, h);

        g2.drawLine(x, y + h/2, x + 10, y + h/2);
        g2.drawLine(x + w - 10, y + h/2, x + w, y + h/2);

        Font font = new Font("Arial", Font.BOLD, 12);
        String string = String.format("%.0fV",voltage);
        Editable.drawStringCentered(g2, string, x, y + 25, w, font);

        g2.setTransform(old);
    }
}