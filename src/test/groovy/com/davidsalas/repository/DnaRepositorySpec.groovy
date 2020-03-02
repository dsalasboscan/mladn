package com.davidsalas.repository

import com.davidsalas.exception.custom.DatabaseException
import com.davidsalas.model.Human
import com.davidsalas.model.dto.StatDto
import com.davidsalas.model.dto.StatDtoRowMapper
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import spock.lang.Specification

import java.sql.SQLException

class DnaRepositorySpec extends Specification {

    DnaRepository mutantRepository
    NamedParameterJdbcTemplate jdbcTemplate = Mock()
    StatDtoRowMapper statDtoRowMapper = Mock()


    def setup() {
        mutantRepository = new DnaRepository(jdbcTemplate, statDtoRowMapper)
    }

    def "given a human call jdbcTemplate.update"() {
        given:
        Map<String, Object> parameters = new HashMap<>()
        parameters.put("is_mutant", false)

        Human human = new Human()

        when:
        mutantRepository.save(human)

        then:
        1 * jdbcTemplate.update(_ as String, _ as MapSqlParameterSource)
        noExceptionThrown()
    }

    def "given a dataAccessException when save dna is called, then return a controlled DataBaseException"() {
        given:
        Human human = new Human()

        jdbcTemplate.update(_ as String, _ as MapSqlParameterSource) >> {
            throw new UncategorizedSQLException("", "", new SQLException("", "", 20))
        }

        when:
        mutantRepository.save(human)

        then:
        thrown(DatabaseException)
    }

    def "when stats it's called return a StatDto"() {
        given:
        jdbcTemplate.queryForObject(_ as String, new HashMap<>(), statDtoRowMapper) >> new StatDto(8, 2, 0.8)

        when:
        def response = mutantRepository.stats

        then:
        response == new StatDto(8, 2, 0.8)
    }

    def "given a DataAccessException when stats it's called return the DatabaseException"() {
        given:
        jdbcTemplate.queryForObject(_ as String, _ as HashMap, statDtoRowMapper) >> {
            throw new UncategorizedSQLException("", "", new SQLException("", "", 20))
        }

        when:
        mutantRepository.stats

        then:
        thrown(DatabaseException)
    }
}