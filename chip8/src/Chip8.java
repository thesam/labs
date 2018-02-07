import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.function.Consumer;

public class Chip8 {

    private final Timer clock;
    private final Timer delay;
    int[] memory = new int[4096];
    int[] v = new int[16];
    int i;
    int pc;

    int delayTimer;
    //delay timer
    //sound timer
    //input
    boolean[][] videoMemory = new boolean[64][32];
    Stack<Integer> stack = new Stack<>();
    private boolean keyPressed;
    int soundTimer;
    boolean waitingForKey;
    private int keyTarget;
    private Consumer<boolean[][]> drawConsumer;

    public Chip8(Timer clock, Timer delay) {
        this.clock = clock;
        this.delay = delay;
        pc = 0x200;
        clock.onTick(this::next);
        delay.onTick(() -> {
            this.delayTimer--;
            if (delayTimer < 0) {
                delayTimer = 0;
            }
        });
    }

    public void next() {
        if (waitingForKey) {
            return;
        }
        int firstbyte = memory[pc++] & 0xFF;
        int secondbyte = memory[pc++] & 0xFF;
        int opcode = ((firstbyte << 8) | secondbyte) & 0xFFFF;
        log(String.format("opcode: %x\n", opcode));
        int firstCode = 0xF000 & opcode;

        //TODO: 0NNN
        if (firstbyte == 0x00) {
            if (secondbyte == 0xE0) {
                log("disp_clear()");
                clearScreen();
                return;
            }
            if (secondbyte == 0xEE) {
                Integer dest = stack.pop();
                log("return: " + dest);
                pc = dest;
                return;
            }
        }
        if (firstCode == 0x1000) {
            int addr = 0x0FFF & opcode;
            log("goto " + addr);
            this.pc = addr;
            return;
        }
        if (firstCode == 0x2000) {
            int addr = 0x0FFF & opcode;
            log("call " + addr);
            stack.push(pc);
            this.pc = addr;
            return;
        }
        if (firstCode == 0x3000) {
            int x = (0x0F00 & opcode) >> 8;
            int nn = (0x00FF & opcode);
            log("skip if v[" + x + "] == " + nn);
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
            log("Add to v[" + x + "]: " + nn);
            v[x] += nn;
            v[x] %= 256;
            return;
        }
        if (firstCode == 0x8000) {
            int lastNibble = opcode & 0x000f;
            int x = (0x0F00 & opcode) >> 8;
            int y = (0x00F0 & opcode) >> 4;

            if (lastNibble == 0) {
                v[x] = v[y];
                return;
            }
            if (lastNibble == 1) {
                v[x] = v[x] | v[y];
                return;
            }
            if (lastNibble == 2) {
                v[x] = v[x] & v[y];
                return;
            }
            if (lastNibble == 3) {
                v[x] = v[x] ^ v[y];
                return;
            }
            if (lastNibble == 4) {
                v[x] = v[x] + v[y];
                if (v[x] > 0xff) {
                    v[0xf] = 1;
                    v[x] = v[x] % 256;
                } else {
                    v[0xf] = 0;
                }
                return;
            }
            if (lastNibble == 5) {
                v[x] = v[x] - v[y];
                if (v[x] < 0) {
                    v[0xf] = 1;
                    v[x] = v[x] & 0xFF;
                }
                return;
            }
            if (lastNibble == 6) {
                v[0xF] = v[y] & 1;
                v[y] = v[y] >> 1;
                v[x] = v[y];
                return;
            }
            if (lastNibble == 7) {
                v[x] = v[y] - v[x];
                if (v[x] < 0) {
                    v[x] = v[x] & 0xFF;
                    v[0xf] = 1;
                }
                return;
            }
            if (lastNibble == 0xE) {
                //TODO MSB
                v[y] = v[y] << 1;
                v[x] = v[y];
                throw new RuntimeException(("MSB"));
                //return;
            }
            throw new RuntimeException("Unknown instruction");
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
            int rand = new Random().nextInt(256);
            v[x] = rand & nn;
            log("v[" + x + "] = " + rand + " & " + nn);
            return;
        }
        if (firstCode == 0xD000) {
            boolean flipped = false;
            int n = opcode & 0x000f;
            int x = v[(0x0F00 & opcode) >> 8];
            int y = v[(0x00F0 & opcode) >> 4];
            log("display at " + x + "," + y + " " + n + " lines");
            log("i = " + i);
            v[0xf] = 0;
            for (int row = 0; row < n; row++) {
                int data = memory[i + row];
                for (int col = 0; col < 8; col++) {
                    boolean spriteSet = ((data >> 7 - col) & 1) > 0;
                    int drawX = x + col;
                    int drawY = y + row;
                    if (spriteSet && drawX < 64 && drawY < 32) {
                        log("DRAW to " + drawX + "," + drawY);
                        boolean currentPixel = videoMemory[drawX][drawY];
                        videoMemory[drawX][drawY] = spriteSet ^ currentPixel;
                        if (videoMemory[drawX][drawY] == false && currentPixel == true) {
                            flipped = true;
                        }
                    }
                }
            }
            if (flipped) {
                v[0xf] = 1;
            }
            if (drawConsumer != null) {
                drawConsumer.accept(Arrays.copyOf(this.videoMemory,this.videoMemory.length));
            }
            return;
        }
        if (firstCode == 0xE000) {
            //TODO: Check actual key
            int x = (0x0F00 & opcode) >> 8;
            if (secondbyte == 0x9E) {
                if (keyPressed) {
                    pc += 2;
                }
            }
            if (secondbyte == 0xA1) {
                if (!keyPressed) {
                    pc += 2;
                }
            }
            return;
        }
        if (firstCode == 0xF000) {
            if (secondbyte == 0x07) {
                int x = (0x0F00 & opcode) >> 8;
                v[x] = delayTimer;

            }
            if (secondbyte == 0x15) {
                int x = (0x0F00 & opcode) >> 8;
                delayTimer = v[x];
            }
            if (secondbyte == 0x18) {
                int x = (0x0F00 & opcode) >> 8;
                soundTimer = v[x];
            }
            if (secondbyte == 0x1E) {
                int x = (0x0F00 & opcode) >> 8;
                i += v[x];
            }
            if (secondbyte == 0x33) {
                int x = (0x0F00 & opcode) >> 8;
                int value = v[x];
                memory[i] = value / 100;
                value = value % 100;
                memory[i + 1] = value / 10;
                value = value % 10;
                memory[i + 2] = value;
            }
            if (secondbyte == 0x0A) {
                waitingForKey = true;
                int x = (0x0F00 & opcode) >> 8;
                keyTarget = x;
            }
            if (secondbyte == 0x55) {
                int x = (0x0F00 & opcode) >> 8;
                for (int n = 0; n <= x; n++) {
                    memory[i++] = v[n];
                }
            }
            if (secondbyte == 0x65) {
                int x = (0x0F00 & opcode) >> 8;
                for (int n = 0; n <= x; n++) {
                    v[n] = memory[i++];
                }
            }
            return;
        }
        throw new UnsupportedOperationException("Unsupported instruction: " + opcode);
    }

    private void clearScreen() {
        videoMemory = new boolean[64][32];
    }

    public void loadProgram(byte... data) {
        int base = 0x200;
        for (int i = 0; i < data.length; i++) {
            memory[base + i] = data[i];
        }
    }

    public void loadProgram(int... data) {
        int base = 0x200;
        for (int i = 0; i < data.length; i++) {
            memory[base + i] = data[i] & 0xFF;
        }
    }

    private void log(String str) {
        //System.err.println(str);
    }

    public void keyPressed() {
        log("keyPressed");
        this.keyPressed = true;
        if (waitingForKey) {
            this.waitingForKey = false;
            //TODO: Hardcoded value
            v[keyTarget] = 0xff;
        }
    }

    public void keyReleased() {
        log("keyReleased");
        this.keyPressed = false;
    }

    public void boot() {
        delay.start();
        clock.start();
    }

    public void onDraw(Consumer<boolean[][]> consumer) {
        this.drawConsumer = consumer;
    }
}
