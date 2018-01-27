import java.util.ArrayList;
import java.util.List;

public class Emulator {

    int[] memory = new int[4096];
    int[] v = new int[16];
    int i;

    //stack
    //delay timer
    //sound timer
    //input
    boolean[][] videoMemory = new boolean[64][32];
    List<Integer> stack = new ArrayList<>();

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
            int addr = 0x0FFF & opcode;
            //TODO: Add i + 2 to return after?
            stack.add(i);
            this.i = addr;
            return;
        }
        if (firstCode == 0x3000) {
            int x = (0x0F00 & opcode) >> 8;
            int nn = (0x00FF & opcode);
            if (v[x] == nn) {
                i = i + 2;
            }
            return;
        }
        if (firstCode == 0x4000) {
            int x = (0x0F00 & opcode) >> 8;
            int nn = (0x00FF & opcode);
            if (v[x] != nn) {
                i = i + 2;
            }
            return;
        }
        if (firstCode == 0x5000) {
            int x = (0x0F00 & opcode) >> 8;
            int y = (0x00F0 & opcode) >> 4;
            if (v[x] == v[y]) {
                i = i + 2;
            }
            return;
        }
        if (firstCode == 0x6000) {
            int x = (0x0F00 & opcode) >> 8;
            int nn = (0x00FF & opcode);
            v[x] = nn;
            return;
        }
        if (firstCode == 0x7000) {
            //TODO: Add
            return;
        }
        if (firstCode == 0x8000) {
            //TODO:
            return;
        }
        if (firstCode == 0x9000) {
            //TODO:
            return;
        }
        if (firstCode == 0xA000) {
            //TODO:
            return;
        }
        if (firstCode == 0xB000) {
            //TODO:
            return;
        }
        if (firstCode == 0xC000) {
            //TODO:
            return;
        }
        if (firstCode == 0xD000) {
            //TODO:
            return;
        }
        if (firstCode == 0xE000) {
            //TODO:
            return;
        }
        if (firstCode == 0xF000) {
            //TODO:
            return;
        }
    }

    private void clearScreen() {
        videoMemory = new boolean[64][32];
    }

    public void loadProgram(byte[] data) {
        int base = 0x200;
        for (int i = 0; i <data.length; i++) {
            memory[base + i] = data[i];
        }
    }
}
