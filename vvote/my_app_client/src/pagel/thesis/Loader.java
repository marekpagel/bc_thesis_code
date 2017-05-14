package pagel.thesis;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Loader {
    final private static long MICRO_CONST = 1000000L;
    final private int phaseCount;
    final private int phaseLen;
    final private double period;
    final private ScheduledExecutorService service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     *
     * @param phaseCount How many phases to run. Each phase creates twice the load as previous one.
     * @param phaseLen Length of single phase in seconds.
     * @param rate Load of first phase (executions/minute).
     */
    public Loader(int phaseCount, int phaseLen, double rate) {
        this.phaseCount = phaseCount;
        this.phaseLen = phaseLen;
        this.period = 1D / (rate / 60) * MICRO_CONST;
    }

    /**
     *
     * @param runnable Runnable that imitates the client (ie. sends a request).
     */
    public void start(Runnable runnable) {
        // schedule all phases
        for (int i = 0; i < phaseCount; i++) {
            ScheduledFuture<?> f = service.scheduleAtFixedRate(runnable, i * phaseLen * MICRO_CONST,
                    (long) (period / Math.pow(2, i)), TimeUnit.MICROSECONDS);
            service.schedule(() -> f.cancel(true), (i + 1) * phaseLen * MICRO_CONST, TimeUnit.MICROSECONDS);
        }
        // shut down the executor service after all load phases are done
        service.schedule(service::shutdown, phaseLen*phaseCount*MICRO_CONST, TimeUnit.MICROSECONDS);
    }
}
