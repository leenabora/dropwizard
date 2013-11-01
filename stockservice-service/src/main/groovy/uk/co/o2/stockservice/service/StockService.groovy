package uk.co.o2.stockservice.service

import uk.co.o2.stockservice.model.StockAllocation
import uk.co.o2.stockservice.model.StockSummary
import uk.co.o2.stockservice.repository.StockAllocationRepository
import uk.co.o2.stockservice.repository.StockMetadataRepository
import uk.co.o2.stockservice.repository.StockReservationRepository

class StockService {

    StockAllocationRepository stockAllocationRepository
    StockReservationRepository stockReservationRepository
    StockMetadataRepository stockMetadataRepository

    StockService(StockAllocationRepository stockAllocationRepository, StockReservationRepository stockReservationRepository, StockMetadataRepository stockMetadataRepository) {
        this.stockAllocationRepository = stockAllocationRepository
        this.stockReservationRepository = stockReservationRepository
        this.stockMetadataRepository = stockMetadataRepository
    }

    StockSummary getStockSummary(String sku, String channel) {
        println("sku is ${sku}")
        StockAllocation stockAllocation = stockAllocationRepository.findBySku(sku)
        if (stockAllocation) {
            StockSummary stockSummary = new StockSummary(sku: stockAllocation.sku, channel: channel, stockStatus: stockAllocation.stockStatus, stockLevel: stockAllocation.stockLevel)
            return stockSummary
        }
    }

    void allocateStock(StockAllocation stockAllocation) {
        stockAllocationRepository.save(stockAllocation)
    }


}
