package com.davidsalas.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatDto {

  @JsonProperty("count_mutant_dna")
  private long mutantDnaCount;

  @JsonProperty("count_human_dna")
  private long humanDnaCount;

  private Double ratio;

  public StatDto(long mutantDnaCount, long humanDnaCount, Double ratio) {
    this.mutantDnaCount = mutantDnaCount;
    this.humanDnaCount = humanDnaCount;
    this.ratio = ratio;
  }
}