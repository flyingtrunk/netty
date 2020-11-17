package io.netty.example.test.mine.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liuhaoxing
 * @date 2020-11-17
 **/
public class ReactorTest {

    class Reactor implements Runnable {

        final Selector selector;
        final ServerSocketChannel serverSocket;

        public Reactor(int port) throws IOException {
            this.selector = Selector.open();
            this.serverSocket = ServerSocketChannel.open();
            // 绑定端口
            this.serverSocket.socket().bind(new InetSocketAddress(port));
            // 设置为非阻塞的
            this.serverSocket.configureBlocking(false);
            // 将ServerSocketChannel注册到Selector上，并设置其对OP_ACCEPT事件感兴趣
            SelectionKey selectionKey = this.serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Object());
        }

        // dispatch loop
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        dispatch(iterator.next());
                    }
                    selectionKeys.clear();
                }
            } catch (IOException e) {

            }
        }


        void dispatch(SelectionKey key) {
            Runnable r = (Runnable) key.attachment();
            if (r != null) {
                r.run();
            }

        }


        class Acceptor implements Runnable {

            @Override
            public void run() {
                try {
                    SocketChannel socketChannel = serverSocket.accept();
                    if (socketChannel != null) {
                        new Handler(socketChannel, selector);
                    }
                } catch (IOException e) {

                }

            }
        }

    }

    final class Handler implements Runnable {
        final SocketChannel socket;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);
        static final int READING = 0, SENDING = 1;
        int state = READING;

        public Handler(SocketChannel socket, Selector selector) throws IOException {
            this.socket = socket;
            this.socket.configureBlocking(false);

            this.sk = this.socket.register(selector, 0);
            this.sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        @Override
        public void run() {
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (IOException e) {

            }
        }

        void read() throws IOException {
        }

        void send() throws IOException {
        }
    }

}
