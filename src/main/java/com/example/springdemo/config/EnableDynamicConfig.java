/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableDynamicConfig {
}
