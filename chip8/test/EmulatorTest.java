import org.junit.Before;
import org.junit.Test;

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


    /*
From: https://en.wikipedia.org/wiki/CHIP-8

4XNN	Cond	if(Vx!=NN)	Skips the next instruction if VX doesn't equal NN. (Usually the next instruction is a jump to skip a code block)
5XY0	Cond	if(Vx==Vy)	Skips the next instruction if VX equals VY. (Usually the next instruction is a jump to skip a code block)
8XYE	BitOp	Vx=Vy=Vy<<1	Shifts VY left by one and copies the result to VX. VF is set to the value of the most significant bit of VY before the shift.[2]
9XY0	Cond	if(Vx!=Vy)	Skips the next instruction if VX doesn't equal VY. (Usually the next instruction is a jump to skip a code block)
CXNN	Rand	Vx=rand()&NN	Sets VX to the result of a bitwise and operation on a random number (Typically: 0 to 255) and NN.
DXYN	Disp	draw(Vx,Vy,N)	Draws a sprite at coordinate (VX, VY) that has a width of 8 pixels and a height of N pixels. Each row of 8 pixels is read as bit-coded starting from memory location I; I value doesn’t change after the execution of this instruction. As described above, VF is set to 1 if any screen pixels are flipped from set to unset when the sprite is drawn, and to 0 if that doesn’t happen
EX9E	KeyOp	if(key()==Vx)	Skips the next instruction if the key stored in VX is pressed. (Usually the next instruction is a jump to skip a code block)
EXA1	KeyOp	if(key()!=Vx)	Skips the next instruction if the key stored in VX isn't pressed. (Usually the next instruction is a jump to skip a code block)
FX07	Timer	Vx = get_delay()	Sets VX to the value of the delay timer.
FX0A	KeyOp	Vx = get_key()	A key press is awaited, and then stored in VX. (Blocking Operation. All instruction halted until next key event)
FX15	Timer	delay_timer(Vx)	Sets the delay timer to VX.
FX18	Sound	sound_timer(Vx)	Sets the sound timer to VX.
FX1E	MEM	I +=Vx	Adds VX to I.[3]
FX29	MEM	I=sprite_addr[Vx]	Sets I to the location of the sprite for the character in VX. Characters 0-F (in hexadecimal) are represented by a 4x5 font.
FX33	BCD	set_BCD(Vx);
*(I+0)=BCD(3);

*(I+1)=BCD(2);

*(I+2)=BCD(1);

Stores the binary-coded decimal representation of VX, with the most significant of three digits at the address in I, the middle digit at I plus 1, and the least significant digit at I plus 2. (In other words, take the decimal representation of VX, place the hundreds digit in memory at location in I, the tens digit at location I+1, and the ones digit at location I+2.)
FX55	MEM	reg_dump(Vx,&I)	Stores V0 to VX (including VX) in memory starting at address I. I is increased by 1 for each value written.
FX65	MEM	reg_load(Vx,&I)	Fills V0 to VX (including VX) with values from memory starting at address I. I is increased by 1 for each value written.

     */

}