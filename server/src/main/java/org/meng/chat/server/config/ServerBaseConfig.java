package org.meng.chat.server.config;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.meng.chat.server.handler.codec.BufToStringHandler;
import org.meng.chat.server.handler.biz.BizHandler;
import org.meng.chat.server.handler.codec.StringToBufHandler;
import org.meng.chat.server.service.Hall;
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
