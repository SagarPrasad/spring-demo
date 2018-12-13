/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.feign.hystrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sagar
 */
@RestController
@Slf4j
public class FeignController {

  @Autowired
  FeignService feignService;

  @GetMapping("/feign-client")
  public String caller() throws Exception {
    return feignService.callService();
  }


  @GetMapping("/customService")
  public String called() throws Exception {
    Thread.sleep(500);
    return "response from called";
  }


  @GetMapping("/customFeignService")
  public IntegrationResponse custome() throws Exception {
    Thread.sleep(500);
    return new IntegrationResponse("test", 30);
  }


  @GetMapping("/feign-custom-client")
  public IntegrationResponse callercus() throws Exception {
    return feignService.callCustomService();
  }

}
