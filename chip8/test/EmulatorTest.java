import org.junit.Test;

import static org.junit.Assert.*;

public class EmulatorTest {

    @Test
    public void test() {
        Emulator emulator = new Emulator();
        int[] data = new int[]{0x1F,0xFF};
        emulator.loadProgram(data);
        emulator.next();
        assertEquals(0xFFF,emulator.i);
    }

}