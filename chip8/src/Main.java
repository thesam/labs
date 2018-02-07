import javax.swing.*;
import java.io.*;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws IOException {
        Chip8 chip8 = new Chip8(new Timer(1000), new Timer(60));

        File file = selectFile();
        byte[] bytes = Files.readAllBytes(file.toPath());
        chip8.loadProgram(bytes);

        Gui gui = initGui(chip8);
        chip8.onDraw(gui::draw);

        chip8.boot();
    }

    private static Gui initGui(Chip8 chip8) {
        Gui gui = new Gui(() -> chip8.keyPressed(),()->chip8.keyReleased());
        gui.show();
        return gui;
    }

    private static File selectFile() {
        JFileChooser jFileChooser = new JFileChooser("");
        jFileChooser.showDialog(null, null);
        return jFileChooser.getSelectedFile();
    }
}
