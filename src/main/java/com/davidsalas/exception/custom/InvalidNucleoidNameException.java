package com.davidsalas.exception.custom;

import com.davidsalas.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InvalidNucleoidNameException extends HttpException {

  public InvalidNucleoidNameException() {
    super(HttpStatus.BAD_REQUEST, "DNA's nucleoid must be one of A, T, C, G");
  }
}