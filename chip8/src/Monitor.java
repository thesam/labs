import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Monitor {

    private final JFrame jFrame;
    private final JPanel jPanel;
    private boolean[][] videoMemory;
    private BufferedImage bufferedImage = new BufferedImage(64,32,BufferedImage.TYPE_3BYTE_BGR);

    public Monitor(boolean[][] videoMemory, KeyListener keyListener) {
        this.videoMemory = videoMemory;

        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Insets insets = jFrame.getInsets();
        jPanel = new ScreenPanel();
        jFrame.getContentPane().add(jPanel);
        jPanel.setPreferredSize(new Dimension(640,320));
        jFrame.pack();
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jPanel.addKeyListener(keyListener);
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
                    Graphics imageGraphics = bufferedImage.getGraphics();
                    //graphics2d.scale(2,2);
                    if (videoMemory[x][y]) {
                        imageGraphics.setColor(Color.BLACK);
                    } else {
                        imageGraphics.setColor(Color.WHITE);
                    }
                    imageGraphics.drawLine(x,y,x,y);
                    graphics2d.drawImage(bufferedImage,0,0,640,320,null);
                }
            }
        }
    }

}
