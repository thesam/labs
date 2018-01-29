import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmulatorTest {

    private Emulator emulator;

    @Before
    public void setup() {
        emulator = new Emulator();
    }

    @Test
    public void setI() {
        emulator.loadProgram((byte) 0x1F,(byte) 0xFF);
        emulator.next();
        assertEquals(0xFFF,emulator.pc);
    }

    @Test
    public void drawFullRow() {
        emulator.i = 0;
        emulator.memory[0] = 0xFF;
        emulator.loadProgram((byte) 0xD0,(byte) 0x01);
        emulator.next();
        assertEquals(true,emulator.videoMemory[0][0]);
        assertEquals(true,emulator.videoMemory[1][0]);
        assertEquals(true,emulator.videoMemory[2][0]);
        assertEquals(true,emulator.videoMemory[3][0]);
        assertEquals(true,emulator.videoMemory[4][0]);
        assertEquals(true,emulator.videoMemory[5][0]);
        assertEquals(true,emulator.videoMemory[6][0]);
        assertEquals(true,emulator.videoMemory[7][0]);
    }

    @Test
    public void drawHalfRow() {
        emulator.i = 0;
        emulator.memory[0] = 0xF0;
        emulator.loadProgram((byte) 0xD0,(byte) 0x01);
        emulator.next();
        assertEquals(true,emulator.videoMemory[0][0]);
        assertEquals(true,emulator.videoMemory[1][0]);
        assertEquals(true,emulator.videoMemory[2][0]);
        assertEquals(true,emulator.videoMemory[3][0]);
        assertEquals(false,emulator.videoMemory[4][0]);
        assertEquals(false,emulator.videoMemory[5][0]);
        assertEquals(false,emulator.videoMemory[6][0]);
        assertEquals(false,emulator.videoMemory[7][0]);
    }

}