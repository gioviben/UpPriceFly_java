package org.example.checkprice;

import org.example.Flight;
import org.example.util.FileUtil;
import org.example.util.LogPrint;
import org.example.MessageBehaviour;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckPrice {

    private String userName;
    private PriceRetrievalFactory priceRetrievalFactory;

    public CheckPrice(String userName) {
        this.userName = userName;
        this.priceRetrievalFactory = new PriceRetrievalFactory();
    }

    public void checkPrice(Flight flight, MessageBehaviour messageManager, String lastPriceFilePath) {

        PriceRetrievalBehaviour priceRetrievalBehaviour = priceRetrievalFactory.getBehaviour(flight.getCompany());
        double actualPrice = priceRetrievalBehaviour.retrievePrice(flight.getDriver());
        InfoPrice infoPrice = new InfoPrice();
        infoPrice.setActualPrice(actualPrice);
        infoPrice.setLastPriceFilePath(lastPriceFilePath);
        checkLowestPrice(flight, messageManager, infoPrice);
    }

    private void checkLowestPrice(Flight flight, MessageBehaviour messageManager, InfoPrice infoPrice) {

        double lastPrice;
        double actualPrice = infoPrice.getActualPrice();
        String lastPriceFilePath = infoPrice.getLastPriceFilePath();
        try {
            lastPrice = Double.parseDouble(FileUtil.getLine(lastPriceFilePath));
            infoPrice.setLastPrice(lastPrice);
        } catch (Exception e) {
            LogPrint.printRed("ERROR", String.format("user=%s company=%s link=%s \n\tError reading from %s: %s",
                    userName, flight.getCompany(), flight.getLink(), lastPriceFilePath, e.getClass().getSimpleName()));
            throw new RuntimeException(e);
        }

        if (actualPrice < lastPrice) {
            handlePriceDecrease(flight, messageManager, infoPrice);
        } else if (actualPrice > lastPrice) {
            handlePriceIncrease(flight, messageManager, infoPrice);
        } else {
            LogPrint.printUncolored("INFO", String.format("user=%s company=%s link=%s\n\tThe price has not changed\n\tactual_price=%s == %s=last_price", userName, flight.getCompany(), flight.getLink(), actualPrice, lastPrice));
        }
    }

    private void handlePriceDecrease(Flight flight, MessageBehaviour messageManager, InfoPrice infoPrice) {

        double lastPrice = infoPrice.getLastPrice();
        double actualPrice = infoPrice.getActualPrice();
        infoPrice.setDecrease(true);
        double delta = Math.round((lastPrice - actualPrice) * 100.0) / 100.0;
        infoPrice.setDelta(delta);
        LogPrint.printUncolored("INFO", String.format("user=%s company=%s link=%s \n\tPrice decreased by %.2f€ since last check", userName, flight.getCompany(), flight.getLink(), delta));

        try {
            sendMessage(flight, messageManager, infoPrice);
            FileUtil.updateFileValue(infoPrice.getLastPriceFilePath(), String.valueOf(actualPrice));
        } catch (Exception e) {
            handleError(e, flight, infoPrice.getLastPriceFilePath());
        }
    }

    private void handlePriceIncrease(Flight flight, MessageBehaviour messageManager, InfoPrice infoPrice) {

        double lastPrice = infoPrice.getLastPrice();
        double actualPrice = infoPrice.getActualPrice();
        infoPrice.setDecrease(false);
        double delta = Math.round((actualPrice - lastPrice) * 100.0) / 100.0;
        infoPrice.setDelta(delta);
        LogPrint.printUncolored("INFO", String.format("user=%s company=%s link=%s \n\tPrice increased by %.2f€ since last check", userName, flight.getCompany(), flight.getLink(), delta));

        try {
            sendMessage(flight, messageManager, infoPrice);
            FileUtil.updateFileValue(infoPrice.getLastPriceFilePath(), String.valueOf(actualPrice));
        } catch (Exception e) {
            handleError(e, flight, infoPrice.getLastPriceFilePath());
        }
    }

    private void sendMessage(Flight flight, MessageBehaviour messageManager, InfoPrice infoPrice) {
        double lastPrice = infoPrice.getLastPrice();
        double actualPrice = infoPrice.getActualPrice();
        boolean isDecrease = infoPrice.isDecrease();
        double delta = infoPrice.getDelta();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String message = String.format("Ultima verifica: %s\nCompany = %s\n<a href=\"%s\">Flight Link</a>\nPrezzo " + "corrente %.2f eur\nIl prezzo e' %s di %.2f eur dall'ultima rilevazione (%.2f eur)", now, flight.getCompany(), flight.getLink(), actualPrice, (isDecrease ? "sceso" : "aumentato"), delta, lastPrice);

        messageManager.setMessage(message);
        messageManager.sendMessage();
    }

    private void handleError(Exception e, Flight flight, String filePath) {
        String errorMsg = String.format("user=%s company=%s link=%s \n\tError writing to %s: %s", userName, flight.getCompany(), flight.getLink(), filePath, e.getClass().getSimpleName());
        LogPrint.printRed("ERROR", errorMsg);
        throw new RuntimeException(e);
    }
}

