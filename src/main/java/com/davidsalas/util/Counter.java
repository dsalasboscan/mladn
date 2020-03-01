package com.davidsalas.util;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

  private final AtomicInteger counter;

  public Counter(int initalValue) {
    counter = new AtomicInteger(initalValue);
  }

  public int getValue() {
    return counter.get();
  }

  public void increment() {
    while (true) {
      int existingValue = getValue();
      int newValue = existingValue + 1;
      if (counter.compareAndSet(existingValue, newValue)) {
        return;
      }
    }
  }

  public void resetTo(int value) {
    counter.set(value);
  }
}