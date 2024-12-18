package org.example.Telegram;

import org.example.util.LogPrint;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SendTelegramMessage {
    private final String botToken;
    private final String chatID;

    private String message;
    private JSONObject query;

    public SendTelegramMessage(String botToken, String chatId) {
        this.botToken = botToken;
        this.chatID = chatId;
        this.message = "";
    }

    public void setMessage(String message) {
        this.message = message;
        query = new JSONObject();
        query.put("chat_id", chatID);
        query.put("text", message);
        query.put("parse_mode", "HTML");
    }

    public void sendMessage() {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(query.toString())).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            LogPrint.printRed("ERROR", String.format("Error sending the message = %s,\nchatID=%s\nException = %s", message, chatID, e.getClass().getSimpleName()));
            throw new RuntimeException(e);
        }

        JSONObject responseJson = new JSONObject(response.body());
        if (responseJson.getBoolean("ok")) {
            LogPrint.printGreen("INFO", "Message sent correctly");
        } else {
            LogPrint.printRed("ERROR", "Error sending the message --> " + responseJson.getString("description"));
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

