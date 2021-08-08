package com.mandar.brewery.breweryclientapplication.client;

import com.mandar.brewery.breweryclientapplication.model.BeerDto;
import com.mandar.brewery.breweryclientapplication.service.RestTemplateService;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(value = "brewery.application.beer", ignoreUnknownFields = false)
public class BreweryClient implements RestTemplateService {

    @Value("${brewery.application.beer.path}")
    public String path;

    @Value("${brewery.application.beer.apiHost}")
    private String apiHost;

    /** Rest Template call to get a beer based on ID **/
    public BeerDto getBeerById(UUID beerId) {
        return getRestTemplate().getForObject(apiHost + path + beerId, BeerDto.class);
    }

    /** Rest Template call to save a beer **/
    public URI saveBeerDto(BeerDto beerDto) {
        return getRestTemplate().postForLocation(apiHost + path , beerDto);
    }

    /** Rest Template call to update a beer **/
    public void updateBeerDto(UUID beerId, BeerDto beerDto) {
        getRestTemplate().put(apiHost + path + beerId, beerDto);
    }

    /** Rest Template call to delete a beer **/
    public void deleteBeerDto(UUID beerid) {
        getRestTemplate().delete(apiHost + path + beerid);
    }

    @Override
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
