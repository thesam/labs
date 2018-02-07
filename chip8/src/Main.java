import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = selectFile();

        byte[] bytes = Files.readAllBytes(file.toPath());
        Timer cpuTimer = new Timer(1000);
        Chip8 chip8 = new Chip8(cpuTimer, new Timer(60));
        chip8.loadProgram(bytes);

        Gui gui = showGui(chip8);

        cpuTimer.onTick(() -> {
            chip8.next();
            gui.repaint();
        });

        chip8.boot();
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
