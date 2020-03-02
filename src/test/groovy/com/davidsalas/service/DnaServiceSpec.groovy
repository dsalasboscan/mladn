package com.davidsalas.service


import com.davidsalas.repository.DnaRepository
import spock.lang.Specification

class DnaServiceSpec extends Specification {

    DnaService dnaService

    TraverseService traverseService = Mock()
    InputValidatorService inputValidatorService = Mock()
    DnaRepository mutantRepository = Mock()

    def setup() {
        dnaService = new DnaService(traverseService, mutantRepository, inputValidatorService)
    }

    def "given a 6x6 return true when mutant count > 1"() {
        given:
        String[] dna = [
                "CGTCTG",
                "TAGACT",
                "TGAAGT",
                "CGGATG",
                "GTCAAC",
                "TGCTCA"
        ]
        traverseService.traverse(dna) >> 2

        when:
        boolean result = dnaService.isMutant(dna)

        then:
        result
    }

    def "given a 4x4 dna return true when mutant count < 1"() {
        given:
        String[] dna = [
                "TTGC",
                "AAAA",
                "CCGT",
                "CTAG"
        ]
        traverseService.traverse(dna) >> 1

        when:
        boolean result = dnaService.isMutant(dna)

        then:
        !result
    }
}