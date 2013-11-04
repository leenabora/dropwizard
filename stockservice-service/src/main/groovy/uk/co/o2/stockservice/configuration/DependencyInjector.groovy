package uk.co.o2.stockservice.configuration

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoURI
import com.mongodb.WriteConcern
import org.mongojack.JacksonDBCollection
import uk.co.o2.json.schema.SchemaPassThroughCache
import uk.co.o2.services.mapper.BadTouchPointContextExceptionMapper
import uk.co.o2.services.mapper.IllegalStateExceptionMapper
import uk.co.o2.services.mapper.ItemNotFoundExceptionMapper
import uk.co.o2.services.mapper.ValidationExceptionMapper
import uk.co.o2.services.monitoring.MongoMonitor
import uk.co.o2.services.serialization.JacksonJsonSchemaValidatingProvider
import uk.co.o2.stockservice.domain.StockReservation
import uk.co.o2.stockservice.exceptionmapper.UnknownErrorExceptionMapper
import uk.co.o2.stockservice.filter.ChannelFilter
import uk.co.o2.stockservice.filter.HomeFilter
import uk.co.o2.stockservice.healthcheck.StockServiceHealthCheck
import uk.co.o2.stockservice.model.StockAllocation
import uk.co.o2.stockservice.model.StockMetadata
import uk.co.o2.stockservice.repository.StockAllocationRepository
import uk.co.o2.stockservice.repository.StockMetadataRepository
import uk.co.o2.stockservice.repository.StockReservationRepository
import uk.co.o2.stockservice.resources.HomeResource
import uk.co.o2.stockservice.resources.StockResource
import uk.co.o2.stockservice.resources.admin.AdminStockMetadataResource
import uk.co.o2.stockservice.resources.admin.AdminStockResource
import uk.co.o2.stockservice.service.StockService

import static org.mongojack.JacksonDBCollection.wrap

class DependencyInjector {

    public Map inject() {
        PropertyConfigurator propertyConfigurator = new PropertyConfigurator()

        MongoURI mongoURI = MongoURIFactory.getURI(propertyConfigurator.getValue("mongo.connectionString"))
        DB mongoDB = mongoURI.connectDB()
        mongoDB.setWriteConcern(WriteConcern.FSYNC_SAFE)

        MongoMonitor mongoMonitor = new MongoMonitor(mongoDB, mongoURI)

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

        StockService stockService = new StockService(stockAllocationRepository, stockReservationRepository, stockMetadataRepository)

        ObjectMapper mapper = new ObjectMapper()
        JsonFactory jsonFactory = new JsonFactory(mapper)
        SchemaPassThroughCache passThroughCache = new SchemaPassThroughCache(jsonFactory)

        BadTouchPointContextExceptionMapper badTouchPointContextExceptionMapper = new BadTouchPointContextExceptionMapper()
        ItemNotFoundExceptionMapper itemNotFoundExceptionMapper = new ItemNotFoundExceptionMapper()
        IllegalStateExceptionMapper illegalStateExceptionMapper = new IllegalStateExceptionMapper()
        ValidationExceptionMapper validationExceptionMapper = new ValidationExceptionMapper()
        UnknownErrorExceptionMapper unknownErrorExceptionMapper = new UnknownErrorExceptionMapper()
        JacksonJsonSchemaValidatingProvider jacksonJsonSchemaValidatingProvider = new JacksonJsonSchemaValidatingProvider(passThroughCache, mapper)

        HomeFilter homeFilter = new HomeFilter()
        ChannelFilter channelFilter = new ChannelFilter()

        HomeResource homeResource = new HomeResource()
        StockResource stockResource = new StockResource(stockService)
        AdminStockResource adminStockResource = new AdminStockResource(stockService, mapper)
        AdminStockMetadataResource adminStockMetadataResource = new AdminStockMetadataResource(stockService, mapper)

        StockServiceHealthCheck stockServiceHealthCheck = new StockServiceHealthCheck(mongoMonitor, PropertyConfigurator.getProperties())

        [

                "badTouchPointContextExceptionMapper": badTouchPointContextExceptionMapper,
                "itemNotFoundExceptionMapper": itemNotFoundExceptionMapper,
                "illegalStateExceptionMapper": illegalStateExceptionMapper,
                "validationExceptionMapper": validationExceptionMapper,
                "unknownErrorExceptionMapper": unknownErrorExceptionMapper,
                "jacksonJsonSchemaValidatingProvider": jacksonJsonSchemaValidatingProvider,

                "homeFilter": homeFilter,
                "channelFilter": channelFilter,

                "homeResource": homeResource,
                "stockResource": stockResource,
                "adminStockResource": adminStockResource,
                "adminStockMetadataResource": adminStockMetadataResource,

                "stockServiceHealthCheck": stockServiceHealthCheck
        ]

    }
}
