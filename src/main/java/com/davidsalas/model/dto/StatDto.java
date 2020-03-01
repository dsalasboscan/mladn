package com.davidsalas.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

  public long getMutantDnaCount() {
    return mutantDnaCount;
  }

  public long getHumanDnaCount() {
    return humanDnaCount;
  }

  public Double getRatio() {
    return ratio;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StatDto statDto = (StatDto) o;
    return mutantDnaCount == statDto.mutantDnaCount &&
        humanDnaCount == statDto.humanDnaCount &&
        Objects.equals(ratio, statDto.ratio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mutantDnaCount, humanDnaCount, ratio);
  }

  @Override
  public String toString() {
    return "StatDto{" +
        "mutantDnaCount=" + mutantDnaCount +
        ", humanDnaCount=" + humanDnaCount +
        ", ratio=" + ratio +
        '}';
  }
}