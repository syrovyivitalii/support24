package lv.dsns.support24.telegram.processors;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Processor {
    void executeMessage(Message message);

    default void process(Update update){
        if (update.hasMessage()){
            executeMessage(update.getMessage());
        }
    }
}
