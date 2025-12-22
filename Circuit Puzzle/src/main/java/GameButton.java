import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameButton extends JButton {
    private Color baseColor = new Color(40, 40, 50);
    private Color hoverColor = new Color(60, 60, 80);
    private Color textColor = new Color(0, 255, 255);
    private boolean isHovered = false;

    public GameButton(String text, ActionListener action) {
        super(text);
        addActionListener(action);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFont(new Font("Consolas", Font.BOLD, 14));
        setForeground(textColor);

        setMaximumSize(new Dimension(180, 50));
        setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
            public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
        });
    }

    public void setColors(Color bg, Color txt) {
        this.baseColor = bg.darker();
        this.hoverColor = bg;
        this.textColor = txt;
        setForeground(txt);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(baseColor);
        }

        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(textColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        super.paintComponent(g);
    }
}