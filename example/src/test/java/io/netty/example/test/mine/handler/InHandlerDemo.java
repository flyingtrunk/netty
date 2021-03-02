package io.netty.example.test.mine.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author liuhaoxing
 * @date 2021-03-02
 **/
public class InHandlerDemo extends ChannelInboundHandlerAdapter {

    /**
     * 当业务处理器被加到流水线后，此方法回调
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("被调用：handlerAdded()");
        super.handlerAdded(ctx);
    }

    /**
     * 当通道成功绑定一个NioEventLoop线程后，会通过流水线回调所有业务处理器的channelRegistered
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("被调用：channelRegistered");
        super.channelRegistered(ctx);
    }

    /**
     * 当通道激活成功后，会通过流水线回调所有业务处理器的channelActive。通道激活指的是所有业务处理器添加，注册的异步任务完成，
     * 并且NioEventLoo[线程绑定的异步任务完成
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("被调用：channelActive()");
        super.channelActive(ctx);
    }

    /**
     * 有数据包入站，通道可读
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("被调用：channelRead()");
        super.channelRead(ctx, msg);
    }

    /**
     * 流水线完成入站处理后，y8ici回调每个入站处理器的channelReadComplete
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("被调用：channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    /**
     * 当通道的底层连接已经不是establish状态，或者底层连接已经关闭时，会首先回调所有业务处理器的channelInactive方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("被调用：channelInactive()");
        super.channelInactive(ctx);
    }

    /**
     * 通道和NioEventLoop线程解除绑定，移除掉对这条通道的事件处理后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("被调用：channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    /**
     * Netty会移除掉通道上所有业务处理器，回调该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("被调用：handlerRemoved()");
        super.handlerRemoved(ctx);
    }
}
