package com.mandar.brewery.breweryclientapplication.client;

import com.mandar.brewery.breweryclientapplication.model.CustomerDto;
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
@ConfigurationProperties(value = "brewery.application.customer", ignoreUnknownFields = false)
public class CustomerClient implements RestTemplateService {

    @Value("${brewery.application.customer.path}")
    public String path;

    @Value("${brewery.application.customer.apiHost}")
    private String apiHost;

    @Override
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    /** Rest Template call to get a customer based on ID **/
    public CustomerDto getCustomerById(UUID customerId) {
        return getRestTemplate().getForObject(apiHost + path + customerId, CustomerDto.class);
    }

    /** Rest Template call to save a customer **/
    public URI saveCustomerDto(CustomerDto customerDto) {
        return getRestTemplate().postForLocation(apiHost + path , customerDto);
    }

    /** Rest Template call to update a customer **/
    public void updateCustomerDto(UUID customerId, CustomerDto customerDto) {
        getRestTemplate().put(apiHost + path + customerId, customerDto);
    }

    /** Rest Template call to delete a customer **/
    public void deleteCustomerDto(UUID customerId) {
        getRestTemplate().delete(apiHost + path + customerId);
    }
}
