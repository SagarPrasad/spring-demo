/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.circuitbreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sagar
 */
@RestController
public class ShakySpringResource {

  @Autowired
  ShakySpringService shakySpringService;

  @GetMapping("/bang")
  public String boom() throws Exception {
    return shakySpringService.shakyMethod();
  }

}
