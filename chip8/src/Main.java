import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.file.Files;

public class Main {

    private static Monitor monitor;
    private static Emulator emulator;
    private static JFileChooser jFileChooser;

    public static void main(String[] args) throws IOException {
        jFileChooser = new JFileChooser("");
        jFileChooser.showDialog(null, null);
        File file = jFileChooser.getSelectedFile();
        byte[] bytes = Files.readAllBytes(file.toPath());
        emulator = new Emulator();
        monitor = new Monitor(emulator.videoMemory, new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                emulator.keyPressed();

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                emulator.keyReleased();
            }
        });
        monitor.show();
        emulator.loadProgram(bytes);
        int count = 0;
        Timer delayTimer = new Timer();
        delayTimer.onTick(() -> {
            emulator.delayTimer--;
            if (emulator.delayTimer < 0) {
                emulator.delayTimer = 0;
            }
            //System.err.println("TICK");
        });
        delayTimer.start();
        while (true) {
            //System.err.print(count + ": ");
            emulator.next();
            monitor.repaint();
            count++;
        }
        //System.err.println("STOP");
    }
}
