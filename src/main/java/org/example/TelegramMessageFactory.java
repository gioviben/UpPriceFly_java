package org.example;

import org.example.Telegram.TelegramMessage;

public class TelegramMessageFactory implements MessageFactory{
    @Override
    public MessageBehaviour createMessageBehaviour(MessageConfiguration config) {
        String botToken = config.getConfig("BOT_TOKEN");
        String chatID = config.getConfig("CHAT_ID");
        String lastMessageFilePath = config.getConfig("LastMessageFilePath");
        return new TelegramMessage(botToken, chatID, lastMessageFilePath);
    }
}
