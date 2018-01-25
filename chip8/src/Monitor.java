import javax.swing.*;
import java.awt.*;

public class Monitor {

    private final JFrame jFrame;
    private final JPanel jPanel;
    private boolean[][] videoMemory;

    public Monitor(boolean[][] videoMemory) {
        this.videoMemory = videoMemory;

        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(640,320);
        jPanel = new ScreenPanel();
        jPanel.setSize(640,320);
        //jPanel.getGraphics().f
        jFrame.getContentPane().add(jPanel);
    }

    public void show() {
        jFrame.setVisible(true);
    }

    class ScreenPanel extends JPanel {
        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 32; y++) {
                    Graphics2D graphics2d = (Graphics2D) graphics;
                    graphics2d.scale(10,10);
                    if (videoMemory[x][y]) {
                        graphics.setColor(Color.BLACK);
                    } else {
                        graphics.setColor(Color.WHITE);
                    }
                    graphics.drawLine(x,y,x,y);
                }
            }
        }
    }

}
