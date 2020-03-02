package com.davidsalas.service

import com.davidsalas.exception.custom.ArrayDimensionsException
import com.davidsalas.exception.custom.InvalidNucleoidNameException
import spock.lang.Specification

class InputValidatorServiceSpec extends Specification {

    InputValidatorService inputValidatorService

    def setup() {
        inputValidatorService = new InputValidatorService()
    }

    def "given a matrix with valid nucleoids, then don't throw exception"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGGTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        noExceptionThrown()
    }

    def "given a matrix with an invalid Nucleoid throw ExecutionException"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGZTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        thrown(InvalidNucleoidNameException)
    }

    def "given a matrix with an invalid Nucleoid in lowercase throw InvalidNucleoidNameException"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGzTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        thrown(InvalidNucleoidNameException)
    }

    def "given a matrix with an invalid Nucleoid in the top half throw InvalidNucleoidNameException"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCZ",
                "AAGGTTCC",
                "AAGGATCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        thrown(InvalidNucleoidNameException)
    }

    def "given a matrix with an invalid Nucleoid in the top half on left throw InvalidNucleoidNameException"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "HAGGTTCG",
                "AAGGTTCC",
                "AAGGATCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        thrown(InvalidNucleoidNameException)
    }

    def "given a matrix with an invalid Nucleoid in the bottom half on left throw InvalidNucleoidNameException"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCG",
                "AAGGTTCC",
                "AAGGATCC",
                "AAGGTTCC",
                "PAGGTTCC",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        thrown(InvalidNucleoidNameException)
    }

    def "given a matrix with an invalid Nucleoid in the bottom half on right throw InvalidNucleoidNameException"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCG",
                "AAGGTTCC",
                "AAGGATCC",
                "AAGGTTCC",
                "GAGGTTCH",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        thrown(InvalidNucleoidNameException)
    }

    def "given a matrix that is NxN then don't throw exception"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGGTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        noExceptionThrown()
    }

    def "given a matrix that is MxN then don't throw exception"() {
        given:
        String[] dna = [
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGGTCC",
                "AAGGTTCC",
                "AAGGTTCC",
                "AAGGTTCC",
        ]

        when:
        inputValidatorService.validate(dna)

        then:
        thrown(ArrayDimensionsException)
    }
}
