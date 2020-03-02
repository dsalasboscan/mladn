package com.davidsalas.service;

import com.davidsalas.model.Human;
import com.davidsalas.model.dto.StatDto;
import com.davidsalas.repository.DnaRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DnaService {

  private DnaRepository dnaRepository;
  private TraverseService traverseService;
  private InputValidatorService inputValidatorService;

  public DnaService(TraverseService traverseService, DnaRepository dnaRepository, InputValidatorService inputValidatorService) {
    this.traverseService = traverseService;
    this.dnaRepository = dnaRepository;
    this.inputValidatorService = inputValidatorService;
  }

  @Cacheable(cacheNames = "dna")
  public boolean isMutant(String[] dna) {
    inputValidatorService.validate(dna);

    Human human = new Human();

    if (traverseService.traverse(dna) > 1) {
      human.setMutant(true);
    }

    dnaRepository.save(human);
    return human.isMutant();
  }

  public StatDto stats() {
    return dnaRepository.getStats();
  }
}