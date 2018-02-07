import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class Gui {

    private final Runnable onKeyPress;
    private final Runnable onKeyRelease;
    private JFrame jFrame;
    private JPanel jPanel;
    private boolean[][] videoMemory;
    private BufferedImage bufferedImage = new BufferedImage(64, 32, BufferedImage.TYPE_3BYTE_BGR);

    public Gui(Runnable onKeyPress, Runnable onKeyRelease) {
        this.videoMemory = new boolean[64][32];
        this.onKeyPress = onKeyPress;
        this.onKeyRelease = onKeyRelease;
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jPanel = new ScreenPanel();
            jFrame.getContentPane().add(jPanel);
            jPanel.setPreferredSize(new Dimension(640, 320));
            jFrame.pack();
            jFrame.setResizable(false);
            jFrame.setLocationRelativeTo(null);
            jFrame.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    onKeyPress.run();
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {
                    onKeyRelease.run();
                }
            });
            jFrame.setVisible(true);
        });
    }

    public void repaint() {
        if (jPanel != null) {
            SwingUtilities.invokeLater(() -> {
                jPanel.repaint();
            });
        }
    }

    public void draw(boolean[][] videoMemory) {
        this.videoMemory = videoMemory;
        repaint();
    }

    class ScreenPanel extends JPanel {
        @Override
        public void paint(Graphics graphics) {
            Graphics2D graphics2d = (Graphics2D) graphics;
            Graphics imageGraphics = bufferedImage.getGraphics();
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 32; y++) {
                    if (videoMemory[x][y]) {
                        imageGraphics.setColor(Color.BLACK);
                    } else {
                        imageGraphics.setColor(Color.WHITE);
                    }
                    imageGraphics.drawLine(x, y, x, y);
                }
            }
            graphics2d.drawImage(bufferedImage, 0, 0, 640, 320, null);
        }
    }

}
