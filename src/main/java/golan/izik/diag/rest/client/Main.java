package golan.izik.diag.rest.client;

import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;

public class Main {

    private static final int  ARGS_SLEEP_TIME    = 0;
    private static final int  ARGS_TOTAL_SIZE    = 1;
    private static final int  DEFAULT_SLEEP_TIME = 1500;
    private static final long DEFAULT_TOTAL_SIZE = (long) (150 * 8 * 1024 * 1024); //150 MB in bytes
    public static final String READ_FILE_NAME = "C:\\SVN\\DIAG\\9.26_IP\\mediator\\build\\log\\server.log";
    public static final int NUMBER_OF_TASKS = 2;

    private static int  sleepTime = 0;
    private static long totalBufferSize = 0;



    public  static final String FQDN = "localhost";//"myd-vm03329.hpswlabs.adapps.hp.com";
    public static final int TOMCAT_PORT = 7080;

    private static String[] URLS = {
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/websocket/snake.xhtml",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/jsp/jsp2/tagfiles/products.jsp",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/jsp/jsp2/jspattribute/shuffle.jsp",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/jsp/sessions/carts.jsp?item=C&submit=add",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/jsp/cal/cal1.jsp?name=Izik&email=golan2%40hotmail.com&action=Submit",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/servlets/servlet/HelloWorldExample",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/servlets/servlet/RequestInfoExample",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/servlets/servlet/RequestHeaderExample",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/servlets/servlet/CookieExample",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/async/async0",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/async/async1",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/async/async2",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/async/async3",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/async/stockticker",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/jsp/xVM_1.jsp",
        "http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/jsp/ping_pong.jsp?depth=1",
        "http://"+FQDN+ ":" + "8080" + "/examples/jsp/ping_pong.jsp?depth=1",
        //"http://"+FQDN+ ":" + TOMCAT_PORT + "/examples/jsp/izik.jsp?depth=25",
    };


    public static void main(String[] args) throws Exception {
        System.out.println("Starting...");
        System.out.println("args="+ Arrays.toString(args));
        sleepTime = getSleepTimeParam(args);
        System.out.println("Sleep Time: " + sleepTime);

        //final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_TASKS, new EndlessRestInvoker.MyThreadFactory());
        //for (int i = 0; i < NUMBER_OF_TASKS; i++) {
        //    executorService.submit(new EndlessRestInvoker(URLS, sleepTime));
        //}


        final ExecutorService executorService = AbsInvokeWorker.createExecutor(NUMBER_OF_TASKS);

        WeakHashMap<String, String> allData = new WeakHashMap<String, String>();

        int i=1;
        while (true) {
            for (String url : URLS) {
                i++;
                if (i>=Integer.MAX_VALUE-1) i=1;

                executorService.submit(new OneTimeInvokeWorker(new String[]{url+"&additional="+i}, new HttpClientInvoker(), allData));
                executorService.submit(new OneTimeInvokeWorker(new String[]{url+"&additional="+i}, new RestInvoker(), allData));

                System.out.println("*** hashCode=["+System.identityHashCode(allData)+"] size=["+allData.keySet().size()+"]");
            }
            Thread.sleep(sleepTime);
        }

        //readFile();
        //sendRestim(cont, i, buffers);
        //System.out.println("DONE!");
    }

    private static int getSleepTimeParam(String[] args) {
        try {
            return  Integer.parseInt(args[ARGS_SLEEP_TIME]);
        } catch (Exception ignored) {
            return DEFAULT_SLEEP_TIME;
        }
    }

    private static long getTotalSizeParam(String[] args) {
        try {
            return  Integer.parseInt(args[ARGS_TOTAL_SIZE]);
        } catch (Exception ignored) {
            return DEFAULT_TOTAL_SIZE;
        }
    }

    //private static void sendRestim(boolean cont, int i, List<StringBuilder> buffers) {
    //    while (cont) {
    //
    //        try {
    //            System.out.print("[" + i++ + "] ==> ");
    //            final StringBuilder buf = new StringBuilder();
    //            for (int j = 0; j < URLS.length; j++) {
    //                String response = sendRest(URLS[j]);
    //                if (response!=null) {
    //                    buf.append(Arrays.toString(response.getBytes()));
    //                    buf.append(Arrays.toString(Thread.currentThread().getStackTrace()));
    //                    System.out.print("[" + j + "," + response.getBytes().length + "] , ");
    //                }
    //                else {
    //                    System.out.print("[" + j + "," + -1 + "] , ");
    //                }
    //                Thread.sleep(sleepTime);
    //            }
    //            buffers.add(buf);
    //            Thread.sleep(sleepTime);
    //            System.out.println();
    //
    //
    //            long buffersSize = 0;
    //            for (StringBuilder buffer : buffers) {
    //                buffersSize+=buffer.length();
    //            }
    //            if (buffersSize>= totalBufferSize) {
    //                System.out.println("Total size of buffer: " + buffersSize);
    //                buffers = new ArrayList<StringBuilder>();
    //            }
    //
    //        } catch (Exception e) {
    //            System.out.println("ERR " + e.getMessage());
    //            e.printStackTrace();
    //            cont=false;
    //        }
    //    }
    //}
    //
    //private static void readFile() throws IOException, InterruptedException {
    //    final FileInputStream inputStream = new FileInputStream(new File(READ_FILE_NAME));
    //    while (true) {
    //        byte[] buf = new byte[200];
    //        final int read = inputStream.read(buf);
    //        if (read >0) {
    //            System.out.print(new String(buf));
    //        }
    //        sleep();
    //    }
    //}

    //private static void sleep() throws InterruptedException {Thread.sleep(sleepTime);}

    //private static String sendRest(String uri) {
    //    ClientConfig clientConfig = new ClientConfig();
    //    try {
    //        RestClient restClient = new RestClient(clientConfig);
    //        Resource resource = restClient.resource(uri);
    //        return resource.accept(MediaType.APPLICATION_JSON).get(String.class);
    //    } catch (Exception ignored) {
    //        return null;
    //    }
    //}
    //
    //private static class MyFileInputStream extends FileInputStream {
    //
    //    public MyFileInputStream(File file) throws FileNotFoundException {
    //        super(file);
    //    }
    //
    //    private int myRead(byte b[]) throws IOException {
    //        b[0] = 'c';
    //        return 1;
    //    }
    //
    //
    //}
}
