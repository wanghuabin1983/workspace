import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTest {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// 创建一个固定大小的线程池
		ExecutorService service = Executors.newFixedThreadPool(1);
		for (int i = 0; i < 3; i++) {
			System.out.println("创建线程" + i);
			Runnable run = new Runnable() {
				public void run() {
					Date d = new Date();
					System.out
							.println("启动线程" + d.getMinutes() + d.getSeconds());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			// 在未来某个时间执行给定的命令
			service.execute(run);
		}
		// 关闭启动线程
		service.shutdown();
		// 等待子线程结束，再继续执行下面的代码
		service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		System.out.println("all thread complete");

		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			try {
				Thread.sleep(index * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(index);
				}
			});
		}
	}
}