package com.davidsalas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

  @PostConstruct
  public void setUpDatabases() {
    DatabasePopulatorUtils.execute(this.createDatabasePopulator(new String[]{"sql-scripts/db-ddl.sql"}), dataSource());
  }

  @Bean
  @ConfigurationProperties(prefix = "datasource.h2.dna")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
    return new NamedParameterJdbcTemplate(jdbcTemplate);
  }

  private ResourceDatabasePopulator createDatabasePopulator(final String[] files) {
    final ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();

    for (final String file : files) {
      resourceDatabasePopulator.addScript(new ClassPathResource(file));
    }

    return resourceDatabasePopulator;
  }
}