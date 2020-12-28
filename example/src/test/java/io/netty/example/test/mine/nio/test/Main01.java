package io.netty.example.test.mine.nio.test;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liuhaoxing
 * @date 2020-12-28
 **/
public class Main01 {

    public static void main(String[] args) throws Exception {
        // 1. 获取selector实例
        Selector selector = Selector.open();
        // 2. 将通道注册到选择器实例
        // 2.1 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2.2 设置为非阻塞的
        serverSocketChannel.configureBlocking(false);
        // 2.3 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8007));
        // 2.4 将通道注册到选择器上，并制定监听事件为"接收连接"事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //3. 选出感兴趣的IO就绪事件
        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 根据IO事件类型，执行对应的业务操作
                if (selectionKey.isAcceptable()) {
                    // IO事件：ServerSocketChannel服务器监听通道有新连接
                } else if (selectionKey.isConnectable()) {
                    // IO事件：传输通道连接成功
                } else if (selectionKey.isReadable()) {
                    // IO事件：传输通道可读
                } else if (selectionKey.isWritable()) {
                    // IO事件：传输通道可写
                }

                // 处理完成，移除选择键
                iterator.remove();
            }
        }

    }

}
