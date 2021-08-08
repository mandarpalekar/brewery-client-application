package com.mandar.brewery.breweryclientapplication.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mandar.brewery.breweryclientapplication.model.CustomerDto;
import java.net.URI;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CustomerClientTest {

    @Autowired
    CustomerClient customerClient;

    @Test
    void getCustomerById() {
        CustomerDto customerDto = customerClient.getCustomerById(UUID.randomUUID());
        assertNotNull(customerDto);
    }

    @Test
    void saveCustomerDto() {
        CustomerDto customerDto = CustomerDto.builder().name("John").build();
        URI uri = customerClient.saveCustomerDto(customerDto);
        log.info("The URI is : {} ", uri.toString());
        assertNotNull(uri);
    }

    @Test
    void updateCustomerDto() {
        CustomerDto customerDto = CustomerDto.builder().name("New John").build();
        customerClient.updateCustomerDto(UUID.randomUUID(), customerDto);
    }

    @Test
    void deleteBeerDto() {
        customerClient.deleteCustomerDto(UUID.randomUUID());
    }
}