import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Timer {
    private int delayUs;
    private Runnable onTickCb;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> scheduledFuture;

    public Timer(int hz) {
        this.delayUs = (int) ((1.0 / hz) * 1_000_000);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public void onTick(Runnable onTickCb) {
        this.onTickCb = onTickCb;
    }

    public void start() {
        if (onTickCb != null) {
            scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(onTickCb, 0, delayUs, TimeUnit.MICROSECONDS);
        }
    }
}
