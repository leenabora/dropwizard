package uk.co.o2.stockservice.repository

import org.mongojack.DBCursor
import org.mongojack.JacksonDBCollection
import org.mongojack.WriteResult
import uk.co.o2.stockservice.model.StockStatus
import uk.co.o2.stockservice.model.StockMetadata

class StockMetadataRepository {

    JacksonDBCollection<StockMetadata, String> stockMetadataCollection

    StockMetadataRepository(JacksonDBCollection<StockMetadata, String> stockMetadataCollection) {
        this.stockMetadataCollection = stockMetadataCollection
    }

    public String save(StockMetadata StockMetadataInfo) {
        WriteResult<StockMetadata, String> result = stockMetadataCollection.save(StockMetadataInfo)
        result.getSavedId()
    }

    public StockMetadata findByStockStatus(StockStatus status) {
        DBCursor<StockMetadata> cursor = stockMetadataCollection.find().is("sku", sku)
        findOne(cursor)
    }

    private void findOne(DBCursor<StockMetadata> cursor) {
        cursor.hasNext() ? cursor.next() : null
    }
}
