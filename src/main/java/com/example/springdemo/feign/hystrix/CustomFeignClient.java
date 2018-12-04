/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.feign.hystrix;

import com.example.springdemo.config.EnableDynamicConfig;
import com.example.springdemo.config.EnableDynamicMockValue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author sagar
 */
@EnableDynamicConfig
@FeignClient(name = "custom", url = "http://localhost:8080", configuration = CustomFeignClientConfig.class)
public interface CustomFeignClient {

  @EnableDynamicMockValue(value = "custom-customService")
  @RequestMapping(method = RequestMethod.GET, value = "/customService")
  String getIntegrationResponse();

  @EnableDynamicMockValue(value = "custom-feignService")
  @RequestMapping(method = RequestMethod.GET, value = "/customFeignService")
  IntegrationResponse getCustomIntegrationResponse();

}
