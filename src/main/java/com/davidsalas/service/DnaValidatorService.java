package com.davidsalas.service;

import com.davidsalas.model.DiagonalValidationDirection;
import com.davidsalas.util.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DnaValidatorService {

  private static final Logger logger = LoggerFactory.getLogger(DnaValidatorService.class);

  public void validateVertical(String[] dna, int row, int col, Counter verticalCounter, Counter mutantCounter) {
    if (dna[row].charAt(col) == dna[row - 1].charAt(col)) {
      verticalCounter.increment();
    } else {
      verticalCounter.resetTo(1);
    }

    validateIfMutant(verticalCounter, mutantCounter);
  }

  public void validateHorizontal(String[] dna, int row, int col, Counter horizontalCounter, Counter mutantCounter) {
    if (dna[row].charAt(col) == dna[row].charAt(col - 1)) {
      horizontalCounter.increment();
    } else {
      horizontalCounter.resetTo(1);
    }

    validateIfMutant(horizontalCounter, mutantCounter);
  }

  public void validateDiagonal(String[] dna, int row, int col,
                               DiagonalValidationDirection direction, Counter diagonalCounter, Counter mutantCounter) {
    switch (direction) {
      case LEFT_UP:
        if (dna[row].charAt(col) == dna[row - 1].charAt(col - 1)) {
          diagonalCounter.increment();
        } else {
          diagonalCounter.resetTo(1);
        }
        break;
      case RIGHT_UP:
        if (dna[row].charAt(col) == dna[row - 1].charAt(col + 1)) {
          diagonalCounter.increment();
        } else {
          diagonalCounter.resetTo(1);
        }
        break;
      case RIGHT_DOWN:
        if (dna[row].charAt(col) == dna[row + 1].charAt(col + 1)) {
          diagonalCounter.increment();
        } else {
          diagonalCounter.resetTo(1);
        }
        break;
      case LEFT_DOWN:
        if (dna[row].charAt(col) == dna[row + 1].charAt(col - 1)) {
          diagonalCounter.increment();
        } else {
          diagonalCounter.resetTo(1);
        }
        break;
    }

    validateIfMutant(diagonalCounter, mutantCounter);
  }

  private void validateIfMutant(Counter counter, Counter mutantCounter) {
    if (counter.getValue() == 4) {
      mutantCounter.increment();
      counter.resetTo(0);
    }
  }
}