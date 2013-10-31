package uk.co.o2.stockservice.model

class StockSummary {
    private final long id;

    private final String sku;
    private final String channel;
    private final String stockStatus;
    private final Integer stockLevel;

    StockSummary(long id, String sku, String channel, String stockStatus, Integer stockLevel) {
        this.id = id
        this.sku = sku
        this.channel = channel
        this.stockStatus = stockStatus
        this.stockLevel = stockLevel
    }

    long getId() {
        return id
    }

    String getSku() {
        return sku
    }

    String getChannel() {
        return channel
    }

    String getStockStatus() {
        return stockStatus
    }

    Integer getStockLevel() {
        return stockLevel
    }
}
