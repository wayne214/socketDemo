package com.wayne;
import java.io.*;
import	java.security.Provider.Service;
import	java.util.concurrent.Executors;
import java.net.Socket;
import java.util.ArrayList;
import	java.util.List;

import java.net.ServerSocket;
import java.util.SortedMap;
import java.util.concurrent.ExecutorService;

public class Server {
    //定义相关的参数,端口,存储Socket连接的集合,ServerSocket对象
    //以及线程池
    private static final int PORT = 12345;
    private ServerSocket serverSocket = null;
    private List<Socket> mList = new ArrayList<Socket>();
    private ExecutorService myExecutorService = null;
    public static void main(String[] args){
        new Server();
    }

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            // 创建线程池
            myExecutorService = Executors.newCachedThreadPool();
            System.out.println("服务端运行中。。。\n");
            Socket client = null;
            while (true){
                client = serverSocket.accept();
                mList.add(client);
                // 线程池执行内部线程
                myExecutorService.execute(new Service(client));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;
        private String msg = "";
        public Service(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                msg = "用户：" + this.socket.getInetAddress() + "~加入了聊天室"
                        + "当前在线人数：" +mList.size();
                this.sendMsg();
            } catch (IOException e){
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                while (true){
                    if((msg = in.readLine()) != null) {
                        if(msg.equals("bye")){
                            System.out.println("~~~~~~~~~~");
                            mList.remove(socket);
                            in.close();
                            msg = "用户：" + socket.getInetAddress() + "退出：" + "当前在线人数：" + mList.size();
                            socket.close();
                            this.sendMsg();
                            break;
                        }else {
                            msg = socket.getInetAddress() + "   说：" + msg;
                            this.sendMsg();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //为连接上服务端的每个客户端发送信息
        public void sendMsg(){
            System.out.println(msg);
            int num = mList.size();
            for (int i = 0; i < num; i++) {
                Socket mSocket = mList.get(i);
                PrintWriter pout = null;
                try {
                    pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "UTF-8")),true);
                    pout.println(msg);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

}
