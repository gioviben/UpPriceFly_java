package org.example;

import org.example.util.FileUtil;
import org.example.util.LogPrint;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Routine {

    private static final int MAX_TRY_NUMBER = 1;
    private List<User> usersList;
    private String activeUsersListFilePath;
    private MessageBehaviour adminMessageManager;
    private MessageFactory messageFactory;

    public Routine(List<User> usersList, String activeUsersListFilePath, MessageBehaviour adminMessageManager, MessageFactory messageFactory) {
        this.usersList = usersList;
        this.activeUsersListFilePath = activeUsersListFilePath;
        this.adminMessageManager = adminMessageManager;
        this.messageFactory = messageFactory;
    }

    public void start() {

        LogPrint.printTitle("\n\tSTARTING THE ROUTINE\n");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        int tryNumber = 0;

        try {
            if (tryNumber < MAX_TRY_NUMBER) {
                scheduler.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        routine();
                    }
                }, 0, 60, TimeUnit.SECONDS);
            } else {
                LogPrint.printRed("ERROR", "Max try number reached, closing the process...");
                try {
                    adminMessageManager.setMessage("An error occurs during the process,\nplease contact the admin");
                    adminMessageManager.sendMessage();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            LogPrint.printYellow("WARNING", "Trying the routine again, try_number " + tryNumber);
            tryNumber++;
        }
    }

    private void routine() {
        System.out.println();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedNow = now.format(formatter);
        LogPrint.printBlue("INFO", "Last Check: " + formattedNow);

        String lastAdminMessage = adminMessageManager.getLastMessage();

        if (lastAdminMessage != null) {
            String[] userInfo = lastAdminMessage.split("\n", 3);
            FileUtil.addLine(activeUsersListFilePath, lastAdminMessage.replace("\n", "--"));
            String userName = userInfo[0].strip();
            MessageConfiguration config = new MessageConfiguration();
            config.addConfig("BOT_TOKEN", userInfo[1].strip());
            config.addConfig("CHAT_ID", userInfo[2].strip());
            String lastMessageFilePath = "." + File.separator + userName + File.separator + "last_message.txt";
            config.addConfig("LastMessageFilePath", lastMessageFilePath);
            MessageBehaviour messageBehaviour = messageFactory.createMessageBehaviour(config);
            User user = new User(userName, messageBehaviour);
            addUserToList(user);
            adminMessageManager.setMessage("New user correctly added");
            adminMessageManager.sendMessage();
            LogPrint.printGreen("INFO", "New user found, added to users list file");
        }

        for (User user : usersList) {
            System.out.println("\n\t\tUSER = " + user.getUserName() + "\n");
            String lastMessage = user.getMessageManager().getLastMessage();
            if (lastMessage != null) {
                if (lastMessage.contains("www.")) {
                    String company;
                    String link;
                    try {
                        String[] companyAndLink = parseCompany(lastMessage);
                        company = companyAndLink[0];
                        link = companyAndLink[1];
                    } catch (Exception e) {
                        LogPrint.printRed("ERROR", "user=" + user.getUserName() + ", Exception while parsing company: " + e.getMessage());
                        throw new RuntimeException("Error in parsing company");
                    }

                    if (!FileUtil.getLine(user.getFlyLinkListFilePath()).contains(link)) {
                        user.addFlightLink(company, link);
                    }
                }

            }
            user.checkLowestPrice();
        }
    }

    private void addUserToList(User user){
        usersList.add(user);
    }

    private String[] parseCompany(String lastMessage) {
        String[] parts = lastMessage.split("\n", 2);
        return new String[]{parts[0].toUpperCase().trim(), parts[1].trim()};
    }

    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }
}
