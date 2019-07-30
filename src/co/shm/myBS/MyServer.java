package co.shm.myBS;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        OutputStream ops = null;
        try {
            //1.创建serverSocket对象，监听本机的8080端口
            serverSocket = new ServerSocket(8080);
            while (true){
                //2.等待来自客户端的请求，获取和客户端对应的socket对象
                socket = serverSocket.accept();
                //3.通过获取到的socket对象获取输出流对象
                ops = socket.getOutputStream();
                //4.通过获取到的输出流对象将HTTP的响应部分发送到客户端
                ops.write("HTTP/1.1 200 OK\n".getBytes());
                ops.write("Content-Type:text/html;charset:utf-8\n".getBytes());
                ops.write("Server:Apache-Coyote/1.1\n".getBytes());
                ops.write("\n\n".getBytes());
                StringBuffer buff = new StringBuffer();
                buff.append("<html>");
                buff.append("<head><title>标题</title></head>");
                buff.append("<body>");
                buff.append("<h1>I am head1</h1>");
                buff.append("<a href='http://www.baidu.com'>百度</a>");
                buff.append("</body>");
                buff.append("</html>");
                ops.write(buff.toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //5.释放资源
            if (ops != null){
                ops.close();
            }
            if (socket != null){
                socket.close();
            }
        }
    }
}
