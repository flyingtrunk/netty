package io.netty.example.test.mine.nio.test;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liuhaoxing
 * @date 2020-12-28
 **/
public class NioDiscardServer {

    public static void main(String[] args) throws IOException {
        startServer();
    }

    public static void startServer() throws IOException {
        // 1. 获取selector实例
        Selector selector = Selector.open();
        // 2. 创建服务端监听通道，将其绑定到selector上
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8007));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    // 获取客户端连接SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 这是其属性并将其注册到selector上
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 读取数据，然后丢弃
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len;
                    while ((len = socketChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        System.out.println("内容" + new String(byteBuffer.array(), 0, len));
                        byteBuffer.clear();
                    }
                    // 关闭客户端连接
                    socketChannel.close();
                }
                iterator.remove();
            }

        }
        // 关闭服务端连接
        serverSocketChannel.close();
    }

}
