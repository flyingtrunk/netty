package study.my.flyingtrunk.nio.simple;

import java.nio.ByteBuffer;

/**
 *
 *  // Invariants: mark <= position <= limit <= capacity
 *     private int mark = -1;
 *     private int position = 0; 当前正在操作的位置
 *     private int limit; 表示缓冲区中可以操作数据的大小（limit之后的数据不能进行读写）
 *     private int capacity; 表示缓存区中最大村塾数据的容量
 *
 * @author: flyingtrunk
 * @date: 2020/10/26
 */
public class BufferTest {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        System.out.println("position:" + byteBuffer.position());
        System.out.println("limit:" + byteBuffer.limit());
       // System.out.println(byteBuffer.mark());
        System.out.println("capacity:" + byteBuffer.capacity());


        System.out.println("----");

        String str = "abcd";
        byteBuffer.put(str.getBytes());
        System.out.println("position:" + byteBuffer.position());
        System.out.println("limit:" + byteBuffer.limit());
        // System.out.println(byteBuffer.mark());
        System.out.println("capacity:" + byteBuffer.capacity());

        // 切换成读取数据的模式
        byteBuffer.flip();
        System.out.println("--flip 切换读写模式--");
        System.out.println("position:" + byteBuffer.position());
        System.out.println("limit:" + byteBuffer.limit());
        // System.out.println(byteBuffer.mark());
        System.out.println("capacity:" + byteBuffer.capacity());

        System.out.println("--get--");
        // 利用get数据来读取缓冲区中的数据
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes, 0, bytes.length));

        System.out.println("position:" + byteBuffer.position());
        System.out.println("limit:" + byteBuffer.limit());
        // System.out.println(byteBuffer.mark());
        System.out.println("capacity:" + byteBuffer.capacity());

        // 可重复读数据
        System.out.println("--rewind可重复读数据--");
        byteBuffer.rewind();
        byte[] bytes2 = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes2);
        System.out.println(new String(bytes2, 0, bytes2.length));

        System.out.println("position:" + byteBuffer.position());
        System.out.println("limit:" + byteBuffer.limit());
        // System.out.println(byteBuffer.mark());
        System.out.println("capacity:" + byteBuffer.capacity());

        System.out.println("--clear 清空缓冲区，实际上不被遗忘状态，还有数据存在，只是指针直到初始位置--");
        byteBuffer.clear();
        System.out.println("position:" + byteBuffer.position());
        System.out.println("limit:" + byteBuffer.limit());
        // System.out.println(byteBuffer.mark());
        System.out.println("capacity:" + byteBuffer.capacity());

        System.out.println("----");
        byteBuffer.mark();
        System.out.println("---");
        //byteBuffer.hasRemaining()
    }


}
