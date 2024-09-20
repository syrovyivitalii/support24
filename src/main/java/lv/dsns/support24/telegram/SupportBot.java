package lv.dsns.support24.telegram;

import lv.dsns.support24.telegram.processors.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class SupportBot extends TelegramLongPollingBot {

    private Processor processor;

    @Value("${telegram.BOT_USERNAME}")
    private String botUsername;
    @Value("${telegram.BOT_TOKEN}")
    private String botToken;


    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        processor.process(update);
    }
    @Autowired
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
