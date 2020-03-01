package com.davidsalas.model.dto;

import javax.validation.constraints.NotNull;

public class DnaDto {

  @NotNull
  private String[] dna;

  public String[] getDna() {
    return dna;
  }

  public void setDna(String[] dna) {
    this.dna = dna;
  }
}