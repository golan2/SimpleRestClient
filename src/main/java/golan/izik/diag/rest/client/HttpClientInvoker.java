package golan.izik.diag.rest.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    25/10/2015 00:21
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class HttpClientInvoker implements Invoker {
  @Override
  public String invoke(String url) {
    try {
      final DefaultHttpClient client = new DefaultHttpClient();
      HttpGet httpGet = new HttpGet(url);
      httpGet.addHeader("User-Agent", "Mozilla/5.0");
      final HttpResponse response = client.execute(httpGet);
      final HttpEntity entity = response.getEntity();
      return org.apache.http.util.EntityUtils.toString(entity);
    } catch (Exception e) {
      System.out.println("Error! URL=[" + url + "] Message=[" + e.getMessage() + "]");
      return e.getMessage();
    }

  }


}
