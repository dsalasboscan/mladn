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

    def "given a matrix with an invalid Nucleoid throw InvalidNucleoidNameException"() {
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
