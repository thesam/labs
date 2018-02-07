import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = selectFile();

        byte[] bytes = Files.readAllBytes(file.toPath());
        Chip8 chip8 = new Chip8();
        chip8.loadProgram(bytes);

        Gui gui = showGui(chip8);

        Timer delayTimer = new Timer(16700);
        delayTimer.onTick(() -> {
            chip8.delayTimer--;
            if (chip8.delayTimer < 0) {
                chip8.delayTimer = 0;
            }
            //System.err.println("TICK");
        });
        delayTimer.start();

        Timer cpuTimer = new Timer(1000);
        cpuTimer.onTick(() -> {
            chip8.next();
            gui.repaint();
        });

        cpuTimer.start();
    }

    private static Gui showGui(Chip8 chip8) {
        Gui gui = new Gui(chip8.videoMemory, new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                chip8.keyPressed();

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                chip8.keyReleased();
            }
        });
        gui.show();
        return gui;
    }

    private static File selectFile() {
        JFileChooser jFileChooser = new JFileChooser("");
        jFileChooser.showDialog(null, null);
        return jFileChooser.getSelectedFile();
    }
    //System.err.println("STOP");
}
