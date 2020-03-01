package com.davidsalas.repository;

import com.davidsalas.exception.custom.DatabaseException;
import com.davidsalas.model.Human;
import com.davidsalas.model.dto.StatDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DnaRepository {

  private NamedParameterJdbcTemplate jdbcTemplate;
  private RowMapper<StatDto> rowMapper;

  private static final Logger logger = LoggerFactory.getLogger(DnaRepository.class);

  public DnaRepository(NamedParameterJdbcTemplate jdbcTemplate, RowMapper<StatDto> rowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.rowMapper = rowMapper;
  }

  private final static String INSERT_QUERY = "INSERT INTO dbo.Dna(is_mutant) VALUES(:is_mutant)";
  private static final String STATS_QUERY = "SELECT SUM(is_mutant = 1) as mutants, SUM(is_mutant = 0) as humans, " +
      "ROUND((SUM(is_mutant = 1) * 100.0) / (COUNT(id) * 100.0), 2) as ratio FROM dbo.Dna";

  public void save(Human human) {
    try {
      jdbcTemplate.update(INSERT_QUERY, mapSqlParameterSource(human));
    } catch (DataAccessException exception) {
      logger.error("Error while saving human DNA to database, message: {}", exception.getMessage());
      throw new DatabaseException(exception.getMessage());
    }
  }

  public StatDto getStats() {
    StatDto statDto;
    try {
      statDto = jdbcTemplate.queryForObject(STATS_QUERY, new HashMap<>(), rowMapper);
    } catch (DataAccessException exception) {
      logger.error("Error while getting stats from database, message: {}", exception.getMessage());
      throw new DatabaseException(exception.getMessage());
    }
    return statDto;
  }

  private MapSqlParameterSource mapSqlParameterSource(Human human) {
    Map<String, Object> parameters = new HashMap<>(2);
    parameters.put("is_mutant", human.isMutant());
    return new MapSqlParameterSource(parameters);
  }
}