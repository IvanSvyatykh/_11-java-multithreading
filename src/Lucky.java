import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Lucky {
    static int x = 0;
    static AtomicInteger count = new AtomicInteger();

    static final int threadNum = 3;

    static void run(ThreadPoolExecutor threadPoolExecutor, AtomicInteger progress) {
        while (x < 999999) {

            if (progress.get() < threadPoolExecutor.getMaximumPoolSize()) {
                operation(threadPoolExecutor, progress);
            }
        }
    }

    static void operation(ThreadPoolExecutor threadPoolExecutor, AtomicInteger progress) {

        progress.incrementAndGet();
        final int current = x++;

        threadPoolExecutor.submit(() -> {


            if ((current % 10) + (current / 10) % 10 + (current / 100) % 10 == (current / 1000)
                    % 10 + (current / 10000) % 10 + (current / 100000) % 10) {
                System.out.println(current);
                count.incrementAndGet();
            }
            progress.decrementAndGet();
        });

    }

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(threadNum, threadNum, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(threadNum));

        AtomicInteger progress = new AtomicInteger();

        run(threadPoolExecutor, progress);

        System.out.println("Total: " + count);
    }
}