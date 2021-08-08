package com.mandar.brewery.breweryclientapplication.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mandar.brewery.breweryclientapplication.model.BeerDto;
import java.net.URI;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class BreweryClientTest {

  @Autowired
  BreweryClient breweryClient;

  @Test
  void testGetBeerById() {
    BeerDto beerDto = breweryClient.getBeerById(UUID.randomUUID());
    assertNotNull(beerDto);
  }

  @Test
  void saveBeerDtoTest() {
    BeerDto beerDto = BeerDto.builder().beerName("New Beer").build();
    URI uri = breweryClient.saveBeerDto(beerDto);
    log.info("The URI is : {} ", uri.toString());
    assertNotNull(uri);
  }

  @Test
  void updateBeerDtoTest() {
    BeerDto beerDto = BeerDto.builder().beerName("New Beer").build();
    breweryClient.updateBeerDto(UUID.randomUUID(), beerDto);
  }

  @Test
  void deleteBeerDtoTest() {
    breweryClient.deleteBeerDto(UUID.randomUUID());
  }
}