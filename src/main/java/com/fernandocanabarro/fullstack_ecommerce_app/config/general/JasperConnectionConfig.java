package com.fernandocanabarro.fullstack_ecommerce_app.config.general;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasperConnectionConfig {

    @Bean
    public Connection connection(DataSource dataSource) throws SQLException{
        return dataSource.getConnection();
    }

}
