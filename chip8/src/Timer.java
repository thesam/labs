import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Timer {
    private int delayUs;
    private Runnable onTickCb;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> scheduledFuture;

    public Timer(int delayUs) {
        this.delayUs = delayUs;
    }

    public void onTick(Runnable onTickCb) {
        this.onTickCb = onTickCb;
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(onTickCb, 0, delayUs, TimeUnit.MICROSECONDS);
    }

    public void stop() {
        //scheduledFuture.cancel()
    }
}
