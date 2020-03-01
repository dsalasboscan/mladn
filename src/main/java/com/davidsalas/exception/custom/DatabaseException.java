package com.davidsalas.exception.custom;

import com.davidsalas.exception.HttpException;
import org.springframework.http.HttpStatus;

public class DatabaseException extends HttpException {

  public DatabaseException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}