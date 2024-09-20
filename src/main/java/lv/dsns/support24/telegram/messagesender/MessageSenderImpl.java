package lv.dsns.support24.telegram.messagesender;

import lv.dsns.support24.telegram.SupportBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderImpl implements MessageSender {
    private final SupportBot supportBot;

    @Autowired
    public MessageSenderImpl(SupportBot supportBot) {
        this.supportBot = supportBot;
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            // Call the instance method execute on supportBot
            supportBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
