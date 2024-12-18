package org.example.checkprice;

public class InfoPrice {
    private double actualPrice;
    private double lastPrice;
    private double delta;
    private boolean isDecrease;
    private String lastPriceFilePath;

    public String getLastPriceFilePath() {
        return lastPriceFilePath;
    }

    public void setLastPriceFilePath(String lastPriceFilePath) {
        this.lastPriceFilePath = lastPriceFilePath;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public boolean isDecrease() {
        return isDecrease;
    }

    public void setDecrease(boolean decrease) {
        isDecrease = decrease;
    }

}
