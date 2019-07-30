package co.shm.mytomcatv2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 所有服务端Java小程序要实现的接口
 */
public interface Servlet {
    //初始化
    void init();
    //服务
    void service(InputStream is, OutputStream ops) throws IOException;
    //销毁
    void destroy();
}
