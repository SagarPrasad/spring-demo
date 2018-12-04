/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.feign.hystrix;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sagar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationResponse {
  String name;
  Integer age;
}
