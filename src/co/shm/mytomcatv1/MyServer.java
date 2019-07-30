package co.shm.mytomcatv1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * tomcat版本1
 * 服务端的准备工作_框架代码的搭建
 * 解析请求部分资源名称
 * 实现静态资源的发送
 */
public class MyServer {
    //定义一个变量，存放WebContent目录的绝对路径
    public static String WEB_ROOT = System.getProperty("user.dir")+"\\"+"WebContent";
    //定义静态变量，存储本次请求的静态页面名称
    private static String url = "";


    public static void main(String[] args) throws IOException {
//        System.out.println(WEB_ROOT);
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream is = null;
        OutputStream ops = null;
        try {
            //1.创建serverSocket对象，监听本机的8080端口,等待来自客户端的请求
            serverSocket = new ServerSocket(8080);
            while (true){
                //获取客户端对象的socket
                socket = serverSocket.accept();
                //获取输入流对象
                is = socket.getInputStream();
                //获取输出流对象
                ops = socket.getOutputStream();
                //获取HTTP协议的请求部分，截取客户端要访问的资源名称，赋值给url
                parse(is);
                //发送静态资源
                sendStaticResource(ops);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                is.close();
                is = null;
            }
            if (ops != null){
                ops.close();
                ops = null;
            }
            if (socket != null){
                socket.close();
                socket = null;
            }
        }
    }

    //获取HTTP协议的请求部分，截取客户端要访问的资源名称
    private static void parse(InputStream is) throws IOException {
        //定义一个变量，存放HTTP协议请求部分的数据
        StringBuffer content = new StringBuffer(2048);
        //定义一个数组，存放HTTP协议请求部分数据
        byte[] buff = new byte[2048];
        //定义一个变量i,代表读取数据到数组之后数据量的大小
        int i = -1;
        //读取客户端发送过来的数据，将数据读到字符数组buff中
        i = is.read(buff);
        //遍历字符数组，将数据追加到content变量中
        for (int j = 0; j < i; j++) {
            content.append((char)buff[j]);
        }
        //打印HTTP协议请求部分
        System.out.println(content);
        //截取客户端要请求的资源路径，赋值给url
        parseUrl(content.toString());
    }

    //截取客户端要请求的资源路径，赋值给url
    private static void parseUrl(String content) {
        //定义两个变量，存放请求行两个空格的位置
        int index1,index2;
        //获取请求行第一个空格的位置
        index1  = content.indexOf(" ");
        if (index1 != -1){
            //获取请求行第二个空格的位置
            index2 = content.indexOf(" ", index1 + 1);
            if (index2 > index1){
                //截取字符串取本次请求的资源名称
                url = content.substring(index1+2,index2);
            }
        }
        //打印本次请求资源名称
        System.out.println(url);

    }


    //发送静态资源
    private static void sendStaticResource(OutputStream ops) throws IOException {
        //定义一个字节数组，用于存放本次请求的静态资源文件的内容
        byte[] bytes = new byte[2048];
        //定义一个文件输入流，用于获取请求资源文件的内容
        FileInputStream fis = null;
        try {
            //创建文件对象file，代表本次要请求的资源文件
            File file = new File(WEB_ROOT, url);
            //如果文件存在
            if (file.exists()){
                //向客户端输出HTTP协议的响应行和响应头
                ops.write("HTTP/1.1 200 OK\n".getBytes());
                ops.write("Content-Type:text/html;charset:utf-8\n".getBytes());
                ops.write("Server:Apache-Coyote/1.1\n".getBytes());
                ops.write("\n".getBytes());
                //读取到文件输入流对象
                fis = new FileInputStream(file);
                //读取资源文件的内容到数组
                int ch = fis.read(bytes);
                while (ch != -1){
                    //将数组中的内容通过输出流发送到客户端
                    ops.write(bytes,0,ch);
                    ch = fis.read(bytes);
                }
            }else {
                //如果文件不存在
                //向客户端响应文件不存在消息
                ops.write("HTTP/1.1 404  not found\n".getBytes());
                ops.write("Content-Type:text/html;charset:utf-8\n".getBytes());
                ops.write("Server:Apache-Coyote/1.1\n".getBytes());
                ops.write("\n".getBytes());
                String errorMessage = "file not found";
                ops.write(errorMessage.getBytes());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fis != null){
                fis.close();
                fis = null;
            }
        }
    }
}
