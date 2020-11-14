package study.my.flyingtrunk.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: flyingtrunk
 * @date: 2020/11/12
 */
public class BioServer {

    public static void main(String[] args) throws Exception {
        // 1.创建一个线程池，如果有客户端建立连接，就创建一个线程与之通信
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务端启动了");
        while (true) {
            System.out.println("线程信息 id=" + Thread.currentThread().getId() + "name:" + Thread.currentThread().getName());
            System.out.println("等待连接...");
            Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    private static void handler(Socket socket) {
        try {
            System.out.println("handler 线程 id:" + Thread.currentThread().getId() + "name:" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            // 通过socket获取到输入流
            InputStream inputStream = socket.getInputStream();

            // 循环读取客户端发送的数据
            while (true) {
                System.out.println("reading...");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
