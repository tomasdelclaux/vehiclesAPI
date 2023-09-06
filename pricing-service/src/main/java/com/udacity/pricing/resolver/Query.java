package com.udacity.pricing.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.udacity.pricing.entity.Price;
import com.udacity.pricing.exception.PriceNotFoundException;
import com.udacity.pricing.repository.PriceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Query implements GraphQLQueryResolver{
    private PriceRepository priceRepository;

    public Query(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Price findPriceForVehicleId(Long vehicleId){
        Optional<Price> optionalPrice = priceRepository.findById(vehicleId);
        if(optionalPrice.isPresent()){
            return optionalPrice.get();
        }
        else{
            throw new PriceNotFoundException("price for this vehicle id was not found", vehicleId);
        }
    }
}
