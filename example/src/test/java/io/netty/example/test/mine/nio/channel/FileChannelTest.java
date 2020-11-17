package io.netty.example.test.mine.nio.channel;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author liuhaoxing
 * @date 2020-11-15
 **/
public class FileChannelTest {


    @Test
    public void test4Write() throws Exception {
        String str = "你好";
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/rishi/Desktop/study/sources/netty/example/src/test/java/io/netty/example/test/mine/nio/channel/zz");
        // 获取Channel
        FileChannel channel = fileOutputStream.getChannel();
        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将数据放入buffer中
        byteBuffer.put(str.getBytes());
        // 写模式切换成读模式
        byteBuffer.flip();
        // 将数据写入FileChannel中
        channel.write(byteBuffer);
        fileOutputStream.close();
    }

    @Test
    public void test4Read() throws Exception {
        File file = new File("/Users/rishi/Desktop/study/sources/netty/example/src/test/java/io/netty/example/test/mine/nio/channel/zz");
        FileInputStream inputStream = new FileInputStream(file);
        // 利用inputStream获取FileChannel
        FileChannel channel = inputStream.getChannel();
        // 创建缓冲区
        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());
        // 将通道中的数据读取到缓冲区里面
        channel.read(allocate);
        System.out.println(new String(allocate.array()));
        inputStream.close();
    }

    /**
     * 使用一个Buffer完成文件的读写
     * 实现文件的拷贝
     */
    @Test
    public void test4ReadAndWrite() throws Exception {
        // 文件输入
        File file = new File("/Users/rishi/Desktop/study/sources/netty/example/src/test/java/io/netty/example/test/mine/nio/channel/zz");
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel channel = inputStream.getChannel();

        // 文件输出
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/rishi/Desktop/study/sources/netty/example/src/test/java/io/netty/example/test/mine/nio/channel/xx");
        FileChannel channel1 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {
            byteBuffer.clear();
            // 从Channel读取到Buffer中
            int read = channel.read(byteBuffer);
            System.out.println("read:" + read);
            if (read == -1) {
                break;
            }
            // 将Buffer中的数据写入到Channel1中
            byteBuffer.flip();
            channel1.write(byteBuffer);
        }

        inputStream.close();
        fileOutputStream.close();

    }

    /**
     * 使用transFrom实现文件拷贝
     */
    @Test
    public void test4TransferFrom() {



    }


}
