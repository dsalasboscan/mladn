package com.davidsalas.exception.custom;

import com.davidsalas.exception.HttpException;
import org.springframework.http.HttpStatus;

public class ArrayDimensionsException extends HttpException {

  public ArrayDimensionsException() {
    super(HttpStatus.BAD_REQUEST, "The imput array must be NxN");
  }
}