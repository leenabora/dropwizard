package uk.co.o2.stockservice.repository

import org.mongojack.JacksonDBCollection
import org.mongojack.WriteResult
import uk.co.o2.stockservice.domain.StockReservation

class StockReservationRepository {

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
