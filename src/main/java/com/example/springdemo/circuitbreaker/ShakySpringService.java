/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.circuitbreaker;

import com.example.springdemo.hystrix.BoomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

/**
 * @author sagar
 */
@Service
@Slf4j
public class ShakySpringService {


  //@Retryable(include = BoomException.class, maxAttempts = 1)
  @CircuitBreaker(include = BoomException.class, maxAttempts = 2, resetTimeout = 3000)
  public String shakyMethod(){

    if (Math.random() > 0.5) {
      try {
        Thread.sleep(2000L);
      } catch (InterruptedException e) {
        //
      }
      log.info("----->>Throwing Spring Error<<-----");
      throw new BoomException();
    }
    return "bang";
  }

  @Recover
  public String fallback(BoomException exception) {
    log.info("----->>Fallback Recover<<-----");
    return "bang-fallback";
  }
}
