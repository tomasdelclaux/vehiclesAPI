package com.udacity.vehicles.client.prices;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Data
public class PriceResponse {

    private Response response;

    @Data
    public class Response{
        private Price price;

        @Data
        public class Price {
            private String currency;
            private BigDecimal price;
            private Long vehicleId;

        }
    }
}
