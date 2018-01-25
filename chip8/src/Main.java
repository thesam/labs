public class Main {

    private static Monitor monitor;
    private static Emulator emulator;

    public static void main(String[] args) {
        emulator = new Emulator();
        monitor = new Monitor(emulator.videoMemory);
        monitor.show();
    }
}
