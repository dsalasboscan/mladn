package com.davidsalas.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class Human {

  private Long id;
  private String dnaHash;
  private boolean isMutant;

  public Human(String dnaHash) {
    this.dnaHash = dnaHash;
    this.isMutant = false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Human human = (Human) o;
    return Objects.equals(dnaHash, human.dnaHash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dnaHash);
  }
}