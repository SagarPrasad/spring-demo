/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.hystrix;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

/**
 * @author sagar
 */
@Slf4j
public class MDCPropagatingHystrixCommandExecutionHook extends HystrixCommandExecutionHook {

  private HystrixRequestVariableDefault<Map<String, String>> mdcContextVariable = new HystrixRequestVariableDefault<>();

  @Override
  public <T> void onStart(HystrixInvokable<T> commandInstance) {
    extractMDCContext();
  }

  @Override
  public <T> void onThreadStart(HystrixInvokable<T> commandInstance) {
    setupMDCContext();
  }


  @Override
  public <T> void onUnsubscribe(HystrixInvokable<T> commandInstance) {
    cleanup();
  }

  @Override
  public <T> void onFallbackStart(HystrixInvokable<T> commandInstance) {
    setupMDCContext();
  }

  @Override
  public <T> void onFallbackSuccess(HystrixInvokable<T> commandInstance) {
    cleanup();
  }

  private void extractMDCContext() {
    HystrixRequestContext.initializeContext();
    Map<String, String> originalMDCContext = MDC.getCopyOfContextMap();
    if (originalMDCContext != null && !originalMDCContext.isEmpty()) {
      mdcContextVariable.set(originalMDCContext);
    }
  }

  private void setupMDCContext() {
    if (HystrixRequestContext.getContextForCurrentThread() == null) {
      extractMDCContext();
    }
    if (mdcContextVariable.get() != null) {
      MDC.setContextMap(mdcContextVariable.get());
    }
  }


  private void cleanup() {
    HystrixRequestContext.getContextForCurrentThread().shutdown();
  }

  //https://github.com/Netflix/Hystrix/issues/1653
}
