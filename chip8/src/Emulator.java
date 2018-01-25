public class Emulator {

    int[] memory = new int[4096];
    int[] v = new int[16];
    int i;

    //stack
    //delay timer
    //sound timer
    //input
    boolean[][] videoMemory = new boolean[64][32];

    public Emulator() {
        i = 0x200;
        int firstbyte = memory[i++];
        int secondbyte = memory[i++];

        boolean toggle=false;
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 32; y++) {
                videoMemory[x][y] = toggle;
                toggle = !toggle;
            }
        }
    }
}
