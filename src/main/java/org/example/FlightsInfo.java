package org.example;

import org.example.util.FileUtil;
import org.example.util.LogPrint;
import org.example.util.WebUtil;
import org.openqa.selenium.WebDriver;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class FlightsInfo implements Aggregate{

    private static final String RANDOM_NUMBERS_FILE_PATH = "random_numbers.txt";
    private String lastPriceFilePathRoot;
    private String userName;
    private String flyLinkListFilePath;
    private List<Flight> flightsInfoList;

    public FlightsInfo(String flyLinkListFilePath, String userName, String lastPriceFilePathRoot) {
        this.lastPriceFilePathRoot = lastPriceFilePathRoot;
        this.userName = userName;
        this.flyLinkListFilePath = flyLinkListFilePath;
        this.flightsInfoList = new ArrayList<>();
    }

    public void getFlightsInfo() {
        List<String> flyLinks = FileUtil.getLines(flyLinkListFilePath);
        int i = 1;
        int totFlyLinks = flyLinks.size();

        String company;
        String randomNumber;
        String link;

        for (String flyLink : flyLinks) {
            try {
                String[] parts = parseFlyLink(flyLink);
                company = parts[0];
                randomNumber = parts[1];
                link = parts[2];
            } catch (Exception e) {
                LogPrint.printRed("ERROR", String.format("user=%s, The following exception was thrown parsing flylink = %s in getFlightsInfo(): %s", userName, flyLink, e.getClass().getSimpleName()));
                throw new RuntimeException(e);
            }
            try {
                WebDriver driver = WebUtil.createDriver(link, userName);
                Flight flight = new Flight(company, driver, link, randomNumber);
                addFlightToList(flight);
                LogPrint.printGreen("INFO", String.format("User = %s\n\tCorrectly setted link n. %d/%d", userName, i, totFlyLinks));
                i++;
            } catch (Exception e) {
                LogPrint.printRed("ERROR", String.format("user=%s, company=%s link=%s \n\t" + "The following exception was thrown during creation of the driver in get_flights_info(): %s", userName, company, link, e.getClass().getSimpleName()));
                throw new RuntimeException(e);
            }
        }
    }

    public void addFlightLink(String company, String link) {
        WebDriver driver;
        try {
            driver = WebUtil.createDriver(link, userName);
        } catch (Exception e) {
            LogPrint.printRed("ERROR", String.format("user=%s company=%s link=%s \n\tThe following exception was thrown during creation of the driver in addFlyLink: %s", userName, company, link, e.getClass().getSimpleName()));
            throw new RuntimeException(e);
        }
        String randomNumber = getRandomNumber();
        String lastPriceFilePath = lastPriceFilePathRoot + randomNumber + "_.txt";
        FileUtil.updateFileValue(lastPriceFilePath, "0");

        Flight flight = new Flight(company, driver, link, randomNumber);
        addFlightToList(flight);
        FileUtil.addLine(flyLinkListFilePath, String.format("%s -- %s -- %s", company, randomNumber, link));
        LogPrint.printGreen("INFO", String.format("User = %s\n\tCorrectly added link=%s", userName, link));
    }

    private void addFlightToList(Flight flight) {
        flightsInfoList.add(flight);
    }

    private String[] parseFlyLink(String flyLink) {
        String[] parts = flyLink.split("--", 3);
        for (int i = 0; i < parts.length; ++i) {
            parts[i] = parts[i].strip();
        }
        return parts;
    }

    private String getRandomNumber() {
        Random random = new Random();
        while (true) {
            String randomNumber = String.valueOf(random.nextInt(1000) + 1);
            if (!FileUtil.getLine(RANDOM_NUMBERS_FILE_PATH).contains(randomNumber)) {
                FileUtil.addLine(RANDOM_NUMBERS_FILE_PATH, randomNumber);
                return randomNumber;
            }
        }
    }

    public String getLastPriceFilePathRoot() {
        return lastPriceFilePathRoot;
    }

    public void setLastPriceFilePathRoot(String lastPriceFilePathRoot) {
        this.lastPriceFilePathRoot = lastPriceFilePathRoot;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFlyLinkListFilePath() {
        return flyLinkListFilePath;
    }

    public void setFlyLinkListFilePath(String flyLinkListFilePath) {
        this.flyLinkListFilePath = flyLinkListFilePath;
    }

    public List<Flight> getFlightsInfoList() {
        return flightsInfoList;
    }

    public void setFlightsInfoList(List<Flight> flightsInfoList) {
        this.flightsInfoList = flightsInfoList;
    }

    public Iterator<Flight> createIterator() {
        return flightsInfoList.iterator();
    }
}
