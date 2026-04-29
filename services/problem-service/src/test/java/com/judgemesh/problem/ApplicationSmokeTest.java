package com.judgemesh.problem;

import org.junit.jupiter.api.Test;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test: boots the problem-service ApplicationContext and verifies
 * /actuator/health responds 200 with status UP. MySQL / Flyway / Redis
 * (Spring Data + Redisson) are autoconfigure-excluded to avoid needing
 * any backing infra.
 */
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.nacos.discovery.enabled=false",
                "spring.cloud.nacos.discovery.register-enabled=false",
                "spring.cloud.nacos.config.enabled=false",
                "spring.cloud.nacos.config.import-check.enabled=false",
                "spring.cloud.config.import-check.enabled=false",
                "spring.cloud.service-registry.auto-registration.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "spring.config.import=",
                "spring.flyway.enabled=false",
                "eureka.client.enabled=false"
        }
)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        FlywayAutoConfiguration.class,
        RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class,
        RedissonAutoConfigurationV2.class
})
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.import=")
class ApplicationSmokeTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void healthEndpointReturnsUp() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }
}
