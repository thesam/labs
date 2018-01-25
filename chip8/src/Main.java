import javax.swing.*;
import java.io.*;
import java.nio.file.Files;

public class Main {

    private static Monitor monitor;
    private static Emulator emulator;
    private static JFileChooser jFileChooser;

    public static void main(String[] args) throws IOException {
        jFileChooser = new JFileChooser("");
        jFileChooser.showDialog(null,null);
        File file = jFileChooser.getSelectedFile();
        byte[] bytes = Files.readAllBytes(file.toPath());
        emulator = new Emulator();
        monitor = new Monitor(emulator.videoMemory);
        monitor.show();
        emulator.loadProgram(bytes);
        while (true) {
            emulator.next();
        }
    }
}
