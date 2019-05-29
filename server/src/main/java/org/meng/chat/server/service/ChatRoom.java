package org.meng.chat.server.service;

import lombok.extern.slf4j.Slf4j;

/**
 * 聊天室
 */
@Slf4j
public class ChatRoom extends AbstractRegister {


    public ChatRoom(String roomName) {
        super(roomName);
    }

    @Override
    public void speak(String name, String message) {
        super.registeredCtx.forEach((s, channel) -> {
            channel.writeAndFlush(name + ":" + message);
        });
    }


    public void close() {
    }
}
