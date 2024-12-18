package org.example.Telegram;

import org.example.MessageBehaviour;

public class TelegramMessage implements MessageBehaviour {

    private String botToken;
    private String lastMessageFilePath;
    private String chatID;

    private SendTelegramMessage sendTelegramMessage;
    private GetTelegramMessage getTelegramMessage;

    public TelegramMessage(String botToken, String chatID, String lastMessageFilePath) {
        this.botToken = botToken;
        this.chatID = chatID;
        this.lastMessageFilePath = lastMessageFilePath;
        sendTelegramMessage = new SendTelegramMessage(botToken, chatID);
        getTelegramMessage = new GetTelegramMessage(botToken, lastMessageFilePath);
    }

    @Override
    public void setMessage(String message) {
        sendTelegramMessage.setMessage(message);
    }

    @Override
    public void sendMessage() {
        sendTelegramMessage.sendMessage();
    }

    @Override
    public String getLastMessage() {
        return getTelegramMessage.retrieveLastMessage();
    }
}
