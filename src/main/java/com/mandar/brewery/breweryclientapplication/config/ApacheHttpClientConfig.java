package com.mandar.brewery.breweryclientapplication.config;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@Slf4j
public class ApacheHttpClientConfig {

    @Bean
    public DefaultConnectingIOReactor getDefaultConnectingIOReactor() throws IOReactorException {
        DefaultConnectingIOReactor connectingIOReactor = new DefaultConnectingIOReactor(IOReactorConfig
                .custom()
                .setConnectTimeout(HttpClientConfigConstants.CONNECTION_TIMEOUT.getValue())
                .setIoThreadCount(HttpClientConfigConstants.IO_THREAD_COUNT.getValue())
                .setSoTimeout(HttpClientConfigConstants.SOCKET_TIMEOUT.getValue())
                .build());
        return connectingIOReactor;
    }

    @Bean
    public PoolingNHttpClientConnectionManager getPoolingNHttpClientConnectionManager() throws IOReactorException {
            PoolingNHttpClientConnectionManager poolingConnectionManager = new PoolingNHttpClientConnectionManager(getDefaultConnectingIOReactor());
        // set total amount of connections across all HTTP routes
        poolingConnectionManager.setMaxTotal(HttpClientConfigConstants.MAX_TOTAL_CONNECTIONS.getValue());

        // set maximum amount of connections for each http route in pool
        poolingConnectionManager.setDefaultMaxPerRoute(HttpClientConfigConstants.MAX_ROUTE_CONNECTIONS.getValue());

        // increase the amounts of connections if host is localhost
        HttpHost localhost = new HttpHost("http://localhost", 8080);
        poolingConnectionManager.setMaxPerRoute(new HttpRoute(localhost), HttpClientConfigConstants.MAX_LOCALHOST_CONNECTIONS.getValue());

        return poolingConnectionManager;
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return (httpResponse, httpContext) -> {
            HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
            HeaderElementIterator headerElementIterator = new BasicHeaderElementIterator(headerIterator);
            while(headerElementIterator.hasNext()){
                HeaderElement element = headerElementIterator.nextElement();
                String param = element.getName();
                String value = element.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    return Long.parseLong(value) * 1000; // convert to ms
                }
            }
            return HttpClientConfigConstants.DEFAULT_KEEP_ALIVE_TIME.getValue();
        };
    }

    @Bean
    public Runnable idleConnectionMonitor(PoolingNHttpClientConnectionManager connectionManager) {
        return new Runnable() {
            @Override
            @Scheduled(fixedDelay = 20000)
            public void run() {
                if(Objects.nonNull(connectionManager)) {
                    connectionManager.closeExpiredConnections();
                    connectionManager.closeIdleConnections(HttpClientConfigConstants.IDLE_CONNECTION_WAIT_TIME.getValue(), TimeUnit.MILLISECONDS);
                    log.info("Idle connection monitor: Closing expired and idle connections");
                }
            }
        };
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("idleMonitor");
        scheduler.setPoolSize(5);
        return scheduler;
    }

    @Bean
    public CloseableHttpAsyncClient httpAsyncClient() throws IOReactorException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(HttpClientConfigConstants.CONNECTION_TIMEOUT.getValue())
                .setConnectionRequestTimeout(HttpClientConfigConstants.REQUEST_TIMEOUT.getValue())
                .setSocketTimeout(HttpClientConfigConstants.SOCKET_TIMEOUT.getValue())
                .build();

       return HttpAsyncClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(getPoolingNHttpClientConnectionManager())
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .build();
    }
}
