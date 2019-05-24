package org.meng.service;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractRegister implements RegisterAble {

    private String name;

    public AbstractRegister(String name) {
        this.name = name;
        registeredCtx = new HashMap<>();
    }

    protected Map<String, Channel> registeredCtx;

    public void register(Channel channel) {
        String host = channel.remoteAddress().toString();
        Attribute<String> attribute = channel.attr(NAME_KEY);
        if (attribute.get() == null || attribute.get().equals("")) {
            attribute.set(host);
        } else {
            host = attribute.get();
        }
        registeredCtx.put(host, channel);
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
        log.info("{} is quit {}", host, name);
    }

    public void rename(Channel channel, String name) {
        registeredCtx.remove(channel.attr(NAME_KEY).get());//移除之前的名称
        channel.attr(RegisterAble.NAME_KEY).set(name);//在channel中放入新的昵称
        registeredCtx.put(name, channel);//放入Room的map中
    }

    public abstract void speak(String name, String message);

    public String scanUser() {
        StringBuilder sb = new StringBuilder("");
        registeredCtx.keySet().forEach(s -> sb.append(s).append(" "));
        return sb.toString();
    }

    ;
}
