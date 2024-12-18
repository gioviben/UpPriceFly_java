package org.example;

import org.openqa.selenium.WebDriver;

public class Flight {
    private String company;
    private WebDriver driver;
    private String link;
    private String randomNumber;

    public Flight(String company, WebDriver driver, String link, String randomNumber) {
        this.company = company;
        this.driver = driver;
        this.link = link;
        this.randomNumber = randomNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }
}
