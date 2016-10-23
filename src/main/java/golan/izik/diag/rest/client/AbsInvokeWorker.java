package golan.izik.diag.rest.client;

import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    24/10/2015 23:06
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public abstract class AbsInvokeWorker implements Runnable {
  protected static int counter;

  protected final String[]      urls;
  protected final int           index;
  private   final Invoker       invoker;
  private   final WeakHashMap<String, String> allData;

  public AbsInvokeWorker(String[] urls, Invoker invoker, WeakHashMap<String, String> allData) {
    this.urls = urls;
    this.invoker = invoker;
    this.allData = allData;
    this.index = ++counter;
  }

  static ThreadPoolExecutor createExecutor(int size) {
      return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new MyThreadFactory(), new MyRejectedExecutionHandler());
  }

  protected long invoke2all() {
    final long before   = System.currentTimeMillis();
    long totalData = 0;
    for (String url : urls) {
      final String data = this.invoker.invoke(url);
      if (allData!=null) allData.put(this.invoker.getClass().getSimpleName()+"_"+url,data);
      totalData += data.length();
    }
    final long after   = System.currentTimeMillis();
    System.out.println(this.getClass().getSimpleName()+"_"+getInvoker().getClass().getSimpleName()+": [" + index + "] totalData=["+totalData+"] latency=["+(after-before)+"] urls=["+ Arrays.toString(urls) +"]");
    return totalData;
  }

  public Invoker getInvoker() {
    return invoker;
  }

  public int getIndex() {
    return index;
  }

  public static class MyThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber   = new AtomicInteger(1);
    private        final ThreadGroup   group;
    private        final AtomicInteger threadNumber = new AtomicInteger(1);
    private        final String        namePrefix;

    MyThreadFactory() {
      SecurityManager s = System.getSecurityManager();
      group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
      namePrefix = "izikWorker-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
      Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
      if (t.isDaemon()) t.setDaemon(false);
      if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
      return t;
    }
  }

  static class MyRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      if (r instanceof AbsInvokeWorker) {
        final int index = ((AbsInvokeWorker) r).getIndex();
        System.out.println("rejectedExecution taskIndex=["+index+"]");
      }
    }
  }
}
