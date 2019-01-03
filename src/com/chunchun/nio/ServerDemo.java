package com.chunchun.nio;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Hu
 * @date 2019/1/2 11:26
 */

public class ServerDemo {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {

        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);

        DatagramSocket socket = channel.socket();
        socket.bind(new InetSocketAddress(8888));

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);

        while(true) {
            try {
                int num = selector.select();
                if(num > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while(it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if(key.isReadable()) {
                            //key.cancel();
                            executorService.execute(() -> {
                                try {
                                    DatagramChannel datagramChannel = (DatagramChannel) key.channel();
                                    ByteBuffer buffer = ByteBuffer.allocate(20);
                                    buffer.clear();
                                    datagramChannel.receive(buffer);
                                    buffer.flip();
                                    StringBuilder s = new StringBuilder();
                                    while(buffer.hasRemaining()) {
                                        buffer.get(new byte[buffer.limit()]);
                                        s.append(new String(buffer.array()));
                                    }
                                    buffer.clear();
                                    System.out.println("------" + s.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
