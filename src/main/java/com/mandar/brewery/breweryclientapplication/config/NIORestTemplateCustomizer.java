package com.mandar.brewery.breweryclientapplication.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NIORestTemplateCustomizer implements RestTemplateCustomizer {

  private final CloseableHttpAsyncClient httpAsyncClient;

  public NIORestTemplateCustomizer(CloseableHttpAsyncClient httpAsyncClient) {
    this.httpAsyncClient = httpAsyncClient;
  }

  @Override
  public void customize(RestTemplate restTemplate) {
    try {
      restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    } catch (IOReactorException e) {
      e.printStackTrace();
    }
  }

  private HttpComponentsAsyncClientHttpRequestFactory  clientHttpRequestFactory() throws IOReactorException {
    ClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsAsyncClientHttpRequestFactory();
    ((HttpComponentsAsyncClientHttpRequestFactory) clientHttpRequestFactory).setHttpClient((HttpClient) httpAsyncClient);
    return new HttpComponentsAsyncClientHttpRequestFactory(httpAsyncClient);
  }
}
