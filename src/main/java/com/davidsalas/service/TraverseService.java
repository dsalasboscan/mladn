package com.davidsalas.service;

import com.davidsalas.model.DiagonalValidationDirection;
import com.davidsalas.util.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class TraverseService {

  private DnaValidatorService dnaValidatorService;
  private ExecutorService executorService;

  private static final Logger logger = LoggerFactory.getLogger(TraverseService.class);

  public TraverseService(DnaValidatorService dnaValidatorService, ExecutorService executorService) {
    this.dnaValidatorService = dnaValidatorService;
    this.executorService = executorService;
  }

  public int traverse(String[] dna) {
    Counter mutantCount = new Counter(0);
    int midPoint = (dna[0].length() / 2);
    List<Future<?>> traverseTasks;

    if (dna.length > 3) {
      traverseTasks = initTraverseMatrixTasks(dna, midPoint, mutantCount);
    } else {
      traverseTasks = initTraverseNonMatrixTasks(dna, midPoint, mutantCount);
    }

    while (mutantCount.getValue() < 2) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        logger.error(e.getMessage());
      }

      if (traverseTasks.stream().allMatch(Future::isDone)) return mutantCount.getValue();
    }

    return mutantCount.getValue();
  }

  private List<Future<?>> initTraverseMatrixTasks(String[] dna, int midPoint, Counter mutantCount) {
    List<Future<?>> tasks = new ArrayList<>();

    tasks.add(executorService.submit(traverseVerticalFromRight(dna, midPoint, mutantCount)));
    tasks.add(executorService.submit(traverseVerticalFromLeft(dna, midPoint, mutantCount)));

    tasks.add(executorService.submit(traverseHorizontalFromUp(dna, midPoint, mutantCount)));
    tasks.add(executorService.submit(traverseHorizontalFromDown(dna, midPoint, mutantCount)));

    tasks.add(executorService.submit(traverseDiagonalFromRightUp(dna, mutantCount)));
    tasks.add(executorService.submit(traverseDiagonalFromLeftUp(dna, mutantCount)));
    tasks.add(executorService.submit(traverseDiagonalFromRightDown(dna, mutantCount)));
    tasks.add(executorService.submit(traverseDiagonalLeftDown(dna, mutantCount)));

    return tasks;
  }

  private List<Future<?>> initTraverseNonMatrixTasks(String[] dna, int midPoint, Counter mutantCount) {
    List<Future<?>> tasks = new ArrayList<>();

    tasks.add(executorService.submit(traverseVerticalFromRight(dna, midPoint, mutantCount)));
    tasks.add(executorService.submit(traverseVerticalFromLeft(dna, midPoint, mutantCount)));

    return tasks;
  }

  private Runnable traverseVerticalFromRight(String[] dna, int midPoint, Counter mutantCounter) {
    return () -> {
      Counter verticalCounter = new Counter(1);
      for (int col = dna[0].length() - 1; col >= midPoint; col--) {
        for (int row = 1; row < dna.length; row++) {
          if (mutantCounter.getValue() > 1) return;
          dnaValidatorService.validateVertical(dna, row, col, verticalCounter, mutantCounter);
        }
      }
    };
  }

  private Runnable traverseVerticalFromLeft(String[] dna, int midPoint, Counter mutantCounter) {
    return () -> {
      Counter verticalCounter = new Counter(1);
      for (int col = 0; col <= midPoint; col++) {
        for (int row = 1; row < dna.length; row++) {
          if (mutantCounter.getValue() > 1) return;
          dnaValidatorService.validateVertical(dna, row, col, verticalCounter, mutantCounter);
        }
      }
    };
  }

  private Runnable traverseHorizontalFromUp(String[] dna, int midPoint, Counter mutantCounter) {
    return () -> {
      Counter horizontalCounter = new Counter(1);
      for (int row = 0; row < midPoint; row++) {
        for (int col = 1; col < dna[0].length(); col++) {
          if (mutantCounter.getValue() > 1) return;
          dnaValidatorService.validateHorizontal(dna, row, col, horizontalCounter, mutantCounter);
        }
      }
    };
  }

  private Runnable traverseHorizontalFromDown(String[] dna, int midPoint, Counter mutantCounter) {
    return () -> {
      Counter horizontalCounter = new Counter(1);
      for (int col = dna[0].length() - 1; col > midPoint; col--) {
        for (int row = 1; row < dna.length - 1; row++) {
          if (mutantCounter.getValue() > 1) return;
          dnaValidatorService.validateHorizontal(dna, row, col, horizontalCounter, mutantCounter);
        }
      }
    };
  }

  private Runnable traverseDiagonalFromRightUp(String[] dna, Counter mutantCount) {
    return () -> {
      Counter diagonalCounter = new Counter(1);
      for (int rowPivot = 3; rowPivot < dna.length - 1; rowPivot++) {
        for (int row = rowPivot, col = dna[0].length() - 1; row > 0; row--, col--) {
          if (mutantCount.getValue() > 1) return;
          dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.LEFT_UP, diagonalCounter,
              mutantCount);
        }
      }
    };
  }

  private Runnable traverseDiagonalLeftDown(String[] dna, Counter mutantCount) {
    return () -> {
      Counter diagonalCounter = new Counter(1);
      for (int rowPivot = dna.length - 4; rowPivot >= 0; rowPivot--) {
        for (int row = rowPivot, col = 0; dna.length - row > 1; row++, col++) {
          if (mutantCount.getValue() > 1) return;
          dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.RIGHT_DOWN, diagonalCounter,
              mutantCount);
        }
      }
    };
  }

  private Runnable traverseDiagonalFromLeftUp(String[] dna, Counter mutantCount) {
    return () -> {
      Counter diagonalCounter = new Counter(1);
      for (int rowPivot = 3; rowPivot < dna.length - 1; rowPivot++) {
        for (int row = rowPivot, col = 0; row > 0; row--, col++) {
          if (mutantCount.getValue() > 1) return;
          dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.RIGHT_UP, diagonalCounter,
              mutantCount);
        }
      }
    };
  }

  private Runnable traverseDiagonalFromRightDown(String[] dna, Counter mutantCount) {
    return () -> {
      Counter diagonalCounter = new Counter(1);
      for (int rowPivot = dna.length - 4; rowPivot >= 0; rowPivot--) {
        for (int row = rowPivot, col = dna[0].length() - 1; dna.length - row > 1; row++, col--) {
          if (mutantCount.getValue() > 1) return;
          dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.LEFT_DOWN, diagonalCounter,
              mutantCount);
        }
      }
    };
  }
}