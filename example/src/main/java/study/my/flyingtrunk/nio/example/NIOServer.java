package study.my.flyingtrunk.nio.example;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: liuhaoxing
 * @Date: 2020/7/8 23:18
 */
public class NIOServer {

    public static void main(String[] args) throws Exception {

        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 获取Selector对象
        Selector selector = Selector.open();

        // 绑定端口666，在服务器监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 把serverSocketChannel注册到Selector关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {

            // 等待1s,没有事件发生则返回
            if (selector.select(2000) == 0) {
                System.out.println("服务器等待了2s,无连接");
                continue;
            }

            // 如果返回的大于0, 获取selectionKeys
            // 已经获取到关注的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                // 获取key
                SelectionKey key = iterator.next();
                // 根据key对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) {
                    // 有OP_ACCEPT事件发生
                    // 给该客户端生成SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，成功了一个socketChannel：" + socketChannel.hashCode());

                    // 将sokcetChannel设置为非阻塞的
                    socketChannel.configureBlocking(false);

                    // 将socketChannel注册到Selector，关注事件为OP_READ,同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(100));
                }

                if (key.isReadable()) {
                    // 读事件
                    // 通过key反向获取Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的Buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端：" + new String(buffer.array()));
                }
                iterator.remove();
            }


        }

    }

}
