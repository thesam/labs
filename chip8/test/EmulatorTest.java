import org.junit.Test;

import static org.junit.Assert.*;

public class EmulatorTest {

    @Test
    public void test() {
        Emulator emulator = new Emulator();
        byte[] data = new byte[]{0x1F,(byte) 0xFF};
        emulator.loadProgram(data);
        emulator.next();
        assertEquals(0xFFF,emulator.i);
    }

}