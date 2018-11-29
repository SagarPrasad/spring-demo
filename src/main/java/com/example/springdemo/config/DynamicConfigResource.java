/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sagar
 */
@RestController
public class DynamicConfigResource {

  @Autowired
  DCService dcService;

  @Autowired
  ConfigUtils configUtils;

  @GetMapping("/config")
  public String config() {
    return dcService.getConfigname() + dcService.someMethod() + "--" + dcService.someFieldValue();
  }

  @GetMapping("/real-config")
  public Map<String, Object> getconfig() {
    return configUtils.getAppConfigs();
  }

  @PutMapping("/update-config")
  public Map<String, Object> updateconfig(HttpServletRequest request,
                                          @RequestBody Map<String, Object> keyValue) {
    configUtils.updateAppConfigs(keyValue);
    System.out.println(keyValue);
    return keyValue;
  }

}
