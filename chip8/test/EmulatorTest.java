import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class EmulatorTest {

    private Emulator emulator;

    @Before
    public void setup() {
        emulator = new Emulator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void _0nnn() {
        execute(0x0000);
    }

    @Test
    public void _00e0() {
        emulator.videoMemory[0][0] = true;

        execute(0x00E0);

        assertEquals(false,emulator.videoMemory[0][0]);
    }

    @Test
    public void _00ee() {
        emulator.stack.push(0xFFF);

        execute(0x00EE);

        assertEquals(0xFFF,emulator.pc);
        assertEquals(true,emulator.stack.empty());
    }

    @Test
    public void _1nnn() {
        execute(0x1FFF);

        assertEquals(0xFFF,emulator.pc);
    }

    @Test
    public void _2nnn() {
        execute(0x2FFF);

        assertEquals(0xFFF,emulator.pc);
        assertEquals(0x200 + 2,(long)emulator.stack.peek());
    }

    @Test
    public void _3xnn_skip() {
        emulator.v[0] = 0xff;

        execute(0x30FF);

        assertEquals(0x200 + 4, emulator.pc);
    }

    @Test
    public void _3xnn_noskip() {
        emulator.v[0] = 0x00;

        execute(0x30FF);

        assertEquals(0x200 + 2, emulator.pc);
    }

    @Test
    public void _4xnn_skip() {
        emulator.v[0] = 0x00;

        execute(0x40FF);

        assertEquals(0x200 + 4, emulator.pc);
    }

    @Test
    public void _4xnn_noskip() {
        emulator.v[0] = 0xFF;

        execute(0x40FF);

        assertEquals(0x200 + 2, emulator.pc);
    }

    @Test
    public void _5xy0_noskip() {
        emulator.v[0] = 0xFE;
        emulator.v[1] = 0xFF;

        execute(0x5010);

        assertEquals(0x200 + 2, emulator.pc);
    }

    @Test
    public void _5xy0_skip() {
        emulator.v[0] = 0xFF;
        emulator.v[1] = 0xFF;

        execute(0x5010);

        assertEquals(0x200 + 4, emulator.pc);
    }

    @Test
    public void _6fnn() {
        execute(0x6AFF);

        assertEquals(0xFF,emulator.v[0xa]);
    }

    @Test
    public void _7xnn() {
        emulator.v[0xa] = 0;
        execute(0x7ABB);

        assertEquals(0xBB,emulator.v[0xa]);
    }

    @Test
    public void _7xnn_overflow() {
        emulator.v[0xA] = 1;
        execute(0x7AFF);

        assertEquals(0x00,emulator.v[0xa]);
        assertEquals(0,emulator.v[0xf]);
    }

    @Test
    public void _8xy0() {
        emulator.v[0xb] = 0xff;
        execute(0x8AB0);

        assertEquals(0xFF,emulator.v[0xa]);
    }

    @Test
    public void _8xy1() {
        emulator.v[0xa] = 0x0f;
        emulator.v[0xb] = 0xf0;
        execute(0x8AB1);

        assertEquals(0xFF,emulator.v[0xa]);
    }

    @Test
    public void _8xy2() {
        emulator.v[0xa] = 0x0f;
        emulator.v[0xb] = 0xf0;
        execute(0x8AB2);

        assertEquals(0x00,emulator.v[0xa]);
    }

    @Test
    public void _8xy3() {
        emulator.v[0xa] = 0x0f;
        emulator.v[0xb] = 0xff;
        execute(0x8AB3);

        assertEquals(0xF0,emulator.v[0xa]);
    }

    @Test
    public void _8xy4_nocarry() {
        emulator.v[0xa] = 0x01;
        emulator.v[0xb] = 0x01;
        execute(0x8AB4);

        assertEquals(0x02,emulator.v[0xa]);
        assertEquals(0,emulator.v[0xf]);
    }

    @Test
    public void _8xy4_carry() {
        emulator.v[0xa] = 0x01;
        emulator.v[0xb] = 0xff;
        execute(0x8AB4);

        assertEquals(0x00,emulator.v[0xa]);
        assertEquals(1,emulator.v[0xf]);
    }

    @Test
    public void _8xy5_noborrow() {
        emulator.v[0xa] = 0x01;
        emulator.v[0xb] = 0x01;
        execute(0x8AB5);

        assertEquals(0x00,emulator.v[0xa]);
        assertEquals(0,emulator.v[0xf]);
    }

    @Test
    public void _8xy5_borrow() {
        emulator.v[0xa] = 0x00;
        emulator.v[0xb] = 0x01;
        execute(0x8AB5);

        assertEquals(0xff,emulator.v[0xa]);
        assertEquals(1,emulator.v[0xf]);
    }

    @Test
    public void _8xy6_lsb() {
        emulator.v[0xa] = 0x00;
        emulator.v[0xb] = 0xF1;
        execute(0x8AB6);

        assertEquals(0xf0 >> 1,emulator.v[0xa]);
        assertEquals(0xf0 >> 1,emulator.v[0xb]);
        assertEquals(1,emulator.v[0xf]);
    }

    @Test
    public void _8xy6_nolsb() {
        emulator.v[0xa] = 0x00;
        emulator.v[0xb] = 0xF0;
        execute(0x8AB6);

        assertEquals(0xF0 >> 1 ,emulator.v[0xa]);
        assertEquals(0xF0 >> 1,emulator.v[0xb]);
        assertEquals(0,emulator.v[0xf]);
    }


    @Test
    public void _8xy7_noborrow() {
        emulator.v[0xa] = 0xFF;
        emulator.v[0xb] = 0xFF;
        execute(0x8AB7);

        assertEquals(0 ,emulator.v[0xa]);
        assertEquals(0, emulator.v[0xf]);
    }

    @Test
    public void _8xy7_borrow() {
        emulator.v[0xa] = 0x02;
        emulator.v[0xb] = 0x01;
        execute(0x8AB7);

        assertEquals(0xFF ,emulator.v[0xa]);
        assertEquals(1, emulator.v[0xf]);
    }

    @Test
    public void _9xy0() {
        emulator.v[0] = 0xFE;
        emulator.v[1] = 0xFF;

        execute(0x9010);

        assertEquals(0x200 + 4, emulator.pc);
    }

    @Test
    public void _annn() {
        execute(0xAFFF);

        assertEquals(0xFFF,emulator.i);
    }


    @Test
    public void _bnnn() {
        emulator.v[0] = 2;
        execute(0xB001);

        assertEquals(0x0003,emulator.pc);
    }

    @Test
    public void _fx07() {
        emulator.delayTimer = 67;

        execute(0xFA07);

        assertEquals(67, emulator.v[0xA]);
    }

    @Test
    public void _fx15() {
        emulator.v[0xa] = 55;

        execute(0xFA15);

        assertEquals(55, emulator.delayTimer);
    }

    @Test
    public void _fx18() {
        emulator.v[0xa] = 55;

        execute(0xFA18);

        assertEquals(55, emulator.soundTimer);
    }

    @Test
    public void _fx1e() {
        emulator.i = 0;
        emulator.v[1] = 11;

        execute(0xF11E);

        assertEquals(11,emulator.i);
    }

    @Test
    public void _fx33() {
        emulator.i = 0;
        emulator.v[1] = 321;

        execute(0xF133);

        assertEquals(3,emulator.memory[emulator.i]);
        assertEquals(2,emulator.memory[emulator.i + 1]);
        assertEquals(1,emulator.memory[emulator.i + 2]);
    }

    @Test
    public void _fx0a_block() {
        execute(0xF00A);
        int pc = emulator.pc;
        emulator.next();
        assertEquals(pc,emulator.pc);
    }

    @Test
    public void _fx0a_block_stop() {
        execute(0xF00A);
        emulator.keyPressed();

        assertEquals(0xff,emulator.v[0]);
        emulator.memory[0x202] = 0xF1;
        emulator.memory[0x203] = 0x1E;
        int pc = emulator.pc;
        emulator.next();
        assertEquals(pc+2, emulator.pc);
    }

    @Test
    public void _fx55() {
        IntStream.rangeClosed(0,0xF).forEach(x ->
            emulator.v[x] = x
        );
        emulator.i = 0;

        execute(0xff55);

        IntStream.rangeClosed(0,0xF).forEach(x ->
                assertEquals(emulator.memory[x],x)
        );

        assertEquals(16,emulator.i);
    }


    @Test
    public void _fx65() {
        IntStream.rangeClosed(0,0xF).forEach(x ->
                emulator.memory[x] = x
        );
        emulator.i = 0;

        execute(0xff65);

        IntStream.rangeClosed(0,0xF).forEach(x ->
                assertEquals(emulator.v[x],x)
        );

        assertEquals(16,emulator.i);
    }


    private void execute(int opcode) {
        int byte0 = (opcode & 0xFF00) >> 8;
        int byte1 = (opcode & 0xFF);
        emulator.loadProgram(byte0,byte1);
        emulator.next();
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

    @Test
    public void drawWithoutCollision() {
        emulator.i = 0;
        emulator.memory[0] = 0xFF;
        emulator.loadProgram((byte) 0xD0,(byte) 0x01);
        emulator.next();
        assertEquals(0,emulator.v[0xf]);
    }

    @Test
    public void drawWithCollision() {
        emulator.i = 0;
        emulator.memory[0] = 0xFF;
        emulator.videoMemory[0][0] = true;
        emulator.loadProgram((byte) 0xD0,(byte) 0x01);
        emulator.next();
        assertEquals(1,emulator.v[0xf]);
    }


/*
From: https://en.wikipedia.org/wiki/CHIP-8

8XYE	BitOp	Vx=Vy=Vy<<1	Shifts VY left by one and copies the result to VX. VF is set to the value of the most significant bit of VY before the shift.[2]
CXNN	Rand	Vx=rand()&NN	Sets VX to the result of a bitwise and operation on a random number (Typically: 0 to 255) and NN.
DXYN	Disp	draw(Vx,Vy,N)	Draws a sprite at coordinate (VX, VY) that has a width of 8 pixels and a height of N pixels. Each row of 8 pixels is read as bit-coded starting from memory location I; I value doesn’t change after the execution of this instruction. As described above, VF is set to 1 if any screen pixels are flipped from set to unset when the sprite is drawn, and to 0 if that doesn’t happen
EX9E	KeyOp	if(key()==Vx)	Skips the next instruction if the key stored in VX is pressed. (Usually the next instruction is a jump to skip a code block)
EXA1	KeyOp	if(key()!=Vx)	Skips the next instruction if the key stored in VX isn't pressed. (Usually the next instruction is a jump to skip a code block)
FX29	MEM	I=sprite_addr[Vx]	Sets I to the location of the sprite for the character in VX. Characters 0-F (in hexadecimal) are represented by a 4x5 font.
     */

}