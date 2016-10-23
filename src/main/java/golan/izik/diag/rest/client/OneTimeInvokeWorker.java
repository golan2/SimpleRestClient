package golan.izik.diag.rest.client;

import java.util.WeakHashMap;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    10/10/2015 19:57
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class OneTimeInvokeWorker extends AbsInvokeWorker {

  public OneTimeInvokeWorker(String[] urls, Invoker invoker) {
    this(urls, invoker, null);
  }

  public OneTimeInvokeWorker(String[] urls, Invoker invoker, WeakHashMap<String, String> allData) {
    super(urls, invoker, allData);
  }

  @Override
  public void run() {
    invoke2all();
  }
}
