package org.meng.service;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public interface RegisterAble {
    AttributeKey<String> NAME_KEY = AttributeKey.newInstance("name");

    void register(Channel channel);
    void unRegister(Channel channel);
}
