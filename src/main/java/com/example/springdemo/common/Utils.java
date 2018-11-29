/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.common;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sagar
 */
public abstract class Utils {

  public static void setMDC(String flow) {
    Map<String , String> testMap = new HashMap<>();
    testMap.put("KEY", flow);
    MDC.setContextMap(testMap);
  }
}
