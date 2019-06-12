package org.meng.chat.server.service;

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


    public Hall(String name) {
        super(name);
    }

    public void enter(String room, Channel channel) {
        userChatRoomMap.get(channel.attr(NAME_KEY).get()).unRegister(channel);
        chatRoomMap.get(room).register(channel);
    }

    public void quit(Channel channel) {
        userChatRoomMap.get(channel.attr(NAME_KEY).get()).unRegister(channel);
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

    /**
     * 供上层调用
     *
     * @param name
     * @param message
     */
    public void speak(String name, String message) {
        AbstractRegister chatRoom = userChatRoomMap.get(name);
        if (chatRoom != null) {
            //大厅也是一个聊天室,可以说话,逻辑需要和聊天室保持一致,否则不利于维护
            chatRoom.doSpeak(name, message);
        }
    }


    public void scanUser(Channel channel) {
        AbstractRegister chatRoom = userChatRoomMap.get(channel.attr(NAME_KEY).get());
        if (chatRoom != null) {
            String users = chatRoom.scanUser();
            channel.writeAndFlush(users);
        }
    }
}
