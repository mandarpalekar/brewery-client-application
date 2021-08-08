package com.mandar.brewery.breweryclientapplication.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

//@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

  @Value("${connectionmanager.maxtotalconnections}")
  private final Integer maxTotalConnections;

  @Value("${connectionmanager.defaultmaxtotalconnections}")
  private final Integer defaultMaxTotalConnections;

  @Value("${requestconfig.requesttimeout}")
  private final Integer requestTimeOut;

  @Value("${requestconfig.sockettimeout}")
  private final Integer socketTimeOut;

  public BlockingRestTemplateCustomizer(
      @Value("${connectionmanager.maxtotalconnections}") Integer maxTotalConnections,
      @Value("${connectionmanager.defaultmaxtotalconnections}") Integer defaultMaxTotalConnections,
      @Value("${requestconfig.requesttimeout}") Integer requestTimeOut,
      @Value("${requestconfig.sockettimeout}") Integer socketTimeOut) {

    this.maxTotalConnections = maxTotalConnections;
    this.defaultMaxTotalConnections = defaultMaxTotalConnections;
    this.requestTimeOut = requestTimeOut;
    this.socketTimeOut = socketTimeOut;
  }

  public ClientHttpRequestFactory clientHttpRequestFactory() {

    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(maxTotalConnections);
    connectionManager.setDefaultMaxPerRoute(defaultMaxTotalConnections);

    RequestConfig requestConfig = RequestConfig
        .custom()
        .setConnectionRequestTimeout(requestTimeOut)
        .setSocketTimeout(socketTimeOut)
        .build();

    CloseableHttpClient closeableHttpClient = HttpClients
        .custom()
        .setConnectionManager(connectionManager)
        .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
        .setDefaultRequestConfig(requestConfig)
        .build();

    /** Return spring's implementation of the HTTP Client **/
    return new HttpComponentsClientHttpRequestFactory(closeableHttpClient);
  }

  @Override
  public void customize(RestTemplate restTemplate) {
    try {
      restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
