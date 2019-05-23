package org.meng.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.meng.handler.biz.EchoHandler;
import org.meng.handler.codec.BufToStringHandler;
import org.meng.handler.codec.StringToBufHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties(ServerProperties.class)
public class ServerBaseConfig {


    public List<ChannelHandler> codecHandler() {
        List<ChannelHandler> handlers = new ArrayList<>();
        handlers.add(new BufToStringHandler());
        handlers.add(new StringToBufHandler());

        return handlers;
    }

    public List<ChannelHandler> bizHandler() {
        List<ChannelHandler> handlers = new ArrayList<>();
        handlers.add(new EchoHandler());

        return handlers;
    }

    @Bean("bizHandler")
    public ChannelInitializer<SocketChannel> initializer(ServerProperties serverProperties) {

        List<ChannelHandler> codec = codecHandler();
        List<ChannelHandler> biz = bizHandler();
        EventLoopGroup custom = new DefaultEventLoopGroup(serverProperties.getCustomThread());

        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(codec.toArray(new ChannelHandler[codec.size()]))
                        .addLast(custom, biz.toArray(new ChannelHandler[biz.size()]));
            }
        };

    }

    @Bean("server")
    public Server server(ChannelInitializer<SocketChannel> initializer, ServerProperties serverProperties) {
        Server server = new Server(serverProperties.getIp(), serverProperties.getPort(), serverProperties.getMaxConnects(), initializer);
        return server;
    }


}
