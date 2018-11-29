/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.rx;

import com.example.springdemo.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sagar
 */
@RestController
@Slf4j
public class RxResource {

  @Autowired
  RxComponentMDC rxComponentMDC;

  @GetMapping("/rx")
  public String rx(@RequestParam(value = "flow", required = false, defaultValue = "flow") String flow) {

    Utils.setMDC(flow);
    switch (flow) {
      case "flow" : rxComponentMDC.flowableJust(); break;
      case "io" : rxComponentMDC.io(); break;
      case "parallel" : rxComponentMDC.parallel(); break;
      case "concurrency" : rxComponentMDC.concurrency(); break;
      case "deffered" : rxComponentMDC.backCallable(); break;
      case "def" : rxComponentMDC.defaultM();
    }
    return "success";
  }



}
