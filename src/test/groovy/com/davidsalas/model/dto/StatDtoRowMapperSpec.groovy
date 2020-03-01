package com.davidsalas.model.dto


import spock.lang.Specification

import java.sql.ResultSet

class StatDtoRowMapperSpec extends Specification {

    ResultSet rs = Mock()
    StatDtoRowMapper rowMapper

    def setup() {
        rowMapper = new StatDtoRowMapper()
    }

    def "given a resultSet returns a StatDto"() {
        given:
        rs.getLong("mutants") >> 8
        rs.getLong("humans") >> 2
        rs.getDouble("ratio") >> 0.8D

        when:
        def response = rowMapper.mapRow(rs, 0)

        then:
        response == new StatDto(8, 2, 0.8D)
    }
}