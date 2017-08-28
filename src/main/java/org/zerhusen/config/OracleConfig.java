package org.zerhusen.config;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.support.ConnectionUsernameProvider;
import org.springframework.data.jdbc.support.oracle.ProxyDataSource;
import org.zerhusen.config.properties.OracleProperties;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class OracleConfig {

    private final ConnectionUsernameProvider provider;

    private final OracleProperties property;

    private OracleDataSource oracleDataSource;

    public OracleConfig(ConnectionUsernameProvider provider, OracleProperties property) throws SQLException {
        this.provider = provider;
        this.property = property;
        this.oracleDataSource = oracleDataSource();
    }

    private OracleDataSource oracleDataSource() throws SQLException {
        OracleDataSource dataSource = new OracleConnectionPoolDataSource();

        dataSource.setUser(property.getUsername());
        dataSource.setPassword(property.getPassword());
        dataSource.setURL(property.getUrl());
        dataSource.setImplicitCachingEnabled(true);
        dataSource.setFastConnectionFailoverEnabled(true);

        return dataSource;
    }

    /*@Bean
    @Profile({"dev", "uat", "prod"})
    public OracleDataSource jndiDataSource() {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();

        return (OracleDataSource) dataSourceLookup.getDataSource("java:comp/env/jdbc/datasource");
    }*/

    @Bean
    public DataSource dataSource() throws SQLException {
        return new ProxyDataSource(oracleDataSource, provider);
    }
}
