package org.example.checkprice;

import org.example.util.WebUtil;
import org.openqa.selenium.WebDriver;

public class RyanairPriceRetrieval implements PriceRetrievalBehaviour {
    private static final String RYANAIR_PRICE_XPATH =
            "/html/body/app-root/flights-root/div/div/div/div/flights-lazy" + "-content/flights-summary-container/flights-summary/div/div[1]/journey-container/journey/div/div[2]/div" + "/carousel-container/carousel/div/ul/li[3]/carousel-item/button/div[2]/flights-price/ry-price/span[%d]";

    @Override
    public double retrievePrice(WebDriver driver) {
        driver.navigate().refresh();
        StringBuilder price = new StringBuilder();

        for (int index = 2; index <= 4; index++) {
            String value = WebUtil.retrieveFieldValue(driver, String.format(RYANAIR_PRICE_XPATH, index), 0, 30);
            price.append(value);
        }

        String priceString = price.toString().replace(',', '.');
        return Double.parseDouble(priceString);
    }
}
