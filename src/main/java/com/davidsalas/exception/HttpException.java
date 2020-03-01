package com.davidsalas.exception;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException {

  private HttpStatus httpStatus;
  private String message;

  public HttpException(HttpStatus status, String message) {
    this.httpStatus = status;
    this.message = message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  @Override
  public String getMessage() {
    return message;
  }
}