import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Emulator {

    int[] memory = new int[4096];
    int[] v = new int[16];
    int i;
    int pc;

    //delay timer
    //sound timer
    //input
    boolean[][] videoMemory = new boolean[64][32];
    Stack<Integer> stack = new Stack<>();

    public Emulator() {
        pc = 0x200;

//        boolean toggle=false;
//        for (int x = 0; x < 64; x++) {
//            for (int y = 0; y < 32; y++) {
//                videoMemory[x][y] = toggle;
//                toggle = !toggle;
//            }
//        }
    }

    public void next() {
        int firstbyte = memory[pc++];
        int secondbyte = memory[pc++];
        int opcode = (firstbyte << 8) | secondbyte;
        System.err.printf("opcode: %x\n", opcode);
        int firstCode = 0xF000 & opcode;

        //TODO: 0NNN
        if (firstbyte == 0x00) {
            if (secondbyte == 0xE0) {
                log("disp_clear()");
                clearScreen();
            }
            if (secondbyte == 0xEE) {
                Integer dest = stack.pop();
                log("return: " + dest);
                pc = dest;
            }
            return;
        }
        if (firstCode == 0x1000) {
            int addr = 0x0FFF & opcode;
            log("goto " + addr);
            this.pc = addr;
            return;
        }
        if (firstCode == 0x2000) {
            int addr = 0x0FFF & opcode;
            //TODO: Add i + 2 to return after?
            log("call " + addr);
            stack.push(pc);
            this.pc = addr;
            return;
        }
        if (firstCode == 0x3000) {
            int x = (0x0F00 & opcode) >> 8;
            int nn = (0x00FF & opcode);
            log("skip if v[x] == nn");
            if (v[x] == nn) {
                pc = pc + 2;
            }
            return;
        }
        if (firstCode == 0x4000) {
            int x = (0x0F00 & opcode) >> 8;
            int nn = (0x00FF & opcode);
            if (v[x] != nn) {
                pc = pc + 2;
            }
            return;
        }
        if (firstCode == 0x5000) {
            int x = (0x0F00 & opcode) >> 8;
            int y = (0x00F0 & opcode) >> 4;
            if (v[x] == v[y]) {
                pc = pc + 2;
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
            int x = (0x0F00 & opcode) >> 8;
            int nn = (0x00FF & opcode);
            v[x] += nn;
            return;
        }
        if (firstCode == 0x8000) {
            int lastNibble = opcode & 0x000f;
            int x = (0x0F00 & opcode) >> 8;
            int y = (0x00F0 & opcode) >> 4;

            if (lastNibble == 0) {
                v[x] = v[y];
            }
            if (lastNibble == 1) {
                v[x] = v[x] | v[y];
            }
            if (lastNibble == 2) {
                v[x] = v[x] & v[y];
            }
            if (lastNibble == 3) {
                v[x] = v[x] ^ v[y];
            }
            if (lastNibble == 4) {
                //TODO: Carry
                v[x] = v[x] + v[y];
            }
            if (lastNibble == 5) {
                //TODO: Borrow
                v[x] = v[x] - v[y];
            }
            //TODO: 6,7,E

            //TODO:
            return;
        }
        if (firstCode == 0x9000) {
            int x = (0x0F00 & opcode) >> 8;
            int y = (0x00F0 & opcode) >> 4;
            if (v[x] != v[y]) {
                pc = pc + 2;
            }
            return;
        }
        if (firstCode == 0xA000) {
            int addr = 0x0FFF & opcode;
            log("i = " + addr);
            this.i = addr;
            return;
        }
        if (firstCode == 0xB000) {
            int addr = 0x0FFF & opcode;
            this.pc = v[0] + addr;
            return;
        }
        if (firstCode == 0xC000) {
            int x = (0x0F00 & opcode) >> 8;
            int nn = (0x00FF & opcode);
            //TODO: real random
            int rand = 1;
            v[x] = rand & nn;
            log("v[x] = rand & nn");
            return;
        }
        if (firstCode == 0xD000) {
            //TODO: Calculate correct coords
            int n = opcode & 0x000f;
            int x = (0x0F00 & opcode) >> 8;
            int y = (0x00F0 & opcode) >> 4;
            for (int row = 0; row < n; row++) {
                int data = i + row;
                for (int col = 0; col < 8; col++) {
                    boolean spriteSet = ((data >> col) & 1) > 0;
                    videoMemory[col][row] = spriteSet;
                }
            }
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
        throw new RuntimeException("Unsupported instruction: " + opcode);
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

    private void log(String str) {
        System.err.println(str);
    }
}
