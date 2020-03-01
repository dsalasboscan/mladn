package com.davidsalas.controller;

import com.davidsalas.model.dto.DnaDto;
import com.davidsalas.model.dto.StatDto;
import com.davidsalas.service.DnaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DnaController {

  private DnaService dnaService;

  public DnaController(DnaService dnaService) {
    this.dnaService = dnaService;
  }

  @PostMapping(
      value = "/mutant",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaDto dnaDto) {
    if (dnaService.isMutant(dnaDto.getDna())) return new ResponseEntity<>(HttpStatus.OK);
    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
  }

  @GetMapping(
      value = "/stats",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public StatDto stats() {
    return dnaService.stats();
  }
}