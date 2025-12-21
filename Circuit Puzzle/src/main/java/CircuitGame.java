import javax.swing.*;
import java.awt.*;

public class CircuitGame extends JFrame {

    public CircuitGame() {
        setTitle("NEON CIRCUIT: OVERDRIVE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(new BorderLayout());
        setResizable(false);

        Color darkBG = new Color(20, 20, 30);
        getContentPane().setBackground(darkBG);

        CircuitBoard board = new CircuitBoard();
        add(board, BorderLayout.CENTER);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 30, 40));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setPreferredSize(new Dimension(200, 720));

        JLabel title = new JLabel("<html><div style='text-align:center; color:#00FFFF; font-size:20px; font-weight:bold;'>CIRCUIT<br>BREAKER</div></html>");
        title.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        sidebar.add(new GameButton("SOURCE [9V]", e -> board.addComponent(new Battery(100, 100))));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(new GameButton("RESISTOR", e -> board.addComponent(new Resistor(100, 100))));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(new GameButton("EMITTER", e -> board.addComponent(new Bulb(100, 100))));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(new GameButton("CONDUIT", e -> board.addComponent(new Wire(100, 100))));

        sidebar.add(Box.createVerticalGlue());
        
        GameButton btnRun = new GameButton("▶ EXECUTE", e -> board.simulate());
        btnRun.setColors(new Color(46, 204, 113), Color.WHITE);
        sidebar.add(btnRun);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        GameButton btnClear = new GameButton("⚠ RESET", e -> board.resetBoard());
        btnClear.setColors(new Color(231, 76, 60), Color.WHITE);
        sidebar.add(btnClear);

        add(sidebar, BorderLayout.WEST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CircuitGame().setVisible(true));
    }
}