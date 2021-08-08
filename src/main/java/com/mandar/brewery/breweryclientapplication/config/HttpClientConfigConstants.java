package com.mandar.brewery.breweryclientapplication.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum HttpClientConfigConstants {
    MAX_ROUTE_CONNECTIONS(40),
    MAX_TOTAL_CONNECTIONS(40),
    MAX_LOCALHOST_CONNECTIONS(80),
    DEFAULT_KEEP_ALIVE_TIME(20000),
    CONNECTION_TIMEOUT(30000),
    REQUEST_TIMEOUT(30000),
    SOCKET_TIMEOUT(60000),
    IO_THREAD_COUNT(4),
    IDLE_CONNECTION_WAIT_TIME(30000);

    @Getter
    private int value;
}
