/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.rx;

import io.reactivex.functions.Function;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

/**
 * @author sagar
 */
@Slf4j
public class ThreadLocalPropogator implements Function<Runnable, Runnable> {

// https://jmnarloch.wordpress.com/2016/07/06/spring-boot-hystrix-and-threadlocals/

  ThreadLocalPropogator() {
  }

  @Override
  public Runnable apply(Runnable runnable) {
    Map<String, String> map = MDC.getCopyOfContextMap();
    return () -> {
      try {
        MDC.setContextMap(map);
        runnable.run();
      } finally {
        MDC.clear();
      }
    };
  }
}
