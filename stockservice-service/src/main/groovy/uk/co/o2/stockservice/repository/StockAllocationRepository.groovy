package uk.co.o2.stockservice.repository

import org.mongojack.DBCursor
import org.mongojack.JacksonDBCollection
import org.mongojack.WriteResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.o2.stockservice.model.StockAllocation

class StockAllocationRepository {
    private static final Logger LOG = LoggerFactory.getLogger(StockService)

    JacksonDBCollection<StockAllocation, String> stocksAllocationCollection

    StockAllocationRepository(JacksonDBCollection<StockAllocation, String> stocksAllocationCollection) {
        this.stocksAllocationCollection = stocksAllocationCollection
    }

    public String save(StockAllocation stockAllocationInfo) {
        WriteResult<StockAllocation, String> result = stocksAllocationCollection.save(stockAllocationInfo)
        result.getSavedId()
    }

    public StockAllocation findBySku(String sku) {
        DBCursor<StockAllocation> cursor = stocksAllocationCollection.find().is("sku", sku)
        findOne(cursor)
    }

    private StockAllocation findOne(DBCursor<StockAllocation> cursor) {
        cursor.hasNext() ? cursor.next() : null
    }
}


