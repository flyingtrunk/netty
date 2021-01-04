package io.netty.example.test.mine.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuhaoxing
 * @date 2020-12-31
 **/
public class MultiThreadEchoServerReactorTest {


}


class MultiThreadEchoServerReactor {

    ServerSocketChannel serverSocketChannel;
    AtomicInteger next = new AtomicInteger(0);
    // 选择器集合，引入多个选择器
    Selector[] selectors = new Selector[2];
    // 引入多个子反应器
    SubReactor[] subReactors;

    MultiThreadEchoServerReactor() throws IOException {
        // 初始化多个选择器
        selectors[0] = Selector.open();
        selectors[1] = Selector.open();
        // 初始化serverSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8007);
        serverSocketChannel.socket().bind(address);
        // 设置其为非阻塞的
        serverSocketChannel.configureBlocking(false);

        // 第一个选择器，负责监听新连接事件
        SelectionKey sk = serverSocketChannel.register(selectors[0], SelectionKey.OP_ACCEPT);
        // 绑定handler
        sk.attach(new AcceptorHandler());

        // 一个子反应器负责一个选择器
        SubReactor subReactor1 = new SubReactor(selectors[0]);
        SubReactor subReactor2 = new SubReactor(selectors[1]);
        subReactors = new SubReactor[]{subReactor1, subReactor2};

    }

    private void startService() {
        new Thread(subReactors[0]).start();
        new Thread(subReactors[1]).start();
    }

    // 子反应器
    class SubReactor implements Runnable {
        // 每个线程负责一个选择器的查询和选择
        final Selector selector;

        SubReactor(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        // 反应器负责dispatch收到的事件
                        SelectionKey sk = iterator.next();
                        dispatch(sk);
                    }
                    selectionKeys.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void dispatch(SelectionKey sk) {
            Runnable handler = (Runnable) sk.attachment();
            // 调用之前绑定到选择键的handler处理器对象
        }
    }

    class AcceptorHandler implements Runnable {

        @Override
        public void run() {
            // TODO: 2020/12/31
        }
    }

}