package com.davidsalas.repository

import com.davidsalas.exception.custom.DatabaseException
import com.davidsalas.model.Human
import com.davidsalas.model.HumanRowMapper
import com.davidsalas.model.dto.StatDto
import com.davidsalas.model.dto.StatDtoRowMapper
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import spock.lang.Specification

import java.sql.SQLException

class DnaRepositorySpec extends Specification {

    DnaRepository dnaRepository
    NamedParameterJdbcTemplate jdbcTemplate = Mock()
    StatDtoRowMapper statDtoRowMapper = Mock()
    HumanRowMapper humanRowMapper = Mock()

    def setup() {
        dnaRepository = new DnaRepository(jdbcTemplate, statDtoRowMapper, humanRowMapper)
    }

    def "given a human save call jdbcTemplate update"() {
        given:
        Human human = new Human()

        when:
        dnaRepository.save(human)

        then:
        1 * jdbcTemplate.update(_ as String, _ as MapSqlParameterSource)
    }

    def "given a dataAccessException when save human is called, then return a controlled DataBaseException"() {
        given:
        Human human = new Human()

        jdbcTemplate.update(_ as String, _ as MapSqlParameterSource) >> {
            throw new UncategorizedSQLException("", "", new SQLException("", "", 20))
        }

        when:
        dnaRepository.save(human)

        then:
        thrown(DatabaseException)
    }

    def "given a human get call jdbcTemplate queryForObject and return the response"() {
        given:
        Human human = new Human("HASH")

        jdbcTemplate.queryForObject(_ as String, _ as MapSqlParameterSource, humanRowMapper) >> new Human("HASH")

        when:
        def response = dnaRepository.getHuman("HASH")

        then:
        response == human
    }

    def "given a dataAccessException when get human is called, then return a controlled DataBaseException"() {
        given:
        jdbcTemplate.queryForObject(_ as String, _ as MapSqlParameterSource, humanRowMapper) >> {
            throw new UncategorizedSQLException("", "", new SQLException("", "", 20))
        }

        when:
        dnaRepository.getHuman("HASH")

        then:
        thrown(DatabaseException)
    }

    def "given a EmptyResultDataAccessException when get human is called, then return null"() {
        given:
        jdbcTemplate.queryForObject(_ as String, _ as MapSqlParameterSource, humanRowMapper) >> {
            throw new EmptyResultDataAccessException(1)
        }

        when:
        def response = dnaRepository.getHuman("HASH")

        then:
        response == null
        noExceptionThrown()
    }

    def "when stats it's called return a StatDto"() {
        given:
        jdbcTemplate.queryForObject(_ as String, new HashMap<>(), statDtoRowMapper) >> new StatDto(8, 2, 0.8)

        when:
        def response = dnaRepository.stats

        then:
        response == new StatDto(8, 2, 0.8)
    }

    def "given a DataAccessException when stats it's called return the DatabaseException"() {
        given:
        jdbcTemplate.queryForObject(_ as String, _ as HashMap, statDtoRowMapper) >> {
            throw new UncategorizedSQLException("", "", new SQLException("", "", 20))
        }

        when:
        dnaRepository.stats

        then:
        thrown(DatabaseException)
    }
}