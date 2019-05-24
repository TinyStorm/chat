package org.meng.service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 大厅
 */
@Slf4j
public class Hall extends AbstractRegister {
    private Map<String, ChatRoom> chatRoomMap = new HashMap<>(50);//现有chat room
    private Map<String, ChatRoom> userChatRoomMap = new HashMap<>(500);


    public Hall(String name) {
        super(name);
    }

    public void enter(String room, Channel channel) {
        chatRoomMap.get(room).register(channel);
    }

    public void quit(Channel channel) {
        chatRoomMap.get(channel.remoteAddress()).unRegister(channel);
    }


    public void createRoom(String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName);
        chatRoomMap.put(roomName, chatRoom);
        log.info("{} has been created", roomName);
    }

    public void deleteRoom(String roomName) {
        ChatRoom chatRoom = chatRoomMap.remove(roomName);
        chatRoom.close();
        log.info("{} has been closed", roomName);
    }

    public String listRoom() {
        StringBuilder sb = new StringBuilder("");
        chatRoomMap.forEach((s, chatRoom) -> {
            sb.append(s).append(" ");
        });
        return sb.toString();

    }

    public void speak(String name, String message) {
        ChatRoom chatRoom = userChatRoomMap.get(name);
        if (chatRoom != null) {
            chatRoom.speak(name, message);
        } else {
            super.registeredCtx.forEach((s, channel) -> {
                channel.writeAndFlush(name + ":" + message);
            });
        }


    }

    public void scanUser(Channel channel) {
        ChatRoom chatRoom = userChatRoomMap.get(channel.attr(NAME_KEY).get());
        String users = "";
        if (chatRoom != null) {
            users = chatRoom.scanUser();
        } else {
            users = this.scanUser();
        }
        channel.writeAndFlush(users);
    }
}
