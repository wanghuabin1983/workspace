import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTest {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// ����һ���̶���С���̳߳�
		ExecutorService service = Executors.newFixedThreadPool(1);
		for (int i = 0; i < 3; i++) {
			System.out.println("�����߳�" + i);
			Runnable run = new Runnable() {
				public void run() {
					Date d = new Date();
					System.out
							.println("�����߳�" + d.getMinutes() + d.getSeconds());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			// ��δ��ĳ��ʱ��ִ�и���������
			service.execute(run);
		}
		// �ر������߳�
		service.shutdown();
		// �ȴ����߳̽������ټ���ִ������Ĵ���
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