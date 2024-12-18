package org.example;

import org.example.checkprice.CheckPrice;
import org.example.util.FileUtil;
import org.example.util.LogPrint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class User {

    private String userName;
    private String lastPriceFilePathRoot;
    private String flyLinkListFilePath;
    private MessageBehaviour messageManager;
    private CheckPrice checkPrice;
    private FlightsInfo flightsInfo;

    public User(String userName, MessageBehaviour messageManager) {
        String lastMessageFilePath;
        boolean directoryExists = false;

        try {
            Files.createDirectory(Paths.get("." + File.separator + userName));
            Files.createDirectory(Paths.get("." + File.separator + userName + File.separator + "Last_Prices_Folder"));
        } catch (IOException e) {
            directoryExists = true;
        }

        this.userName = userName;
        lastMessageFilePath = "." + File.separator + userName + File.separator + "last_message.txt";
        this.lastPriceFilePathRoot = "." + File.separator + userName + File.separator + "Last_Prices_Folder" + File.separator + "last_price_";
        this.flyLinkListFilePath = "." + File.separator + userName + File.separator + "fly_link_list.txt";

        if (!directoryExists) {
            FileUtil.createEmptyFile(lastMessageFilePath);
            FileUtil.createEmptyFile(this.flyLinkListFilePath);
        }

        this.messageManager = messageManager;
        this.checkPrice = new CheckPrice(userName);
        this.flightsInfo = new FlightsInfo(flyLinkListFilePath, userName, this.lastPriceFilePathRoot);
        if (directoryExists){
            flightsInfo.getFlightsInfo();
        }
    }

    public void checkLowestPrice() {

        Iterator<Flight> iterator = flightsInfo.createIterator();
        while (iterator.hasNext()) {
            Flight flight = iterator.next();

            int randomNumber = Integer.parseInt(flight.getRandomNumber());
            String lastPriceFilePath = lastPriceFilePathRoot + randomNumber + "_.txt";

            try {
                checkPrice.checkPrice(flight, messageManager, lastPriceFilePath);
            } catch (Exception e) {
                LogPrint.printRed("ERROR", "User = " + userName + ", an exception was thrown during the check_price process\n\t" + "Company = " + flight.getCompany() + ", Link = " + flight.getLink() + ", Random_number = " + randomNumber + "\n\t" + "Continuing with the next flight if any");
            }
        }
    }

    public void addFlightLink(String company, String link){
        flightsInfo.addFlightLink(company, link);
        messageManager.setMessage("Flight correctly added\nStarting to monitor");
        messageManager.sendMessage();
    }

    @Override
    public String toString() {
        return userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFlyLinkListFilePath() {
        return flyLinkListFilePath;
    }

    public MessageBehaviour getMessageManager() {
        return messageManager;
    }

    public FlightsInfo getFlightsInfo() {
        return flightsInfo;
    }
}

