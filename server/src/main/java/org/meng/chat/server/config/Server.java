package org.meng.chat.server.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;


@Slf4j
public class Server {


    private ChannelFuture future;

    public Server(String ip, int port, int maxConnects, ChannelInitializer<SocketChannel> initializer) {
        EventLoopGroup acceptor = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptor, worker);
        bootstrap.option(ChannelOption.SO_BACKLOG, maxConnects);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(initializer);
        try {
            // 绑定端口，开始接收进来的连接
            future = bootstrap.bind(ip, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @PostConstruct
    public void start() {

        new Thread(() -> {
            log.info("server start ");
            try {
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }


}
