package com.davidsalas.service


import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class TraverseServiceSpec extends Specification {

    TraverseService traverseService
    DnaValidatorService geneticCodeValidatorService
    ExecutorService executorService
    AtomicInteger mutantGenes

    static final int MINIMUM_COUNT_TO_BE_A_MUTANT = 2

    def setup() {
        geneticCodeValidatorService = new DnaValidatorService()
        executorService = Executors.newCachedThreadPool()
        traverseService = new TraverseService(geneticCodeValidatorService, executorService)
        mutantGenes = new AtomicInteger()
    }

    def "given a 10 x 10 matrix, when call to the matrix traverse service, then get three DNA mutant matches"() {
        given:
        String[] dna = [
                "CTGCTACGAT",
                "TGAGATTAGG",
                "GCCACGACCC",
                "ATTGATCAAC",
                "CTCTTACGCG",
                "AGGCAAGATA",
                "TCTGCGTCCA",
                "TACAGCGTGT",
                "GTATGATCAC",
                "CGACACCATT"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 8 x 8 matrix, when call to the matrix traverse service, then get three DNA matches"() {
        given:
        String[] dna = [
                "TACACATG",
                "CGCCTCAC",
                "ATAATGAC",
                "CATCACTA",
                "GAGTCACC",
                "TACCTGAT",
                "ATCTCAAT",
                "CTAACGTG"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 6 x 6 matrix, when call to the matrix traverse service, then get less than one DNA mutant match"() {
        given:
        String[] dna = [
                "ATTCAA",
                "TCAGTT",
                "TGCCAG",
                "CCACTC",
                "ATAACA",
                "GACGTG"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response < MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 7 x 7 matrix, when call to the matrix traverse service, then get three DNA mutant matches"() {
        given:
        String[] dna = [
                "TATAGCG",
                "GCACTAC",
                "AAAATGT",
                "GCACAGT",
                "TCGCTGT",
                "CTTCGCT",
                "CAAGCTG"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 4 x 4 matrix, when call to the matrix traverse service, then get one mutant match"() {
        given:
        String[] dna = [
                "TTGC",
                "AAAA",
                "CCGT",
                "CTAG"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response < MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 12 x 12 matrix, when call to the matrix traverse service, then get 4 matches"() {
        given:
        String[] dna = [
                "CCACTCGTTGCA",
                "TATGGACGCTGC",
                "CGCCTTAAACTT",
                "AATAGCCTTCAG",
                "TACCGAGTGATC",
                "GTTATCCTGCAT",
                "CACATTTTACGC",
                "ACACACGCTCAA",
                "TGTCTAACACAG",
                "GAGAAGATAGTT",
                "CACGTTCTTCGT",
                "CTTACCAACCAC"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 6 x 6 matrix with overlap, when call to the matrix traverse service, then get two DNA mutant matches"() {
        given:
        String[] dna = [
                "CGTCTG",
                "TAGACT",
                "TGAAGT",
                "CGGATG",
                "GTCAAC",
                "TGCTCA"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 8 x 8 matrix, when call to the matrix traverse service, then got three DNA mutant matches"() {
        given:
        String[] dna = [
                "CCTCCTGA",
                "GCAAGTAC",
                "CTCTCTCG",
                "TGGCATGG",
                "GCTGCTCT",
                "CACAGTCA",
                "TCTGCTGC",
                "CAAAAAAG"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }


    def "given a 6 x 6 matrix, when call to the matrix traverse service, then get three DNA mutant matches"() {
        given:
        String[] dna = [
                "GTTAAG",
                "CAAAAT",
                "ACGTAC",
                "GATCAG",
                "CTCTCT",
                "TGATAC"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 4 x 4 matrix, when call to the matrix traverse service, then get one DNA mutant match"() {
        given:
        String[] dna = [
                "ACTG",
                "GCAT",
                "TCGC",
                "ACCC"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response < MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 7 x 7 matrix, when call to the matrix traverse service, then get four DNA mutant matches"() {
        given:
        String[] dna = [
                "TCGAAAA",
                "ACTGCTC",
                "CACAGGT",
                "TCGCTAG",
                "TGACCTC",
                "TTTTACA",
                "TAGCGTA"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 5 x 5 matrix, then get two DNA mutant matches one horizontal and one diagonal"() {
        given:
        String[] dna = [
                "AAAAT",
                "CCTGC",
                "TCGCA",
                "GGAGC",
                "GCCTA"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 5 x 5 matrix, then get two DNA mutant matches, one horizontal and one vertical"() {
        given:
        String[] dna = [
                "GCCCC",
                "TGTTG",
                "TCAAC",
                "TGCTT",
                "TTCAC"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }


    def "given a 6 x 6 matrix, when call to the matrix traverse service, then get two DNA mutant matches"() {
        given:
        String[] dna = [
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }

    def "given a 4 X 4 matrix, when call to the matrix traverse service, then get more than one dna match"() {
        given:
        String[] dna = [
                "AAAA",
                "AAAA",
                "AATT",
                "CCGG"
        ]

        when:
        def response = traverseService.traverseMatrix(dna)

        then:
        response >= MINIMUM_COUNT_TO_BE_A_MUTANT
        noExceptionThrown()
    }
}