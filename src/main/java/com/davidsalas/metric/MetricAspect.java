package com.davidsalas.metric;

import com.davidsalas.exception.HttpException;
import com.davidsalas.exception.custom.ArrayDimensionsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Aspect
@Configurable
@Component
public class MetricAspect {

/*
  @Pointcut("@annotation(com.davidsalas.metric.Countable(metricName))")
  private void joinpoint(String metricName) {}
*/

  @AfterThrowing(pointcut = "com.davidsalas.exception.arrayDimensionsException()", throwing = "ex")
  public void test(ArrayDimensionsException ex) throws Throwable {
    System.out.println("Hola " /*+ countable.metricName()*/);

  }
}
