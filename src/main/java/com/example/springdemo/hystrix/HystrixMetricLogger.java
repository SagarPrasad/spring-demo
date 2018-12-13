/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.hystrix;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author sagar
 */
@Service
@Slf4j
public class HystrixMetricLogger {

  //https://github.com/ericdahl/hello-hystrix/tree/master/src/main/java/example
  @Scheduled(fixedRate = 5000)
  public void something() {
    final HystrixCommandMetrics metrics = HystrixCommandMetrics.getInstance(HystrixCommandKey.Factory.asKey
            ("shakyMethod"));

    if (metrics != null) { // metrics will be null until command is first used
      log.info("command [{}]: execTime [{}] errors [{}]/[{}]  {} {} {}", metrics.getCommandKey().name(),
              metrics.getExecutionTimeMean(),
              metrics.getHealthCounts().getErrorCount(),
              metrics.getHealthCounts().getTotalRequests(),
              metrics.getProperties().circuitBreakerEnabled().get(),
              metrics.getExecutionTimePercentile(95), metrics.getExecutionTimePercentile(90));
    }
  }
}
