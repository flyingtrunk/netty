package io.netty.example.test.mine.nio.selector;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO Server Client 测试用例
 * @author liuhaoxing
 * @date 2020-11-15
 **/
public class ServerTest {

    @Test
    public void test4Server() throws Exception {
        // 1. 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2. 获取Selector对象
        Selector selector = Selector.open();
        // 3. 绑定端口，在服务端监听
        serverSocketChannel.bind(new InetSocketAddress(6666));
        // 4. 设置为非阻塞的
        serverSocketChannel.configureBlocking(false);
        // 5. 把serverSocketChannel注册到selector上，其感兴趣的事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 6. 循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1s， 无连接");
                continue;
            }
            // 服务器接收到连接
            // 7. 返回关心事件的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                // 8. 获取到SelectKey
                SelectionKey selectionKey = iterator.next();
                // 如果是OP_ACCEPT
                if (selectionKey.isAcceptable()) {
                    // 有新的客户端连接，为客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成一个SocketChannel:" + socketChannel.hashCode());

                    // 将SocketChannel注册到selector中，关注事件为OP_READ,同时给SocketChannel关联一个Buffer
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }


                if (selectionKey.isReadable()) {
                    // 通过key反向获取到Channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 获取到该Channel关联的Buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    // 从Channel中读取数据到Buffer
                    socketChannel.read(byteBuffer);
                    System.out.println("from 客户端:" + new String(byteBuffer.array()));
                }

                // 手动从集合中移除当前的selectKey,防止重复操作
                iterator.remove();

            }
        }
    }


}
