/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.rx;

import io.reactivex.plugins.RxJavaPlugins;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author sagar
 */
@Component
@ConditionalOnProperty(prefix = "mdc.rx", value = "enable", matchIfMissing = false)
public class RxMDCEnabler implements InitializingBean
{
  @Override
  public void afterPropertiesSet()
  {
    RxJavaPlugins.setScheduleHandler(new ThreadLocalPropogator());
  }
}
