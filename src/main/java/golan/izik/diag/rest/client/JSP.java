package golan.izik.diag.rest.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    20/10/2015 15:53
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class JSP {

    private static void jsp() {
        met("http://localhost:7080/trust-me-bank-ui/");
        met("http://localhost:7080/trust-me-bank-ui/");
    }

    private static String met(String url) {
        ClientConfig clientConfig = new ClientConfig();
        RestClient restClient = new RestClient(clientConfig);
        Resource resource = restClient.resource(url);
        final long before = System.currentTimeMillis();
        final String data = resource.accept("application/json"/*MediaType.APPLICATION_JSON*/).get(String.class);
        final long after = System.currentTimeMillis();
        final long latency = after-before;

        return "length=["+data.length()+" bytes] latency=["+latency+"ms] ";
    }


    // HTTP GET request
    private String sendGet(String url) throws Exception {

        final DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", "Mozilla/5.0");

        final long         before   = System.currentTimeMillis();
        final HttpResponse response = client.execute(request);
        final long         after    = System.currentTimeMillis();

        final long latency       = after - before;
        final long contentLength = response.getEntity().getContentLength();
        final int  statusCode    = response.getStatusLine().getStatusCode();

        return "statusCode=["+statusCode+"] length=["+contentLength+" bytes] latency=["+latency+"ms] ";
    }

    private String sendGet(String url, boolean withContent) throws Exception {

        final DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/5.0");

        final long         before   = System.currentTimeMillis();
        final HttpResponse response = client.execute(request);
        final long         after    = System.currentTimeMillis();

        final long latency       = after - before;
        final long contentLength = response.getEntity().getContentLength();
        final int  statusCode    = response.getStatusLine().getStatusCode();

        StringBuffer buf = new StringBuffer();

        buf.append("statusCode=[").append(statusCode).append("] length=[").append(contentLength).append(" bytes] latency=[").append(latency).append("ms]");

        if (withContent) {
            buf.append("<br/><br/><hl><br/>");
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                buf.append(line);
            }
        }

        return buf.toString();

    }


    public static void main(String[] args) {
        jsp();
    }

}
