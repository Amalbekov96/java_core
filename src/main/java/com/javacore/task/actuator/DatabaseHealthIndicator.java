package com.javacore.task.actuator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return Health.up().build();
            } else {
                return Health.down().withDetail("Error", "Cannot establish a valid connection to the database").build();
            }
        } catch (SQLException e) {
            return Health.down().withDetail("Error", "Cannot connect to the database: " + e.getMessage()).build();
        }
    }
}

