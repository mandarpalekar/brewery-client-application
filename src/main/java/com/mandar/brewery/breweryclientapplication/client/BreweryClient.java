package com.mandar.brewery.breweryclientapplication.client;

import com.mandar.brewery.breweryclientapplication.model.BeerDto;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Setter
@RequiredArgsConstructor
public class BreweryClient {

    @Value("${brewery.application.path}")
    private String path;

    @Value("${brewery.application.apiHost}")
    private String apiHost;

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    /** Rest Template call to get a beer based on ID **/
    public BeerDto getBeerById(UUID beerId) {
        return restTemplate.getForObject(apiHost + path + beerId, BeerDto.class);
    }

    /** Rest Template call to save a beer **/
    public URI saveBeerDto(BeerDto beerDto) {
        return restTemplate.postForLocation(apiHost + path , beerDto);
    }

    /** Rest Template call to update a beer **/
    public void updateBeerDto(UUID beerId, BeerDto beerDto) {
        restTemplate.put(apiHost + path + beerId, beerDto);
    }

    /** Rest Template call to delete a beer **/
    public void deleteBeerDto(UUID beerid) {
        restTemplate.delete(apiHost + path + beerid);
    }

}
