package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.Telegram.TelegramMessage;
import org.example.checkprice.PriceRetrievalFactory;
import org.example.checkprice.RyanairPriceRetrieval;
import org.example.util.LogPrint;

import java.util.List;

public class Application {
    private static String BOT_TOKEN_ADMIN;
    private static String CHAT_ID_ADMIN;

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        BOT_TOKEN_ADMIN = dotenv.get("BOT_TOKEN_ADMIN");
        CHAT_ID_ADMIN = dotenv.get("CHAT_ID_ADMIN");

        PriceRetrievalFactory.registerBehaviour("RYANAIR", new RyanairPriceRetrieval());

        Setup setup = new Setup();
        setup.setMessageFactory(new TelegramMessageFactory()); //<----
        setup.init();

        Routine routine = getRoutine(setup, BOT_TOKEN_ADMIN, CHAT_ID_ADMIN);
        routine.start();
    }

    private static Routine getRoutine(Setup setup, String BOT_TOKEN_ADMIN, String CHAT_ID_ADMIN) {
        List<User> usersList = setup.getUsersList();
        String activeUsersListFilePath = setup.getActiveUsersListFilePath();

        String lastMessageFilePath = setup.getLastMessageAdminFilePath();
        MessageBehaviour adminMessageManager = new TelegramMessage(BOT_TOKEN_ADMIN, CHAT_ID_ADMIN, lastMessageFilePath);
        
        MessageFactory messageFactory = new TelegramMessageFactory();

        Routine routine = new Routine(usersList, activeUsersListFilePath, adminMessageManager, messageFactory);
        return routine;
    }
}
