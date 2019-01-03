package com.chunchun.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Hu
 * @date 2019/1/2 13:41
 */

public class ClientDemo extends Thread{


    private ByteBuffer buffer;
    private DatagramChannel channel;

    public static void main(String[] args) throws Exception {
        ClientDemo clientDemo = new ClientDemo();
        clientDemo.init();
        ClientDemo clientDemo1 = new ClientDemo();
        clientDemo1.init();

        new Thread(clientDemo).start();
        new Thread(clientDemo1).start();
    }

    public void init() throws IOException {
        buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("localhost",8888));
    }

    @Override
    public void run() {

        for(int i = 0; i < 3; i++) {
            String s = Thread.currentThread().getName() + "哈哈哈哈哈";
            buffer.put(s.getBytes());
            buffer.flip();
            try {
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.clear();
        }

    }
}
