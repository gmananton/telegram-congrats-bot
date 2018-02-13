package com.gman.telegram.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Anton Mikhaylov on 13.02.2018.
 * Пингует наше приложение, чтобы бот в heroku не засыпал
 */
@Service
@Slf4j
public class PingService {

    @Value("${server.port}")
    private int port;


    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 */5 * * * *")
    public void ping() {
        RestTemplate client = new RestTemplate();
        log.info("Checking health: " + client.getForObject("http://localhost:" + port + "/health", String.class));
    }
}
