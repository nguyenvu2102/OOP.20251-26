import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CircuitBoard extends JPanel {
    private List<Component> components = new ArrayList<>();
    private Component selectedComponent = null;
    private int dragOffsetX, dragOffsetY;
    private boolean levelComplete = false;

    private Consumer<String> logger;

    public CircuitBoard() {
        this(System.out::println);
    }

    // Constructor chính
    public CircuitBoard(Consumer<String> logger) {
        this.logger = (logger != null) ? logger : (msg -> {});

        setBackground(new Color(20, 20, 30));

        // Xử lý chuột (Kéo thả & Xoay)
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
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
                    // Hít vào lưới 20px
                    selectedComponent.x = Math.round((float)selectedComponent.x / 20) * 20;
                    selectedComponent.y = Math.round((float)selectedComponent.y / 20) * 20;
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

    public void addComponent(Component c) {
        c.x = 100 + (components.size() * 10) % 300;
        c.y = 100 + (components.size() * 10) % 300;
        components.add(c);
        repaint();
    }

    public void resetBoard() {
        components.clear();
        levelComplete = false;
        repaint();
    }

    public void simulate() {
        logger.accept("--------------------------------");
        logger.accept(">> SYSTEM DIAGNOSTICS INITIATED...");

        // 1. Reset
        for (Component c : components) c.setPowered(false);
        levelComplete = false;

        // 2. Tìm nguồn
        Battery source = null;
        for (Component c : components) {
            if (c instanceof Battery) {
                source = (Battery) c;
                break;
            }
        }

        if (source == null) {
            logger.accept("ERROR: POWER SOURCE NOT FOUND!");
            return;
        }

        double voltage = source.getValue();
        logger.accept(String.format(">> SOURCE DETECTED: %.1fV", voltage));

        // 3. Quét mạch để tìm kết nối
        Set<Component> visited = new HashSet<>();
        List<Component> connectedComponents = new ArrayList<>();

        findConnectedPath(source, visited, connectedComponents);

        // 4. Tính toán
        double totalResistance = 0;
        boolean bulbFound = false;

        for (Component c : connectedComponents) {
            c.setPowered(true);

            if (c instanceof Resistor) {
                totalResistance += c.getValue();
            }
            if (c instanceof Bulb) {
                bulbFound = true;
                levelComplete = true;
            }
        }

        // 5. Xuất báo cáo
        logger.accept(">> SCAN COMPLETE.");
        logger.accept(String.format(">> TOTAL RESISTANCE: %.1f Ω", totalResistance));

        if (bulbFound) {
            if (totalResistance > 0) {
                double current = voltage / totalResistance;
                logger.accept(String.format(">> CIRCUIT CURRENT: %.4f A", current));
                logger.accept(String.format(">> VOLTAGE DROP: %.1f V", voltage));
                logger.accept("SUCCESS: TARGET (BULB) IS ONLINE.");
            } else {
                logger.accept("WARNING: SHORT CIRCUIT! (Infinite Current)");
                logger.accept(">> SAFETY FUSE ENGAGED.");
                levelComplete = false;
            }
        } else {
            logger.accept("FAILURE: CIRCUIT OPEN. TARGET NOT REACHED.");
        }

        repaint();
    }

    // Hàm đệ quy tìm đường đi
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

        // Vẽ lưới
        g2.setColor(new Color(40, 40, 60));
        for (int i = 0; i < getWidth(); i += 40) g2.drawLine(i, 0, i, getHeight());
        for (int i = 0; i < getHeight(); i += 40) g2.drawLine(0, i, getWidth(), i);

        // Vẽ linh kiện
        for (Component c : components) c.draw(g2);

        if (levelComplete) {
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, getHeight() / 2 - 60, getWidth(), 120);

            g2.setFont(new Font("Impact", Font.ITALIC, 60));
            g2.setColor(Color.GREEN);
            String msg = "CIRCUIT ACTIVE";
            int w = g2.getFontMetrics().stringWidth(msg);
            g2.drawString(msg, getWidth()/2 - w/2, getHeight()/2 + 20);

            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.WHITE);
            g2.drawString("SYSTEM ONLINE - GOOD JOB", getWidth()/2 - 140, getHeight()/2 + 50);
        }
    }
}