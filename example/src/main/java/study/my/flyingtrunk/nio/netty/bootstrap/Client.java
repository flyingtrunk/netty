package study.my.flyingtrunk.nio.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: flyingtrunk
 * @date: 2020/11/9
 */
public class Client {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 设置用于处理Channel所有事件的EventLoopGroup
                .group(new NioEventLoopGroup())
                // 指定要是用的Channel实现
                .channel(NioSocketChannel.class);
    }

}
