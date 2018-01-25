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

        boolean toggle=false;
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 32; y++) {
                videoMemory[x][y] = toggle;
                toggle = !toggle;
            }
        }
    }

    public void next() {
        int firstbyte = memory[i++];
        int secondbyte = memory[i++];
        int opcode = (firstbyte << 8) | secondbyte;
        int firstCode = 0xF000 & opcode;

        //TODO: 0NNN
        if (firstbyte == 0x00) {
            if (secondbyte == 0xE0) {
                clearScreen();
            }
            if (secondbyte == 0xEE) {
                //TODO: return
            }
            return;
        }
        if (firstCode == 0x1000) {
            int addr = 0x0FFF & opcode;
            this.i = addr;
            return;
        }
        if (firstCode == 0x2000) {
            //TODO: Call subroutine
            return;
        }
        if (firstCode == 0x3000) {
            //TODO: Skip
            return;
        }
        if (firstCode == 0x4000) {
            //TODO: Skip
            return;
        }
        if (firstCode == 0x5000) {
            //TODO: Skip
            return;
        }
        if (firstCode == 0x6000) {
            //TODO: Set
            return;
        }
        if (firstCode == 0x7000) {
            //TODO: Add
            return;
        }
    }

    private void clearScreen() {
        videoMemory = new boolean[64][32];
    }

    public void loadProgram(int[] data) {
        int base = 0x200;
        for (int i = 0; i <data.length; i++) {
            memory[base + i] = data[i];
        }
    }
}
