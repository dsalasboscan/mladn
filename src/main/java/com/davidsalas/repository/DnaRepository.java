package com.davidsalas.repository;

import com.davidsalas.exception.custom.DatabaseException;
import com.davidsalas.model.Human;
import com.davidsalas.model.dto.StatDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DnaRepository {

  private NamedParameterJdbcTemplate jdbcTemplate;
  private RowMapper<StatDto> statDtoRowMapper;
  private RowMapper<Human> humanRowMapper;

  private static final Logger logger = LoggerFactory.getLogger(DnaRepository.class);

  public DnaRepository(NamedParameterJdbcTemplate jdbcTemplate, RowMapper<StatDto> statDtoRowMapper, RowMapper<Human> humanRowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.statDtoRowMapper = statDtoRowMapper;
    this.humanRowMapper = humanRowMapper;
  }

  private final static String INSERT_QUERY = "INSERT INTO dbo.Dna(is_mutant, dna_hash) VALUES(:is_mutant, :dna_hash)";
  private static final String STATS_QUERY = "SELECT SUM(is_mutant = 1) as mutants, SUM(is_mutant = 0) as humans, " +
      "ROUND((SUM(is_mutant = 1) * 100.0) / (COUNT(id) * 100.0), 2) as ratio FROM dbo.Dna";
  private static final String GET_HUMAN_QUERY = "SELECT dna_hash, is_mutant FROM dbo.Dna where dna_hash = :dna_hash";

  public void save(Human human) {
    try {
      jdbcTemplate.update(INSERT_QUERY, getStatsParams(human));
    } catch (DataAccessException e) {
      logger.error("Error while saving human DNA to database, message: {}", e.getMessage());
      throw new DatabaseException(e.getMessage());
    }
  }

  public StatDto getStats() {
    StatDto statDto;
    try {
      statDto = jdbcTemplate.queryForObject(STATS_QUERY, new HashMap<>(), statDtoRowMapper);
    } catch (DataAccessException e) {
      logger.error("Error while getting stats from database, message: {}", e.getMessage());
      throw new DatabaseException(e.getMessage());
    }
    return statDto;
  }

  public Human getHuman(String dnaHash) {
    Human human;
    try {
      human = jdbcTemplate.queryForObject(GET_HUMAN_QUERY, getHumanGetParams(dnaHash), humanRowMapper);
    } catch (EmptyResultDataAccessException e) {
      return null;
    } catch (DataAccessException e) {
      throw new DatabaseException(e.getMessage());
    }
    return human;
  }

  private MapSqlParameterSource getHumanGetParams(String dnaHash) {
    Map<String, Object> parameters = new HashMap<>(1);
    parameters.put("dna_hash", dnaHash);
    return new MapSqlParameterSource(parameters);
  }

  private MapSqlParameterSource getStatsParams(Human human) {
    Map<String, Object> parameters = new HashMap<>(2);
    parameters.put("is_mutant", human.isMutant());
    parameters.put("dna_hash", human.getDnaHash());
    return new MapSqlParameterSource(parameters);
  }
}