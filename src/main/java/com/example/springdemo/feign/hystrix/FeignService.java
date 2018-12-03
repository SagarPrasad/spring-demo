/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.feign.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sagar
 */
@Service
public class FeignService {

  @Autowired
  CustomFeignClient customFeignClient;

  public String callService() {
    return customFeignClient.getIntegrationResponse();
  }

}
