/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sagar
 */
@Configuration
@ConfigurationProperties(prefix = "com.example.config")
@Data
public class DynamicConfigurations {
  private List<String> sampleList;
  private String sampleString;
  private boolean sampleBoolean;
  private BigDecimal sampleDecimal;
  private Integer sampleInteger;
  private int sampleint;
  private Map<String, String> sampleMap;
  private Set<String> sampleSet;

}