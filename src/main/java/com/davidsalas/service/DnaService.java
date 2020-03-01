package com.davidsalas.service;

import com.davidsalas.model.Human;
import com.davidsalas.model.dto.StatDto;
import com.davidsalas.repository.DnaRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DnaService {

  private DnaRepository dnaRepository;
  private TraverseService traverseService;
  private DnaCacheService cacheService;
  private InputValidatorService inputValidatorService;

  public DnaService(TraverseService traverseService, DnaRepository dnaRepository, DnaCacheService cacheService, InputValidatorService inputValidatorService) {
    this.traverseService = traverseService;
    this.dnaRepository = dnaRepository;
    this.cacheService = cacheService;
    this.inputValidatorService = inputValidatorService;
  }

  public boolean isMutant(String[] dna) {
    inputValidatorService.validate(dna);

    String hashedDna = dnaHash(dna);
    Human human = new Human(hashedDna);

    if (cacheService.containsDna(hashedDna)) return cacheService.get(hashedDna);

    if (traverseService.traverse(dna) > 1) {
      human.setMutant(true);
    }

    cacheService.put(hashedDna, human.isMutant());
    dnaRepository.save(human);
    return human.isMutant();
  }

  public StatDto stats() {
    return dnaRepository.getStats();
  }

  private String dnaHash(String[] dna) {
    return DigestUtils.md5Hex(Arrays.toString(dna));
  }
}