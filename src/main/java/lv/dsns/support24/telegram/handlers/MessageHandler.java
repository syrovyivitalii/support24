package lv.dsns.support24.telegram.handlers;

import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.telegram.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@Component
@Slf4j
public class MessageHandler implements Handler<Message>{

    private MessageSender messageSender;

    public MessageHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void choose(Message message) {
        if (message.hasText()){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setParseMode(ParseMode.HTML);

            switch (message.getText()){
                case "/start":
                    sendMessage.setText("hello bot");
            }
            messageSender.sendMessage(sendMessage);
        }
    }
}
