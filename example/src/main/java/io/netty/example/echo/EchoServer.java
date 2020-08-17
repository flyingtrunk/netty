/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Echoes back any received data from a client.
 */
public final class EchoServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        // 使用reactor主从多线程模式

        // parent reactor
        // boss group 只处理连接请求，真正的客户端业务处理，会交给work group处理
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        // child group
        // 含有子线程NioEventLoop的个数默认是cpu核数 * 2
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final EchoServerHandler serverHandler = new EchoServerHandler();
        try {
            // 引导类
            ServerBootstrap b = new ServerBootstrap();
            // 设置主线程和子线程
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 定义服务为NIO模型
                    .option(ChannelOption.SO_BACKLOG, 100) // 设置线程队列等待的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动的连接状态
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 给workgroup的eventloop对应的管道设置处理器
                    .childHandler(
                            // 创建一个通道测试对象
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline p = ch.pipeline();
                                    if (sslCtx != null) {
                                        p.addLast(sslCtx.newHandler(ch.alloc()));
                                    }
                                    //p.addLast(new LoggingHandler(LogLevel.INFO));
                                    p.addLast(serverHandler);
                                }
                            });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
