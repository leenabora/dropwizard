package uk.co.o2.stockservice.configuration

import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoURI
import org.mongojack.JacksonDBCollection
import uk.co.o2.stockservice.domain.StockReservation
import uk.co.o2.stockservice.model.StockAllocation
import uk.co.o2.stockservice.model.StockMetadata
import uk.co.o2.stockservice.repository.StockAllocationRepository
import uk.co.o2.stockservice.repository.StockMetadataRepository
import uk.co.o2.stockservice.repository.StockReservationRepository
import uk.co.o2.stockservice.service.StockService

import static org.mongojack.JacksonDBCollection.wrap

class DependencyInjector {

    public StockService inject() {
        PropertyConfigurator propertyConfigurator = new PropertyConfigurator()


        MongoURI mongoURI = MongoURIFactory.getURI(propertyConfigurator.getValue("mongo.connectionString"))
        DB mongoDB = mongoURI.connectDB()
        mongoDB.setWriteConcern(com.mongodb.WriteConcern.FSYNC_SAFE)

        DBCollection stockAllocationCollection = mongoDB.getCollection("stockAllocation")
        DBCollection stockReservationCollection = mongoDB.getCollection("stockReservation")
        DBCollection stockMetadataCollection = mongoDB.getCollection("stockMetadata")


        JacksonDBCollection<StockAllocation, String> stocksAllocationJacksonCollection = wrap(stockAllocationCollection, StockAllocation.class,
                String.class);

        JacksonDBCollection<StockReservation, String> stockReservationJacksonCollection = wrap(stockReservationCollection, StockReservation.class,
                String.class);

        JacksonDBCollection<StockMetadata, String> stockMetadataJacksonCollection = wrap(stockMetadataCollection, StockMetadata.class,
                String.class);


        StockAllocationRepository stockAllocationRepository = new StockAllocationRepository(stocksAllocationJacksonCollection)
        StockReservationRepository stockReservationRepository = new StockReservationRepository(stockReservationJacksonCollection)
        StockMetadataRepository stockMetadataRepository = new StockMetadataRepository(stockMetadataJacksonCollection)

        new StockService(stockAllocationRepository, stockReservationRepository, stockMetadataRepository)
    }
}
