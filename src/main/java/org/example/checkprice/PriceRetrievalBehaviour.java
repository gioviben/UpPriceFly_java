package org.example.checkprice;

import org.openqa.selenium.WebDriver;

public interface PriceRetrievalBehaviour {
    double retrievePrice(WebDriver driver);
}
