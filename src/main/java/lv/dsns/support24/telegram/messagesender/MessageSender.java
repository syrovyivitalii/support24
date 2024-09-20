package lv.dsns.support24.telegram.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {
    void sendMessage(SendMessage sendMessage);
}
