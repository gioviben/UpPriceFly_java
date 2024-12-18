package org.example.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebUtil {

    private static final String RYANAIR_COOKIE_POPUP = "//*[@id='cookie-popup-with-overlay']/div/div[3]/button[2]";

    public static void clickButton(WebDriver driver, String xpath, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        element.click();
    }

    public static String retrieveFieldValue(WebDriver driver, String xpath, int tryNumber, int timeout) {
        String valueText = "";
        try {
            if (tryNumber >= 5) {
                LogPrint.printRed("ERROR", "Error in retrieveFieldValue() --> Max number of tries reached");
                throw new Exception();
            }
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement value = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            valueText = value.getText();
        } catch (TimeoutException e) {
            LogPrint.printRed("ERROR", "Error in retrieveFieldValue() --> Timeout Exception caught, trying again n. " + tryNumber);
            valueText = retrieveFieldValue(driver, xpath, tryNumber + 1, timeout);
        } catch (NoSuchElementException e) {
            if (xpath != null && !xpath.isEmpty()) {
                LogPrint.printRed("ERROR", "Error in retrieveFieldValue() --> The selector " + xpath + " can't be found");
            }
            throw new RuntimeException();
        } catch (Exception e) {
            LogPrint.printRed("ERROR", "Error in retrieveFieldValue() --> A more general exception was thrown: " + e.getClass().getSimpleName());
            throw new RuntimeException();
        }
        return valueText;
    }

    public static WebDriver createDriver(String link, String userName) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(link);
        closeCookie(driver, userName);
        return driver;
    }

    public static void closeCookie(WebDriver driver, String userName) {
        try {
            clickButton(driver, RYANAIR_COOKIE_POPUP, 4);
        } catch (NoSuchElementException e) {
            LogPrint.printYellow("WARNING", "User=" + userName + " No cookie banner was found, continuing as usual");
        } catch (Exception e) {
            LogPrint.printRed("ERROR", "User=" + userName + " The following exception was thrown during the close cookie banner process: " + e.getClass().getSimpleName());
            throw new RuntimeException();
        }
    }
}

