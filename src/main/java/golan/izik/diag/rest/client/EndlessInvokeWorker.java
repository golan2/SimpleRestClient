package golan.izik.diag.rest.client;

import java.util.WeakHashMap;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    09/10/2015 16:35
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class EndlessInvokeWorker extends AbsInvokeWorker {

  private final long     sleepTime;

  public EndlessInvokeWorker(String[] urls, Invoker invoker, long sleepTime) {
    this(urls, invoker, sleepTime, null);
  }

  public EndlessInvokeWorker(String[] urls, Invoker invoker, long sleepTime, WeakHashMap<String, String> allData) {
    super(urls, invoker, allData);
    this.sleepTime = sleepTime;
    System.out.println("RestInvoker Created index=["+index+"] sleepTime=["+sleepTime+"]");
  }

  @Override
  public void run() {
    long counter = 0;
    while (true) {
      try {
        System.out.print("["+index+","+ ++counter+"] ");
        invoke2all();
        Thread.sleep(sleepTime);

      }
      catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

}
