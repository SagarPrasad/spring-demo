/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.hystrix;

import com.netflix.hystrix.strategy.HystrixPlugins;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author sagar
 */
@Component
@ConditionalOnProperty(prefix = "mdc.hystrix", value = "enable", matchIfMissing = false)
public class HystrixMDCEnabler implements InitializingBean
{
  @Override
  public void afterPropertiesSet()
  {
    HystrixPlugins.reset();
    HystrixPlugins.getInstance().registerCommandExecutionHook(new MDCPropagatingHystrixCommandExecutionHook());
  }
}
