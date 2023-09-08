package com.udacity.vehicles.client.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a class to interface with the Pricing Client for price data.
 */
@Component
public class PriceClient {

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

    private final WebClient client;

    public PriceClient(WebClient pricing) {
        this.client = pricing;
    }

    // In a real-world application we'll want to add some resilience
    // to this method with retries/CB/failover capabilities
    // We may also want to cache the results so we don't need to
    // do a request every time
    /**
     * Gets a vehicle price from the pricing client, given vehicle ID.
     * @param vehicleId ID number of the vehicle for which to get the price
     * @return Currency and price of the requested vehicle,
     *   error message that the vehicle ID is invalid, or note that the
     *   service is down.
     */
    public String getPrice(Long vehicleId) {
        String query = "{findPriceForVehicleId(vehicleid:"+vehicleId+") {price}}";
        String resourceUrl = "http://localhost:"+8762+"/graphql";
        System.out.println("sending graphql request");
        try {
            String price = client
                    .post()
                    .uri(resourceUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData("query","{findPriceForVehicleId(vehicleid:"+vehicleId+") {currency,price,vehicleid}}"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();


            System.out.println("Printing response !!");
            System.out.println(price);
            return String.format("%s %s", getCurrency(price), getPrice(price));

        } catch (Exception e) {
            System.out.println(e);
            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
        }
        return "(consult price)";
    }

    public String getCurrency(String response){
        Pattern pattern = Pattern.compile("currency\":\"(.*?)\",");
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()){
            System.out.println(matcher.group(1));
            return matcher.group(1);
        }
        else{
            return null;
        }
    }

    public String getPrice(String response){
        Pattern pattern = Pattern.compile("price\":(.*?),");
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()){
            System.out.println(matcher.group(1));
            return matcher.group(1);
        }
        else{
            return null;
        }
    }
}
