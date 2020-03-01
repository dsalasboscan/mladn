package com.davidsalas.service

import com.davidsalas.model.DiagonalValidationDirection
import com.davidsalas.util.Counter
import spock.lang.Specification

class DnaValidatorServiceSpec extends Specification {

    DnaValidatorService dnaValidatorService

    def setup() {
        dnaValidatorService = new DnaValidatorService()
    }

    def "given a 10 x 10 dna find 3 mutant genes on it"() {
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
        Counter mutantCounter = new Counter(0)
        Counter diagonalCounter = new Counter(1)

        when:
        for (int row = 4; row > 1; row++) {
            for (int col = 5; col > 1; col--) {
                dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.LEFT_UP, diagonalCounter, mutantCounter)
                row--
            }
        }

        for (int row = 4; dna.length - row > 1; row++) {
            for (int col = 7; col > 3; col--) {
                dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.LEFT_DOWN, diagonalCounter, mutantCounter)
                row++
            }
        }

        for (int row = 4; dna.length - row > 1; row++) {
            for (int col = 2; col < 6; col++) {
                dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.RIGHT_DOWN, diagonalCounter, mutantCounter)
            }
        }

        then:
        mutantCounter.getValue() == 3
    }

    def "given a 8 x 8 dna find a mutant on the center diagonal of the matrix"() {
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
        Counter mutantCounter = new Counter(0)
        Counter diagonalCounter = new Counter(1)

        when:
        for (int row = 2; dna.length - row > 1; row++) {
            for (int col = 1; col < 5; col++) {
                dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.RIGHT_DOWN, diagonalCounter, mutantCounter)
                row++;
            }
        }

        then:
        mutantCounter.getValue() == 1
    }

    def "given a 6 x 6 matrix find 3 mutants"() {
        given:
        String[] dna = [
                "GTTAAG",
                "CAAAAT",
                "ACGTAC",
                "GATCAG",
                "CTCTCT",
                "TGATAC"
        ]
        Counter mutantCounter = new Counter(0)
        Counter diagonalCounter = new Counter(1)
        Counter horizontalCounter = new Counter(1)
        Counter verticalCounter = new Counter(1)

        when:
        for (int row = dna.length - 1; row > 1; row++) {
            for (int col = 0; col < 5; col++) {
                dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.RIGHT_UP, diagonalCounter, mutantCounter)
                row--
            }
        }

        for (int col = 1; col < 5; col++) {
            dnaValidatorService.validateHorizontal(dna, 1, col, horizontalCounter, mutantCounter)
        }

        for (int row = 1; row < 5; row++) {
            dnaValidatorService.validateVertical(dna, row, 4, verticalCounter, mutantCounter)
        }

        then:
        mutantCounter.getValue() == 3
    }
}
