/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author sagar
 */
@EnableDynamicConfig
@Service
public class DCService {

  @Value("${config.name:static}")
  @Getter
  String configname;


  //@EnableDynamicMockValue("field.name")
  @Value("${field.name:field}")
  String field;

  @EnableDynamicMockValue(value = "someMethod")
  public String someMethod() {
    return " somevalue";
  }

  public String someFieldValue() {
    return field;
  }

}
