package uk.co.o2.stockservice.repository

import org.mongojack.JacksonDBCollection
import org.mongojack.WriteResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.o2.stockservice.domain.StockReservation

class StockReservationRepository {
    private static final Logger LOG = LoggerFactory.getLogger(StockReservationRepository)

    JacksonDBCollection<StockReservation, String> stockReservationCollection

    StockReservationRepository(JacksonDBCollection<StockReservation, String> stockReservationCollection) {
        this.stockReservationCollection = stockReservationCollection
    }

    public String save(StockReservation StockReservationInfo) {
        WriteResult<StockReservation, String> result = stockReservationCollection.save(StockReservationInfo)
        result.getSavedId()
    }

    public StockReservation findById(String id) {
        stockReservationCollection.findOneById(id)
    }

}
