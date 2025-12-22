import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CircuitBoard extends JPanel {
    private List<Component> components = new ArrayList<>();
    private Component selectedComponent = null;
    private int dragOffsetX, dragOffsetY;
    private boolean levelComplete = false;

    private String hudVoltage = "-- V";
    private String hudResistance = "-- Ω";
    private String hudCurrent = "-- A";
    private String hudStatus = "STANDBY";
    private Color hudColor = Color.GRAY;
    private boolean showHud = false;

    public CircuitBoard() {
        setBackground(new Color(20, 20, 30));

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                resetHud();
                for (int i = components.size() - 1; i >= 0; i--) {
                    Component c = components.get(i);
                    if (c.contains(e.getPoint())) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            c.rotate();
                            repaint();
                        } else {
                            selectedComponent = c;
                            dragOffsetX = e.getX() - c.x;
                            dragOffsetY = e.getY() - c.y;
                        }
                        return;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedComponent != null) {
                    selectedComponent.x = Math.round((float) selectedComponent.x / 20) * 20;
                    selectedComponent.y = Math.round((float) selectedComponent.y / 20) * 20;
                    selectedComponent = null;
                    repaint();
                }
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedComponent != null) {
                    selectedComponent.x = e.getX() - dragOffsetX;
                    selectedComponent.y = e.getY() - dragOffsetY;
                    repaint();
                }
            }
        });
    }

    private void resetHud() {
        showHud = false;
        hudStatus = "EDITING...";
        hudColor = Color.GRAY;
        repaint();
    }

    public void addComponent(Component c) {
        c.x = 100 + (components.size() * 10) % 300;
        c.y = 100 + (components.size() * 10) % 300;
        components.add(c);
        resetHud();
    }

    public void resetBoard() {
        components.clear();
        levelComplete = false;
        resetHud();
        repaint();
    }

    public void simulate() {
        for (Component c : components) c.setPowered(false);
        levelComplete = false;
        showHud = true;

        Battery source = null;
        for (Component c : components) {
            if (c instanceof Battery) {
                source = (Battery) c;
                break;
            }
        }

        if (source == null) {
            hudStatus = "NO POWER";
            hudColor = Color.RED;
            hudVoltage = "0 V";
            hudResistance = "∞ Ω";
            hudCurrent = "0 A";
            repaint();
            return;
        }

        double voltage = source.getValue();
        hudVoltage = String.format("%.1f V", voltage);

        Set<Component> visited = new HashSet<>();
        List<Component> connected = new ArrayList<>();
        findConnectedPath(source, visited, connected);

        double totalR = 0;
        boolean bulbFound = false;

        for (Component c : connected) {
            c.setPowered(true);
            if (c instanceof Resistor) totalR += c.getValue();
            if (c instanceof Bulb) bulbFound = true;
        }

        hudResistance = String.format("%.1f Ω", totalR);

        if (bulbFound) {
            levelComplete = true;
            if (totalR > 0) {
                double I = voltage / totalR;
                hudCurrent = String.format("%.4f A", I);
                hudStatus = "ONLINE";
                hudColor = Color.GREEN;
            } else {
                hudCurrent = "INFINITE";
                hudStatus = "SHORT CIRCUIT";
                hudColor = Color.RED;
                levelComplete = false;
            }
        } else {
            hudCurrent = "0 A";
            hudStatus = "OPEN CIRCUIT";
            hudColor = Color.ORANGE;
        }

        repaint();
    }

    private void findConnectedPath(Component current, Set<Component> visited, List<Component> list) {
        visited.add(current);
        list.add(current);
        for (Component neighbor : components) {
            if (!visited.contains(neighbor) && current.isTouching(neighbor)) {
                findConnectedPath(neighbor, visited, list);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(40, 40, 60));
        for (int i = 0; i < getWidth(); i += 40) g2.drawLine(i, 0, i, getHeight());
        for (int i = 0; i < getHeight(); i += 40) g2.drawLine(0, i, getWidth(), i);

        for (Component c : components) c.draw(g2);

        if (showHud) {
            drawHUD(g2);
        }
    }

    private void drawHUD(Graphics2D g2) {
        int x = 20;
        int y = 20;
        int w = 220;
        int h = 130;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, w, h, 15, 15);

        g2.setColor(hudColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, w, h, 15, 15);

        g2.setFont(new Font("Consolas", Font.BOLD, 16));
        g2.drawString("SYSTEM MONITOR", x + 40, y + 25);
        g2.drawLine(x + 10, y + 35, x + w - 10, y + 35);

        g2.setFont(new Font("Consolas", Font.PLAIN, 14));
        g2.setColor(Color.CYAN);
        g2.drawString("Voltage: " + hudVoltage, x + 20, y + 60);
        g2.setColor(Color.MAGENTA);
        g2.drawString("Resist : " + hudResistance, x + 20, y + 80);
        g2.setColor(Color.YELLOW);
        g2.drawString("Current: " + hudCurrent, x + 20, y + 100);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(hudColor);
        g2.drawString(hudStatus, x + w - 100, y + 120);
    }
}