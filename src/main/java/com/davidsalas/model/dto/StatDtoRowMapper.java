package com.davidsalas.model.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StatDtoRowMapper implements RowMapper<StatDto> {

  private static final String MUTANTS_COUNT = "mutants";
  private static final String HUMANS_COUNT = "humans";
  private static final String RATIO = "ratio";

  @Override
  public StatDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new StatDto(
        rs.getLong(MUTANTS_COUNT),
        rs.getLong(HUMANS_COUNT),
        rs.getDouble(RATIO)
    );
  }
}