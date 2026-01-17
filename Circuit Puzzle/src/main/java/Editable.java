import java.awt.*;

public interface Editable {
    default void editValue() {
    }
    static void drawStringCentered(Graphics2D g2, String text, int x, int y, int w, Font font){
        FontMetrics metrics = g2.getFontMetrics(font);
        g2.setFont(font);
        int xCentered = x + (w - metrics.stringWidth(text)) / 2;
        g2.drawString(text, xCentered, y);
    };
}
