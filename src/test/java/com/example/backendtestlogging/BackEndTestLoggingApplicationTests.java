package com.example.backendtestlogging;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.backendtestlogging.controllers.ClientSideLoggerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackEndTestLoggingApplicationTests {

    @Autowired
    private ClientSideLoggerController clientSideLoggerController;

    @Test
    void contextLoads() {
        assertThat(clientSideLoggerController).isNotNull();
    }

}
