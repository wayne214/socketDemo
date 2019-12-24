package com.wayne;

import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        // 1.创建一个服务端的socket, 即serversocke,绑定指定端口，并监听此端口
        ServerSocket serverSocket = new ServerSocket(12345);
        InetAddress localHost = Inet4Address.getLocalHost();
        String ip = localHost.getHostAddress();
        Socket socket = null;
        System.out.println("~~服务端已就绪，等待客户端接入~~~, 服务端ip地址：" + ip);
        // 2.调用accept()等待客户端连接
        socket = serverSocket.accept();
        // 3.连接后获取输入流，读取客户端信息
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;

        is = socket.getInputStream(); // 获取输入流
        isr = new InputStreamReader(is, "UTF-8");
        br = new BufferedReader(isr);
        String info = null;
        // 循环读取客户端数据
        while ((info = br.readLine()) != null) {
            System.out.println("客户端发送过来的消息：" + info);
        }
        // 关闭输入流
        socket.shutdownInput();
        socket.close();
    }
}
