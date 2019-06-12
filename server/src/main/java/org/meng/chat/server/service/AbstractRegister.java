package org.meng.chat.server.service;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractRegister implements RegisterAble {

    private String name;
    /**
     * 保存 用户-->ChatRoom
     */
    protected static Map<String, AbstractRegister> userChatRoomMap = new HashMap<>(500);

    /**
     * 保存当前房间的用户
     */
    protected Map<String, Channel> registeredCtx;

    public AbstractRegister(String name) {
        this.name = name;
        registeredCtx = new HashMap<>();
    }

    public void register(Channel channel) {
        String host = channel.remoteAddress().toString();
        Attribute<String> attribute = channel.attr(NAME_KEY);
        if (attribute.get() == null || attribute.get().equals("")) {
            attribute.set(host);
        } else {
            host = attribute.get();
        }
        registeredCtx.put(host, channel);
        userChatRoomMap.put(host, this);
        log.info("{} has entered {}", host, name);
    }

    public void unRegister(Channel channel) {
        String host = channel.remoteAddress().toString();
        Attribute<String> attribute = channel.attr(NAME_KEY);
        if (attribute.get() == null || attribute.get().equals("")) {
            attribute.set(host);
        } else {
            host = attribute.get();
        }
        registeredCtx.remove(host);
        userChatRoomMap.remove(host);
        log.info("{} is quit {}", host, name);
    }

    public void rename(Channel channel, String name) {
        registeredCtx.remove(channel.attr(NAME_KEY).get());//移除之前的名称
        AbstractRegister register = userChatRoomMap.remove(channel.attr(NAME_KEY).get());
        channel.attr(RegisterAble.NAME_KEY).set(name);//在channel中放入新的昵称
        registeredCtx.put(name, channel);//放入Room的map中
        userChatRoomMap.put(name, register);
    }

    public void doSpeak(String name, String message) {
        registeredCtx.forEach((s, channel) -> {
            channel.writeAndFlush(name + ":" + message);
        });
    }

    public String scanUser() {
        StringBuilder sb = new StringBuilder("");
        registeredCtx.keySet().forEach(s -> sb.append(s).append(" "));
        return sb.toString();
    }


}
