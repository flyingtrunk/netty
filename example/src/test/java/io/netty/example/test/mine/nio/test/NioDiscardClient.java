package io.netty.example.test.mine.nio.test;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author liuhaoxing
 * @date 2020-12-28
 **/
public class NioDiscardClient {

    public static void main(String[] args) throws Exception {
        startClient();
    }

    public static void startClient() throws Exception {
        InetSocketAddress socketAddress = new InetSocketAddress(8007);
        // 1. 获取通道
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
        // 2. 设置其为非阻塞的
        socketChannel.configureBlocking(false);

        System.out.println("建立连接中...");
        // 3. 等待连接完成
        while (!socketChannel.finishConnect());

        System.out.println("客户端连接完成");

        // 4. 分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello nio".getBytes());
        byteBuffer.flip();

        // 发送到服务器
        socketChannel.write(byteBuffer);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }


}
