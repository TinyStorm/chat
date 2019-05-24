package org.meng.config;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.meng.handler.biz.BizHandler;
import org.meng.handler.codec.BufToStringHandler;
import org.meng.handler.codec.StringToBufHandler;
import org.meng.service.Hall;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties(ServerProperties.class)
public class ServerBaseConfig {


    @Bean("initializer")
    public ChannelInitializer<SocketChannel> initializer(ServerProperties serverProperties, Hall hall) {

        EventLoopGroup custom = new DefaultEventLoopGroup(serverProperties.getCustomThread());

        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new BufToStringHandler())
                        .addLast(new StringToBufHandler())
                        .addLast(custom, new BizHandler(hall));
            }
        };

    }


    @Bean
    public Hall hall() {
        Hall hall = new Hall("Hall");
        hall.createRoom("Default Room");
        return hall;
    }

    @Bean("server")
    public Server server(ChannelInitializer<SocketChannel> initializer,
                         ServerProperties serverProperties) {
        Server server = new Server(serverProperties.getIp(), serverProperties.getPort(), serverProperties.getMaxConnects(), initializer);
        return server;
    }


}
