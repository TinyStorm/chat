package org.meng.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;


@Slf4j
public class Server {


    private ChannelInitializer<SocketChannel> initializer;
    private ChannelFuture future;

    public Server(String ip, int port, int maxConnects, ChannelInitializer<SocketChannel> initializer) {
        this.initializer = initializer;
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
