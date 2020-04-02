import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.DAYS;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/11
 */
public class MyTest {
    int i;
    @Test
    public void fun() throws InterruptedException, ExecutionException, TimeoutException {
        //创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(10);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        int m = 0;
        for (i = 0; i < 10; i++) {
            Thread.sleep(500);
            Callable c = new Callable() {
                @Override
                public Object call() throws Exception {
                    return i;
                }
            };
// 执行任务并获取 Future 对象
            Future f = pool.submit(c);
            list.add(f);
        }
// 关闭线程池
        pool.shutdown();
// 获取所有并发任务的运行结果
        for (Future f : list) {
// 从 Future 对象上获取任务的返回值，并输出到控制台
            System.out.println("res：" + f.get(100L, DAYS ).toString());
        }
    }

    @Test
    public void fusadsn() throws Exception{
        final CountDownLatch latch = new CountDownLatch(2);
        new Thread(){public void run() {
            System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
            latch.countDown();
        };}.start();
        new Thread(){ public void run() {
            System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
            latch.countDown();
        };}.start();
        System.out.println("等待 2 个子线程执行完毕...");
        latch.await();
        System.out.println("2 个子线程已经执行完毕");
        System.out.println("继续执行主线程");
    }
}
