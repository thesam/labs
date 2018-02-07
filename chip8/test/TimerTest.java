import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void shouldCallCallback() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);
        Timer timer = new Timer(60);
        timer.onTick(() -> {
            count.incrementAndGet();
        });
        timer.start();
        Thread.sleep(1000);
        assertTrue(count.get() >= 59);
        assertTrue(count.get() <= 62);
        System.err.println(count.get());
    }

}