package io.netty.example.test.mine.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @author liuhaoxing
 * @date 2021-03-02
 **/
public class InHandlerDemoTester {

    @Test
    public void testInHandlerLifeCircle() {
        final InHandlerDemo inHandler = new InHandlerDemo();
        ChannelInitializer<EmbeddedChannel> channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(inHandler);
            }
        };
        // 创建嵌入式通道
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(channelInitializer);
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(1);
        // 模拟入站，写一个入站数据包
        embeddedChannel.writeInbound(buffer);
        embeddedChannel.flush();
        // 再写一个
        embeddedChannel.writeInbound(buffer);
        embeddedChannel.flush();

        // 通道关闭
        embeddedChannel.close();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
