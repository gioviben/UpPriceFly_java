package org.example.Telegram;

import org.example.util.FileUtil;
import org.example.util.LogPrint;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class GetTelegramMessage {

    private final String botToken;
    private final String lastMessageFilePath;
    private final String URL;
    ChatMessage lastMessage;

    public GetTelegramMessage(String botToken, String lastMessageFilePath) {
        this.botToken = botToken;
        this.lastMessageFilePath = lastMessageFilePath;
        this.URL = String.format("https://api.telegram.org/bot%s/", this.botToken);
    }

    private String getRequest(String url) throws IOException {
        java.net.URL requestUrl = new java.net.URL(url);
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");

        try (java.io.InputStream stream = connection.getInputStream()) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private JSONObject getJsonFromUrl(String url) throws IOException {
        String content = getRequest(url);
        return new JSONObject(content);
    }

    private JSONObject getUpdates() throws IOException {
        String url = URL + "getUpdates";
        return getJsonFromUrl(url);
    }

    private void getLastChatIdAndText(JSONObject updates) {
        try {
            JSONArray results = updates.getJSONArray("result");
            if (results.isEmpty()) {
                LogPrint.printYellow("WARNING", "No recent message found");
                lastMessage = null;
                return ;
            }

            JSONObject lastUpdate = results.getJSONObject(results.length() - 1);
            String text = lastUpdate.getJSONObject("message").getString("text");
            long chatId = lastUpdate.getJSONObject("message").getJSONObject("chat").getLong("id");

            this.lastMessage = new ChatMessage(text, chatId);
        } catch (Exception e) {
            LogPrint.printRed("ERROR", "Exception retrieving last chat id and text: " + e.getClass().getSimpleName());
            this.lastMessage = null;
        }
    }

    public String retrieveLastMessage() {
        String lastTextChat = FileUtil.getLine(lastMessageFilePath);
        JSONObject updates;

        try {
            updates = getUpdates();
        } catch (IOException e) {
            LogPrint.printRed("ERROR", "Exception getting chat updates: " + e.getClass().getSimpleName());
            return null;
        }

        getLastChatIdAndText(updates);

        if (lastMessage != null) {
            String combinedMessage = lastMessage.getText() + " " + lastMessage.getChatId();

            if (!combinedMessage.equals(lastTextChat)) {
                LogPrint.printCyan("INFO", "Found new message = ", "\n" + lastMessage.getText() + "\n", "");

                FileUtil.updateFileValue(lastMessageFilePath, combinedMessage);

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                return lastMessage.getText();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            LogPrint.printUncolored("INFO", "No recent message found");
        }
        return null;
    }
}

