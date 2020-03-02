package com.davidsalas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MlAdnApplication {

  public static void main(String[] args) {
    SpringApplication.run(MlAdnApplication.class, args);
  }
}
