package com.davidsalas.model.dto;

public class ErrorResponseDto {

  private String message;

  public ErrorResponseDto(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}