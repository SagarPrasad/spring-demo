/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.hystrix;

import com.example.springdemo.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sagar
 */
@RestController
@Slf4j
public class ShakyResource {

  @Autowired
  ShakyService shakyService;

  @GetMapping("/boom")
  public String boom() throws Exception {
    Utils.setMDC("BoomMDC");
    log.info(" I am boom resource " + MDC.get("KEY"));
    return shakyService.shakyMethod();
  }

}
