package co.shm.mytomcatv2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BBServlet implements Servlet {
    @Override
    public void init() {
        System.out.println("BBServlet ... init");
    }

    @Override
    public void service(InputStream is, OutputStream ops) throws IOException {
        System.out.println("BBServlet ... service");
        ops.write("I am from BBServlet".getBytes());
        ops.flush();
    }

    @Override
    public void destroy() {
        System.out.println("BBServlet ... destroy");
    }
}
