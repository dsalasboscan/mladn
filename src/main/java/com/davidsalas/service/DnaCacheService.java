package com.davidsalas.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class DnaCacheService {

  private ConcurrentHashMap<String, Boolean> dnaCacheMap = new ConcurrentHashMap<>();
  private static final int CACHE_SIZE_LIMIT = 1000;

  public void put(String hashedDna, boolean isMutant) {
    dnaCacheMap.putIfAbsent(hashedDna, isMutant);
  }

  public Boolean containsDna(String hashedDna) {
    return dnaCacheMap.containsKey(hashedDna);
  }

  public Boolean get(String hashedDna) {
    return dnaCacheMap.get(hashedDna);
  }

  @Scheduled(fixedDelay = 600000, initialDelay = 600000)
  public void evictCache() {
    if (CACHE_SIZE_LIMIT == dnaCacheMap.size()) {
      dnaCacheMap.clear();
    }
  }
}