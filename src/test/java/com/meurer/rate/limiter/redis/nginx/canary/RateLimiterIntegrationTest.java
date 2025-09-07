package com.meurer.rate.limiter.redis.nginx.canary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimiterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTooManyRequestsAfterLimit() throws Exception {
        int limit = 10; // ou o valor configurado em application.properties
        for (int i = 0; i < limit; i++) {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk());
        }
        // Próxima requisição deve ser bloqueada
        mockMvc.perform(get("/"))
                .andExpect(status().isTooManyRequests());
    }
}