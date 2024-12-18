package org.example.Telegram;

public class ChatMessage {
    private final String text;
    private final long chatId;

    public ChatMessage(String text, long chatId) {
        this.text = text;
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public long getChatId() {
        return chatId;
    }
}
