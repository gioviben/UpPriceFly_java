package org.example;

import org.example.util.FileUtil;
import org.example.util.LogPrint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Setup {

    private String lastMessageAdminFilePath;
    private List<User> usersList = new ArrayList<>();
    private String activeUsersListFilePath;
    private MessageFactory messageFactory;



    public void init() {

        //LogPrint.printBlueLine();
        LogPrint.printTitle("\n\tADMIN SETUP\n");

        try {
            this.lastMessageAdminFilePath = adminSetup();
        } catch (Exception e) {
            LogPrint.printRed("ERROR", "The following exception was thrown during the Admin setup: " + e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }

        LogPrint.printGreen("INFO", "Successfully Admin setup");
        System.out.println();

        //printBlueLine();
        LogPrint.printTitle("\n\tUSER INFO FOLDER SETUP\n");

        try {
            this.activeUsersListFilePath = usersInfoFolderSetup();
        } catch (Exception e) {
            LogPrint.printRed("ERROR", "The following exception was thrown during the Users info folder setup: " + e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }

        LogPrint.printGreen("INFO", "Successfully Users info folder setup");
        System.out.println();


        //printBlueLine();
        LogPrint.printTitle("\n\tPOPULATING USER LIST FROM FILE\n");

        List<String[]> usersListInfo;

        try {
            usersListInfo = populateUsersListFromUsersFile();
            for (String[] userInfo : usersListInfo) {
                System.out.println();
                String userName = userInfo[0];
                MessageConfiguration config = new MessageConfiguration();
                config.addConfig("BOT_TOKEN", userInfo[1]);
                config.addConfig("CHAT_ID", userInfo[2]);
                String lastMessageFilePath = "." + File.separator + userName + File.separator + "last_message.txt";
                config.addConfig("LastMessageFilePath", lastMessageFilePath);
                MessageBehaviour messageBehaviour = messageFactory.createMessageBehaviour(config);
                User user = new User(userName, messageBehaviour);
                addUserToList(user);
            }
        } catch (Exception e) {
            LogPrint.printRed("ERROR", "The following exception was thrown during the users list population: " + e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }

        if (!usersListInfo.isEmpty()) {
            System.out.println();
            LogPrint.printUncolored("INFO", "usersList = ");
            printList(usersList);
            LogPrint.printGreen("INFO", "Successfully populated users list");
            System.out.println();
        }
        //printBlueLine();



    }

    private String adminSetup() {
        String adminFolderPath = "." + File.separator + "Admin";
        String lastMessageAdminFilePath = "." + File.separator + "Admin" + File.separator + "last_message.txt";
        String randomNumberFilePath = "." + File.separator + "random_numbers.txt";

        try {
            LogPrint.printUncolored("INFO", "Creating Admin folder");
            Files.createDirectory(Path.of(adminFolderPath));
            //LogPrint.printUncolored("INFO", "Creating Random Number File");
            FileUtil.createEmptyFile(randomNumberFilePath);
            LogPrint.printUncolored("INFO", "Creating last message file");
            FileUtil.createEmptyFile(lastMessageAdminFilePath);
        } catch (IOException e) {
            LogPrint.printUncolored("INFO", "Admin folder, last message file and random number file already exist");
        }

        return lastMessageAdminFilePath;
    }

    private String usersInfoFolderSetup() {
        String usersInfoFolderPath = "." + File.separator + "Users Info";
        String activeUsersListFilePath = usersInfoFolderPath + File.separator + "users_list.txt";

        try {
            LogPrint.printUncolored("INFO", "Creating Users Info folder");
            Files.createDirectory(Path.of(usersInfoFolderPath));
            LogPrint.printUncolored("INFO", "Creating users list file");
            FileUtil.createEmptyFile(activeUsersListFilePath);
        } catch (IOException e) {
            LogPrint.printUncolored("INFO", "Users Info folder and Users list file already exist");
        }
        return activeUsersListFilePath;
    }

    private List<String[]> populateUsersListFromUsersFile() {
        String activeUsersListFilePath = "." + File.separator + "Users Info" + File.separator + "users_list.txt";
        List<String> users = FileUtil.getLines(activeUsersListFilePath);

        LogPrint.printUncolored("\nINFO", "Found " + users.size() + " active users\n");
        List<String[]> usersList = new ArrayList<>();

        for (String user : users) {
            String[] info = user.split("--", 3);
            usersList.add(new String[]{info[0].trim(), info[1].trim(), info[2].trim()});
        }

        return usersList;
    }

    private void printList(List<User> usersList) {
        for (User user : usersList) {
            System.out.println(user.toString());
        }
    }

    private void addUserToList(User user){
        usersList.add(user);
    }
    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    public String getLastMessageAdminFilePath() {
        return lastMessageAdminFilePath;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public String getActiveUsersListFilePath() {
        return activeUsersListFilePath;
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }
}

