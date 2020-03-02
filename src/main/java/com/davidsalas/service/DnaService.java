package com.davidsalas.service;

import com.davidsalas.model.Human;
import com.davidsalas.model.dto.StatDto;
import com.davidsalas.repository.DnaRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DnaService {

  private DnaRepository dnaRepository;
  private TraverseService traverseService;
  private InputValidatorService inputValidatorService;

  private static final Logger logger = LoggerFactory.getLogger(DnaService.class);

  public DnaService(TraverseService traverseService, DnaRepository dnaRepository, InputValidatorService inputValidatorService) {
    this.traverseService = traverseService;
    this.dnaRepository = dnaRepository;
    this.inputValidatorService = inputValidatorService;
  }

  @Cacheable(cacheNames = "dna")
  public boolean isMutant(String[] dna) {
    logger.info("Detecting if human is mutant with dna: {}", Arrays.toString(dna));

    String dnaHash = doHashDna(dna);
    Human human = dnaRepository.getHuman(dnaHash);

    if (human != null) {
      return human.isMutant();
    }

    inputValidatorService.validate(dna);

    human = new Human(dnaHash);

    if (traverseService.traverse(dna) > 1) {
      human.setMutant(true);
    }

    dnaRepository.save(human);

    logger.info("Result of human dna validation, is mutant: {}", human.isMutant());
    return human.isMutant();
  }

  public StatDto stats() {
    return dnaRepository.getStats();
  }

  private String doHashDna(String[] dna) {
    return DigestUtils.md5Hex(Arrays.toString(dna));
  }
}