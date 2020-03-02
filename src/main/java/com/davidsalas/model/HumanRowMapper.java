package com.davidsalas.model;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class HumanRowMapper implements RowMapper<Human> {

  private static final String DNA_HASH = "dna_hash";
  private static final String IS_MUTANT = "is_mutant";

  @Override
  public Human mapRow(ResultSet rs, int rowNum) throws SQLException {
    Human human = new Human(rs.getString(DNA_HASH));
    human.setMutant(rs.getBoolean(IS_MUTANT));

    return human;
  }
}