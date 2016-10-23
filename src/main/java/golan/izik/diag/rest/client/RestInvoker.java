package golan.izik.diag.rest.client;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

import javax.ws.rs.core.MediaType;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    10/10/2015 19:54
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class RestInvoker implements Invoker {

  public String invoke(String url) {
    try {
      ClientConfig clientConfig = new ClientConfig();
      RestClient restClient = new RestClient(clientConfig);
      Resource resource = restClient.resource(url);
      return resource.accept(MediaType.APPLICATION_JSON).get(String.class);
    }
    catch (Exception e) {
      System.out.println("Error! URL=["+url+"] Message=["+e.getMessage()+"]");
      return e.getMessage();
    }
  }

}
