package com.davidsalas.service


import com.davidsalas.repository.DnaRepository
import spock.lang.Specification

class DnaServiceSpec extends Specification {

    DnaService dnaService

    TraverseService traverseService = Mock()
    DnaCacheService cacheService = Mock()
    InputValidatorService inputValidatorService = Mock()
    DnaRepository mutantRepository = Mock()

    def setup() {
        dnaService = new DnaService(traverseService, mutantRepository, cacheService, inputValidatorService)
    }

    def "given a 6x6 dna that is not in cache calls the traverse service and dont call get from cache, also return true when mutant couns > 1"() {
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
        cacheService.containsDna(_ as String) >> false

        when:
        boolean result = dnaService.isMutant(dna)

        then:
        0 * cacheService.get(_ as String)
        result
    }

    def "given a 4x4 dna that is not in cache calls the traverse service and dont call get from cache, also return true when mutant couns > 1"() {
        given:
        String[] dna = [
                "TTGC",
                "AAAA",
                "CCGT",
                "CTAG"
        ]
        traverseService.traverse(dna) >> 1
        cacheService.containsDna(_ as String) >> false

        when:
        boolean result = dnaService.isMutant(dna)

        then:
        0 * cacheService.get(_ as String)
        !result
    }

    def "given a 6x6 dna that is in cache return the cached response and dont call the traverse service"() {
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
        cacheService.containsDna(_ as String) >> true
        cacheService.get(_ as String) >> true

        when:
        boolean result = dnaService.isMutant(dna)

        then:
        0 * traverseService.traverse(dna)
        result
    }

    def "given a 4x4 dna that is in cache return the cached response and dont call the traverse service"() {
        given:
        String[] dna = [
                "TTGC",
                "AAAA",
                "CCGT",
                "CTAG"
        ]
        traverseService.traverse(dna) >> 1
        cacheService.containsDna(_ as String) >> true
        cacheService.get(_ as String) >> false

        when:
        boolean result = dnaService.isMutant(dna)

        then:
        0 * traverseService.traverse(dna)
        !result
    }
}