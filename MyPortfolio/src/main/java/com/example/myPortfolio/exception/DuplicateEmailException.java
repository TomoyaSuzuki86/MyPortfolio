package com.example.myPortfolio.exception;

public class DuplicateEmailException extends RuntimeException {
  public DuplicateEmailException(String message) {
    super(message);
  }
}