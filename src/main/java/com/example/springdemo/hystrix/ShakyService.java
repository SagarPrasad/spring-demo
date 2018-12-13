/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * @author sagar
 */
@Service
@Slf4j
public class ShakyService {

  @HystrixCommand(fallbackMethod = "fallback")
  public String shakyMethod(){
    log.info(" I am boom service " + MDC.get("KEY"));
    if (Math.random() > 0.5) {
      try {
        Thread.sleep(2000L);
      } catch (InterruptedException e) {
        //
      }
      log.info("----->>Throwing Error<<-----");
      throw new BoomException();
    }
    return "boom";
  }


  public String fallback() {
    log.info(" I am boom service fallback" + MDC.get("KEY"));
    log.info("----->>Fallback<<-----");
    return "boom-fallback";
  }
}
