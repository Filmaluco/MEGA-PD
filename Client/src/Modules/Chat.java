package Modules;

import PD.Core.Message;
import PD.Core.Module;
import PD.Core.User;

import java.util.List;

public class Chat implements Runnable, Module.ChatModule {
    @Override
    public int[] getOnlineUsers() {
        return new int[0];
    }

    @Override
    public int[] getRecentUsers() {
        return new int[0];
    }

    @Override
    public User getUsernameFromID(int i) {
        return null;
    }

    @Override
    public List<Message> getMessages(int i) {
        return null;
    }

    @Override
    public int[] getUnreadMessages(int i) {
        return new int[0];
    }

    @Override
    public boolean sendMessage(Message message) {
        return false;
    }

    @Override
    public boolean broadCastMessage(Message message) {
        return false;
    }

    @Override
    public void run() {

    }
}
