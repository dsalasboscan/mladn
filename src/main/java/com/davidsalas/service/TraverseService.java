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

    List<Future<?>> traverseTasks;

    traverseTasks = initTraverseMatrixTasks(dna, mutantCount);

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

  private List<Future<?>> initTraverseMatrixTasks(String[] dna, Counter mutantCount) {
    List<Future<?>> tasks = new ArrayList<>();
    int midPoint = (dna[0].length() / 2);
    int rowLength = dna.length;
    int colLength = dna[0].length();

    tasks.add(executorService.submit(traverseVerticalFromRight(dna, midPoint, rowLength, colLength, mutantCount)));
    tasks.add(executorService.submit(traverseVerticalFromLeft(dna, midPoint, rowLength, mutantCount)));

    tasks.add(executorService.submit(traverseHorizontalFromUp(dna, midPoint, colLength, mutantCount)));
    tasks.add(executorService.submit(traverseHorizontalFromDown(dna, midPoint, rowLength, colLength, mutantCount)));

    tasks.add(executorService.submit(traverseDiagonalFromRightUp(dna, rowLength, colLength, mutantCount)));
    tasks.add(executorService.submit(traverseDiagonalFromLeftUp(dna, rowLength, mutantCount)));
    tasks.add(executorService.submit(traverseDiagonalFromRightDown(dna, rowLength, colLength, mutantCount)));
    tasks.add(executorService.submit(traverseDiagonalLeftDown(dna, rowLength, mutantCount)));

    return tasks;
  }

  private Runnable traverseVerticalFromRight(String[] dna, int midPoint, int rowLength, int colLength, Counter mutantCounter) {
    return () -> {
      Counter verticalCounter = new Counter(1);
      for (int col = colLength - 1; col >= midPoint; col--) {
        for (int row = 1; row < rowLength; row++) {
          /*if (mutantCounter.getValue() > 1) return;*/
          dnaValidatorService.validateVertical(dna, row, col, verticalCounter, mutantCounter);
        }
        verticalCounter.resetTo(1);
      }
    };
  }

  private Runnable traverseVerticalFromLeft(String[] dna, int midPoint, int rowLength, Counter mutantCounter) {
    return () -> {
      Counter verticalCounter = new Counter(1);
      for (int col = 0; col <= midPoint; col++) {
        for (int row = 1; row < rowLength; row++) {
          if (mutantCounter.getValue() > 1) return;
          dnaValidatorService.validateVertical(dna, row, col, verticalCounter, mutantCounter);
        }
        verticalCounter.resetTo(1);
      }
    };
  }

  private Runnable traverseHorizontalFromUp(String[] dna, int midPoint, int colLength, Counter mutantCounter) {
    return () -> {
      Counter horizontalCounter = new Counter(1);
      for (int row = 0; row < midPoint; row++) {
        for (int col = 1; col < colLength; col++) {
          if (mutantCounter.getValue() > 1) return;
          dnaValidatorService.validateHorizontal(dna, row, col, horizontalCounter, mutantCounter);
        }
        horizontalCounter.resetTo(1);
      }
    };
  }

  private Runnable traverseHorizontalFromDown(String[] dna, int midPoint, int rowLength, int colLength, Counter mutantCounter) {
    return () -> {
      Counter horizontalCounter = new Counter(1);
      for (int row = rowLength - 1; row > midPoint; row--) {
        for (int col = 1; col < colLength; col++) {
          if (mutantCounter.getValue() > 1) return;
          dnaValidatorService.validateHorizontal(dna, row, col, horizontalCounter, mutantCounter);
        }
        horizontalCounter.resetTo(1);
      }
    };
  }

  private Runnable traverseDiagonalFromRightUp(String[] dna, int rowLength, int colLength, Counter mutantCount) {
    return () -> {
      Counter diagonalCounter = new Counter(1);
      for (int rowPivot = 3; rowPivot < rowLength - 1; rowPivot++) {
        for (int row = rowPivot, col = colLength - 1; row > 0; row--, col--) {
          if (mutantCount.getValue() > 1) return;
          dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.LEFT_UP, diagonalCounter,
              mutantCount);
        }
        diagonalCounter.resetTo(1);
      }
    };
  }

  private Runnable traverseDiagonalFromLeftUp(String[] dna, int rowLength, Counter mutantCount) {
    return () -> {
      Counter diagonalCounter = new Counter(1);
      for (int rowPivot = 3; rowPivot < rowLength - 1; rowPivot++) {
        for (int row = rowPivot, col = 0; row > 0; row--, col++) {
          if (mutantCount.getValue() > 1) return;
          dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.RIGHT_UP, diagonalCounter,
              mutantCount);
        }
        diagonalCounter.resetTo(1);
      }
    };
  }

  private Runnable traverseDiagonalLeftDown(String[] dna, int rowLength, Counter mutantCount) {
    return () -> {
      Counter diagonalCounter = new Counter(1);
      for (int rowPivot = rowLength - 4; rowPivot >= 0; rowPivot--) {
        for (int row = rowPivot, col = 0; rowLength - row > 1; row++, col++) {
          if (mutantCount.getValue() > 1) return;
          dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.RIGHT_DOWN, diagonalCounter,
              mutantCount);
        }
        diagonalCounter.resetTo(1);
      }
    };
  }

  private Runnable traverseDiagonalFromRightDown(String[] dna, int rowLength, int colLength, Counter mutantCount) {
    return () -> {
      Counter diagonalCounter = new Counter(1);
      for (int rowPivot = rowLength - 4; rowPivot >= 0; rowPivot--) {
        for (int row = rowPivot, col = colLength - 1; rowLength - row > 1; row++, col--) {
          if (mutantCount.getValue() > 1) return;
          dnaValidatorService.validateDiagonal(dna, row, col, DiagonalValidationDirection.LEFT_DOWN, diagonalCounter,
              mutantCount);
        }
        diagonalCounter.resetTo(1);
      }
    };
  }
}